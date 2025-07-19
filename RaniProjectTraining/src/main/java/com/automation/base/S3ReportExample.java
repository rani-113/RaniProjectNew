package com.automation.examples;

import com.automation.services.WeeklyReportManager;
import com.automation.utils.S3ReportUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/**
 * Example class demonstrating how to use the S3 integration for fetching weekly
 * reports
 */
public class S3ReportExample {

    private static final Logger logger = LoggerFactory.getLogger(S3ReportExample.class);

    public static void main(String[] args) {
        logger.info("Starting S3 Report Example");

        try {
            // Example 1: Get the latest weekly report
            exampleGetLatestReport();

            // Example 2: Get current week's report
            exampleGetCurrentWeekReport();

            // Example 3: Get previous week's report
            exampleGetPreviousWeekReport();

            // Example 4: Get last 4 weeks reports
            exampleGetLastNWeeksReports();

            // Example 5: Get monthly reports
            exampleGetMonthlyReports();

            // Example 6: Check report availability
            exampleCheckReportAvailability();

            // Example 7: Get reports summary
            exampleGetReportsSummary();

        } catch (Exception e) {
            logger.error("Example failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Example: Get the latest weekly report
     */
    public static void exampleGetLatestReport() {
        logger.info("=== Example 1: Get Latest Weekly Report ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            Path reportPath = manager.getLatestWeeklyReport("target/downloads/latest");
            logger.info("Latest report downloaded to: {}", reportPath);

            // Get report metadata
            var metadata = manager.getReportMetadata(LocalDate.now());
            if (metadata != null) {
                logger.info("Report size: {} bytes", metadata.contentLength());
                logger.info("Last modified: {}", metadata.lastModified());
            }

        } catch (Exception e) {
            logger.error("Failed to get latest report: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Get current week's report
     */
    public static void exampleGetCurrentWeekReport() {
        logger.info("=== Example 2: Get Current Week Report ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            Path reportPath = manager.getCurrentWeekReport("target/downloads/current-week");
            logger.info("Current week report downloaded to: {}", reportPath);

        } catch (Exception e) {
            logger.error("Failed to get current week report: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Get previous week's report
     */
    public static void exampleGetPreviousWeekReport() {
        logger.info("=== Example 3: Get Previous Week Report ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            Path reportPath = manager.getPreviousWeekReport("target/downloads/previous-week");
            logger.info("Previous week report downloaded to: {}", reportPath);

        } catch (Exception e) {
            logger.error("Failed to get previous week report: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Get last N weeks reports
     */
    public static void exampleGetLastNWeeksReports() {
        logger.info("=== Example 4: Get Last 4 Weeks Reports ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            List<Path> reportPaths = manager.getLastNWeeksReports(4, "target/downloads/last-4-weeks");
            logger.info("Downloaded {} reports for last 4 weeks", reportPaths.size());

            for (Path reportPath : reportPaths) {
                logger.info("  - {}", reportPath.getFileName());
            }

        } catch (Exception e) {
            logger.error("Failed to get last 4 weeks reports: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Get monthly reports
     */
    public static void exampleGetMonthlyReports() {
        logger.info("=== Example 5: Get Monthly Reports ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            LocalDate now = LocalDate.now();
            List<Path> reportPaths = manager.getMonthlyReports(
                    now.getYear(),
                    now.getMonthValue(),
                    "target/downloads/monthly");

            logger.info("Downloaded {} reports for current month", reportPaths.size());

            for (Path reportPath : reportPaths) {
                logger.info("  - {}", reportPath.getFileName());
            }

        } catch (Exception e) {
            logger.error("Failed to get monthly reports: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Check report availability
     */
    public static void exampleCheckReportAvailability() {
        logger.info("=== Example 6: Check Report Availability ===");

        WeeklyReportManager manager = new WeeklyReportManager();
        try {
            // Check if today's report exists
            boolean todayExists = manager.weeklyReportExists(LocalDate.now());
            logger.info("Today's report exists: {}", todayExists);

            // Check if yesterday's report exists
            boolean yesterdayExists = manager.weeklyReportExists(LocalDate.now().minusDays(1));
            logger.info("Yesterday's report exists: {}", yesterdayExists);

            // Check if last week's report exists
            boolean lastWeekExists = manager.weeklyReportExists(LocalDate.now().minusWeeks(1));
            logger.info("Last week's report exists: {}", lastWeekExists);

        } catch (Exception e) {
            logger.error("Failed to check report availability: {}", e.getMessage());
        } finally {
            manager.close();
        }
    }

    /**
     * Example: Get reports summary using utility methods
     */
    public static void exampleGetReportsSummary() {
        logger.info("=== Example 7: Get Reports Summary ===");

        try {
            // Get summary using utility method
            String summary = S3ReportUtils.getReportsSummary();
            logger.info("Reports Summary:\n{}", summary);

            // Check if reports are up to date
            boolean upToDate = S3ReportUtils.areReportsUpToDate(7);
            logger.info("Reports are up to date (within 7 days): {}", upToDate);

            // Get days since latest report
            int daysSinceLatest = S3ReportUtils.getDaysSinceLatestReport();
            logger.info("Days since latest report: {}", daysSinceLatest);

        } catch (Exception e) {
            logger.error("Failed to get reports summary: {}", e.getMessage());
        }
    }

    /**
     * Example: Using utility methods for common operations
     */
    public static void exampleUsingUtilityMethods() {
        logger.info("=== Example 8: Using Utility Methods ===");

        try {
            // Download latest report using utility method
            Path latestReport = S3ReportUtils.downloadLatestReport("target/downloads/utils");
            logger.info("Latest report downloaded via utility: {}", latestReport);

            // Download current month reports using utility method
            List<Path> currentMonthReports = S3ReportUtils.downloadCurrentMonthReports("target/downloads/utils");
            logger.info("Current month reports downloaded via utility: {}", currentMonthReports.size());

            // Check if today's report exists using utility method
            boolean todayExists = S3ReportUtils.todayReportExists();
            logger.info("Today's report exists (utility method): {}", todayExists);

        } catch (Exception e) {
            logger.error("Failed to use utility methods: {}", e.getMessage());
        }
    }
}