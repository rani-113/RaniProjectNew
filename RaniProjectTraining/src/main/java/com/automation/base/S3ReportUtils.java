package com.automation.utils;

import com.automation.services.WeeklyReportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class S3ReportUtils {

    private static final Logger logger = LoggerFactory.getLogger(S3ReportUtils.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Download the most recent weekly report
     * 
     * @param downloadDirectory Directory to download to
     * @return Path to the downloaded report
     */
    public static Path downloadLatestReport(String downloadDirectory) {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.getLatestWeeklyReport(downloadDirectory);
        } catch (Exception e) {
            logger.error("Failed to download latest report: {}", e.getMessage());
            throw new RuntimeException("Failed to download latest report", e);
        } finally {
            manager.close();
        }
    }

    /**
     * Download reports for a specific date range
     * 
     * @param startDate         Start date (inclusive)
     * @param endDate           End date (inclusive)
     * @param downloadDirectory Directory to download to
     * @return List of paths to downloaded reports
     */
    public static List<Path> downloadReportsForDateRange(LocalDate startDate, LocalDate endDate,
            String downloadDirectory) {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.getWeeklyReportsByDateRange(startDate, endDate, downloadDirectory);
        } catch (Exception e) {
            logger.error("Failed to download reports for date range {} to {}: {}",
                    startDate, endDate, e.getMessage());
            throw new RuntimeException("Failed to download reports for date range", e);
        } finally {
            manager.close();
        }
    }

    /**
     * Download reports for the current month
     * 
     * @param downloadDirectory Directory to download to
     * @return List of paths to downloaded reports
     */
    public static List<Path> downloadCurrentMonthReports(String downloadDirectory) {
        LocalDate now = LocalDate.now();
        return downloadReportsForDateRange(
                now.withDayOfMonth(1),
                now.withDayOfMonth(now.lengthOfMonth()),
                downloadDirectory);
    }

    /**
     * Download reports for the previous month
     * 
     * @param downloadDirectory Directory to download to
     * @return List of paths to downloaded reports
     */
    public static List<Path> downloadPreviousMonthReports(String downloadDirectory) {
        LocalDate now = LocalDate.now();
        LocalDate previousMonth = now.minusMonths(1);
        return downloadReportsForDateRange(
                previousMonth.withDayOfMonth(1),
                previousMonth.withDayOfMonth(previousMonth.lengthOfMonth()),
                downloadDirectory);
    }

    /**
     * Download reports for the current quarter
     * 
     * @param downloadDirectory Directory to download to
     * @return List of paths to downloaded reports
     */
        public static List<Path> downloadCurrentQuarterReports(String downloadDirectory) {
        LocalDate now = LocalDate.now();
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.getQuarterlyReports(now.getYear(), currentQuarter, downloadDirectory);
        } catch (Exception e) {
            logger.error("Failed to download current quarter reports: {}", e.getMessage());
            throw new RuntimeException("Failed to download current quarter reports", e);
        } finally {
            manager.close();
        }
    }

    /**
     * Download reports for the current year
     * 
     * @param downloadDirectory Directory to download to
     * @return List of paths to downloaded reports
     */
        public static List<Path> downloadCurrentYearReports(String downloadDirectory) {
        LocalDate now = LocalDate.now();
        
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.getYearlyReports(now.getYear(), downloadDirectory);
        } catch (Exception e) {
            logger.error("Failed to download current year reports: {}", e.getMessage());
            throw new RuntimeException("Failed to download current year reports", e);
        } finally {
            manager.close();
        }
    }

    /**
     * Check if a report exists for today
     * 
     * @return true if report exists, false otherwise
     */
    public static boolean todayReportExists() {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.weeklyReportExists(LocalDate.now());
        } catch (Exception e) {
            logger.error("Failed to check if today's report exists: {}", e.getMessage());
            return false;
        } finally {
            manager.close();
        }
    }

    /**
     * Check if a report exists for a specific date
     * 
     * @param date Date to check
     * @return true if report exists, false otherwise
     */
    public static boolean reportExistsForDate(LocalDate date) {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            return manager.weeklyReportExists(date);
        } catch (Exception e) {
            logger.error("Failed to check if report exists for {}: {}", date, e.getMessage());
            return false;
        } finally {
            manager.close();
        }
    }

    /**
     * Get the date of the latest available report
     * 
     * @return LocalDate of the latest report, or null if no reports found
     */
        public static LocalDate getLatestReportDate() {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            String latestReportKey = manager.getS3Service().getLatestWeeklyReport();
            if (latestReportKey == null) {
                return null;
            }
            
            // Extract date from the report key
            String fileName = latestReportKey.substring(latestReportKey.lastIndexOf('/') + 1);
            if (fileName.contains(".")) {
                String datePart = fileName.split("\\.")[0];
                return LocalDate.parse(datePart, DATE_FORMATTER);
            }
            
            return null;
        } catch (Exception e) {
            logger.error("Failed to get latest report date: {}", e.getMessage());
            return null;
        } finally {
            manager.close();
        }
    }

    /**
     * Get the number of days since the latest report
     * 
     * @return Number of days since latest report, or -1 if no reports found
     */
    public static int getDaysSinceLatestReport() {
        LocalDate latestReportDate = getLatestReportDate();
        if (latestReportDate == null) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.DAYS.between(latestReportDate, today);
    }

    /**
     * Check if reports are up to date (within specified number of days)
     * 
     * @param maxDaysOld Maximum number of days old for reports to be considered up
     *                   to date
     * @return true if reports are up to date, false otherwise
     */
    public static boolean areReportsUpToDate(int maxDaysOld) {
        int daysSinceLatest = getDaysSinceLatestReport();
        if (daysSinceLatest == -1) {
            return false; // No reports found
        }

        return daysSinceLatest <= maxDaysOld;
    }

    /**
     * Get a summary of available reports
     * 
     * @return Summary string with report information
     */
        public static String getReportsSummary() {
        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            List<String> allReports = manager.getS3Service().getAllWeeklyReports();
            
            if (allReports.isEmpty()) {
                return "No weekly reports found in S3 bucket";
            }
            
            LocalDate latestDate = getLatestReportDate();
            int daysSinceLatest = getDaysSinceLatestReport();
            
            StringBuilder summary = new StringBuilder();
            summary.append("Reports Summary:\n");
            summary.append("- Total reports available: ").append(allReports.size()).append("\n");
            summary.append("- Latest report date: ").append(latestDate != null ? latestDate : "Unknown").append("\n");
            summary.append("- Days since latest report: ").append(daysSinceLatest).append("\n");
            summary.append("- Reports up to date: ").append(areReportsUpToDate(7)).append("\n");
            
            return summary.toString();
            
        } catch (Exception e) {
            logger.error("Failed to get reports summary: {}", e.getMessage());
            return "Failed to get reports summary: " + e.getMessage();
        } finally {
            manager.close();
        }
    }
}