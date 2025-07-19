package com.automation.services;

import com.automation.config.AWSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class WeeklyReportManager implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportManager.class);
    private final S3Service s3Service;
    private final AWSConfig awsConfig;

    public WeeklyReportManager() {
        this.awsConfig = new AWSConfig();
        this.awsConfig.validateConfiguration();
        this.s3Service = new S3Service(
                awsConfig.getAccessKeyId(),
                awsConfig.getSecretAccessKey());
    }

    public WeeklyReportManager(String accessKeyId, String secretAccessKey) {
        this.awsConfig = new AWSConfig(accessKeyId, secretAccessKey);
        this.s3Service = new S3Service(accessKeyId, secretAccessKey);
    }

    /**
     * Get the current week's report
     * 
     * @param downloadDirectory Directory to download the report to
     * @return Path to the downloaded report
     */
    public Path getCurrentWeekReport(String downloadDirectory) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        logger.info("Fetching current week report ({} to {})", startOfWeek, endOfWeek);
        return getWeeklyReport(startOfWeek, endOfWeek, downloadDirectory);
    }

    /**
     * Get the previous week's report
     * 
     * @param downloadDirectory Directory to download the report to
     * @return Path to the downloaded report
     */
    public Path getPreviousWeekReport(String downloadDirectory) {
        LocalDate today = LocalDate.now();
        LocalDate startOfPreviousWeek = today.minusWeeks(1)
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfPreviousWeek = startOfPreviousWeek
                .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        logger.info("Fetching previous week report ({} to {})", startOfPreviousWeek, endOfPreviousWeek);
        return getWeeklyReport(startOfPreviousWeek, endOfPreviousWeek, downloadDirectory);
    }

    /**
     * Get a specific week's report
     * 
     * @param weekStartDate     Start date of the week (Monday)
     * @param downloadDirectory Directory to download the report to
     * @return Path to the downloaded report
     */
    public Path getSpecificWeekReport(LocalDate weekStartDate, String downloadDirectory) {
        LocalDate endOfWeek = weekStartDate.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        logger.info("Fetching specific week report ({} to {})", weekStartDate, endOfWeek);
        return getWeeklyReport(weekStartDate, endOfWeek, downloadDirectory);
    }

    /**
     * Get the latest available weekly report
     * 
     * @param downloadDirectory Directory to download the report to
     * @return Path to the downloaded report
     */
    public Path getLatestWeeklyReport(String downloadDirectory) {
        try {
            String latestReportKey = s3Service.getLatestWeeklyReport();
            if (latestReportKey == null) {
                throw new RuntimeException("No weekly reports found in S3 bucket");
            }

            String fileName = latestReportKey.substring(latestReportKey.lastIndexOf('/') + 1);
            String localPath = downloadDirectory + "/" + fileName;

            Path downloadedPath = s3Service.downloadReport(latestReportKey, localPath);
            logger.info("Downloaded latest weekly report: {}", downloadedPath);
            return downloadedPath;

        } catch (Exception e) {
            logger.error("Failed to get latest weekly report: {}", e.getMessage());
            throw new RuntimeException("Failed to get latest weekly report", e);
        }
    }

    /**
     * Get weekly reports for the last N weeks
     * 
     * @param numberOfWeeks     Number of weeks to fetch
     * @param downloadDirectory Directory to download the reports to
     * @return List of paths to downloaded reports
     */
    public List<Path> getLastNWeeksReports(int numberOfWeeks, String downloadDirectory) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(numberOfWeeks)
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endDate = today;

        logger.info("Fetching last {} weeks reports ({} to {})", numberOfWeeks, startDate, endDate);
        return getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);
    }

    /**
     * Get weekly reports for a specific month
     * 
     * @param year              Year
     * @param month             Month (1-12)
     * @param downloadDirectory Directory to download the reports to
     * @return List of paths to downloaded reports
     */
    public List<Path> getMonthlyReports(int year, int month, String downloadDirectory) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        logger.info("Fetching monthly reports for {}-{} ({} to {})", year, month, startDate, endDate);
        return getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);
    }

    /**
     * Get weekly reports for a specific quarter
     * 
     * @param year              Year
     * @param quarter           Quarter (1-4)
     * @param downloadDirectory Directory to download the reports to
     * @return List of paths to downloaded reports
     */
    public List<Path> getQuarterlyReports(int year, int quarter, String downloadDirectory) {
        LocalDate startDate = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);

        logger.info("Fetching quarterly reports for Q{} {} ({} to {})", quarter, year, startDate, endDate);
        return getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);
    }

    /**
     * Get weekly reports for a specific year
     * 
     * @param year              Year
     * @param downloadDirectory Directory to download the reports to
     * @return List of paths to downloaded reports
     */
    public List<Path> getYearlyReports(int year, String downloadDirectory) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        logger.info("Fetching yearly reports for {} ({} to {})", year, startDate, endDate);
        return getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);
    }

    /**
     * Get all available weekly reports
     * 
     * @param downloadDirectory Directory to download the reports to
     * @return List of paths to downloaded reports
     */
    public List<Path> getAllWeeklyReports(String downloadDirectory) {
        try {
            List<String> allReportKeys = s3Service.getAllWeeklyReports();
            List<Path> downloadedPaths = s3Service.downloadWeeklyReportsByDateRange(
                    LocalDate.MIN, LocalDate.MAX, downloadDirectory);

            logger.info("Downloaded all {} weekly reports to {}", allReportKeys.size(), downloadDirectory);
            return downloadedPaths;

        } catch (Exception e) {
            logger.error("Failed to get all weekly reports: {}", e.getMessage());
            throw new RuntimeException("Failed to get all weekly reports", e);
        }
    }

    /**
     * Check if a weekly report exists for a specific date
     * 
     * @param date Date to check
     * @return true if report exists, false otherwise
     */
    public boolean weeklyReportExists(LocalDate date) {
        try {
            List<String> allReports = s3Service.getAllWeeklyReports();
            String dateString = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            boolean exists = allReports.stream()
                    .anyMatch(key -> key.contains(dateString));

            logger.info("Weekly report exists for {}: {}", date, exists);
            return exists;

        } catch (Exception e) {
            logger.error("Failed to check if weekly report exists for {}: {}", date, e.getMessage());
            return false;
        }
    }

    /**
     * Get report metadata for a specific date
     * 
     * @param date Date of the report
     * @return Report metadata or null if not found
     */
    public software.amazon.awssdk.services.s3.model.HeadObjectResponse getReportMetadata(LocalDate date) {
        try {
            List<String> allReports = s3Service.getAllWeeklyReports();
            String dateString = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String reportKey = allReports.stream()
                    .filter(key -> key.contains(dateString))
                    .findFirst()
                    .orElse(null);

            if (reportKey != null) {
                return s3Service.getReportMetadata(reportKey);
            }

            logger.warn("No report found for date: {}", date);
            return null;

        } catch (Exception e) {
            logger.error("Failed to get report metadata for {}: {}", date, e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to get weekly report for a date range
     */
    private Path getWeeklyReport(LocalDate startDate, LocalDate endDate, String downloadDirectory) {
        try {
            List<Path> downloadedPaths = getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);

            if (downloadedPaths.isEmpty()) {
                throw new RuntimeException("No reports found for the specified date range");
            }

            // Return the first (or only) report
            return downloadedPaths.get(0);

        } catch (Exception e) {
            logger.error("Failed to get weekly report for date range {} to {}: {}",
                    startDate, endDate, e.getMessage());
            throw new RuntimeException("Failed to get weekly report", e);
        }
    }

    /**
     * Helper method to get weekly reports by date range
     */
    public List<Path> getWeeklyReportsByDateRange(LocalDate startDate, LocalDate endDate, String downloadDirectory) {
        try {
            // Create download directory if it doesn't exist
            Path downloadPath = Paths.get(downloadDirectory);
            if (!java.nio.file.Files.exists(downloadPath)) {
                java.nio.file.Files.createDirectories(downloadPath);
            }

            List<Path> downloadedPaths = s3Service.downloadWeeklyReportsByDateRange(
                    startDate, endDate, downloadDirectory);

            logger.info("Downloaded {} reports for date range {} to {}",
                    downloadedPaths.size(), startDate, endDate);
            return downloadedPaths;

        } catch (Exception e) {
            logger.error("Failed to get weekly reports by date range: {}", e.getMessage());
            throw new RuntimeException("Failed to get weekly reports by date range", e);
        }
    }

    /**
     * Get the S3 service instance
     */
    public S3Service getS3Service() {
        return s3Service;
    }
    
    /**
     * Close the S3 service
     */
    @Override
    public void close() {
        if (s3Service != null) {
            s3Service.close();
        }
    }
}