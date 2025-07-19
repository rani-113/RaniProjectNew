package com.automation.tests;

import com.automation.services.WeeklyReportManager;
import com.automation.config.AWSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class S3ReportTest {

    private static final Logger logger = LoggerFactory.getLogger(S3ReportTest.class);
    private WeeklyReportManager reportManager;

    @BeforeClass
    public void setUp() {
        // Create sample config file if it doesn't exist
        AWSConfig.createSampleConfigFile();

        // Initialize the report manager
        // You can either use environment variables or provide credentials directly
        reportManager = new WeeklyReportManager();
    }

    @AfterClass
    public void tearDown() {
        if (reportManager != null) {
            reportManager.close();
        }
    }

    @Test(description = "Test fetching the latest weekly report")
    public void testGetLatestWeeklyReport() {
        try {
            String downloadDir = "target/downloads/latest";
            Path reportPath = reportManager.getLatestWeeklyReport(downloadDir);

            logger.info("Latest weekly report downloaded to: {}", reportPath);

            // Verify the file exists
            assert reportPath.toFile().exists() : "Downloaded report file should exist";
            assert reportPath.toFile().length() > 0 : "Downloaded report file should not be empty";

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching current week's report")
    public void testGetCurrentWeekReport() {
        try {
            String downloadDir = "target/downloads/current-week";
            Path reportPath = reportManager.getCurrentWeekReport(downloadDir);

            logger.info("Current week report downloaded to: {}", reportPath);

            // Verify the file exists
            assert reportPath.toFile().exists() : "Current week report file should exist";

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching previous week's report")
    public void testGetPreviousWeekReport() {
        try {
            String downloadDir = "target/downloads/previous-week";
            Path reportPath = reportManager.getPreviousWeekReport(downloadDir);

            logger.info("Previous week report downloaded to: {}", reportPath);

            // Verify the file exists
            assert reportPath.toFile().exists() : "Previous week report file should exist";

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching last 4 weeks reports")
    public void testGetLastNWeeksReports() {
        try {
            String downloadDir = "target/downloads/last-4-weeks";
            List<Path> reportPaths = reportManager.getLastNWeeksReports(4, downloadDir);

            logger.info("Downloaded {} reports for last 4 weeks", reportPaths.size());

            // Verify files exist
            for (Path reportPath : reportPaths) {
                assert reportPath.toFile().exists() : "Report file should exist: " + reportPath;
            }

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching specific month reports")
    public void testGetMonthlyReports() {
        try {
            int currentYear = LocalDate.now().getYear();
            int currentMonth = LocalDate.now().getMonthValue();

            String downloadDir = "target/downloads/monthly";
            List<Path> reportPaths = reportManager.getMonthlyReports(currentYear, currentMonth, downloadDir);

            logger.info("Downloaded {} reports for {}-{}", reportPaths.size(), currentYear, currentMonth);

            // Verify files exist
            for (Path reportPath : reportPaths) {
                assert reportPath.toFile().exists() : "Monthly report file should exist: " + reportPath;
            }

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching specific quarter reports")
    public void testGetQuarterlyReports() {
        try {
            int currentYear = LocalDate.now().getYear();
            int currentQuarter = (LocalDate.now().getMonthValue() - 1) / 3 + 1;

            String downloadDir = "target/downloads/quarterly";
            List<Path> reportPaths = reportManager.getQuarterlyReports(currentYear, currentQuarter, downloadDir);

            logger.info("Downloaded {} reports for Q{} {}", reportPaths.size(), currentQuarter, currentYear);

            // Verify files exist
            for (Path reportPath : reportPaths) {
                assert reportPath.toFile().exists() : "Quarterly report file should exist: " + reportPath;
            }

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test checking if weekly report exists for a specific date")
    public void testWeeklyReportExists() {
        try {
            LocalDate testDate = LocalDate.now().minusWeeks(1);
            boolean exists = reportManager.weeklyReportExists(testDate);

            logger.info("Weekly report exists for {}: {}", testDate, exists);

            // This is just a check, no assertion needed as it depends on actual data

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test getting report metadata")
    public void testGetReportMetadata() {
        try {
            LocalDate testDate = LocalDate.now().minusWeeks(1);
            var metadata = reportManager.getReportMetadata(testDate);

            if (metadata != null) {
                logger.info("Report metadata for {}: size={}, lastModified={}",
                        testDate, metadata.contentLength(), metadata.lastModified());
            } else {
                logger.info("No report metadata found for {}", testDate);
            }

            // This is just a check, no assertion needed as it depends on actual data

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(description = "Test fetching specific week report")
    public void testGetSpecificWeekReport() {
        try {
            LocalDate weekStart = LocalDate.now().minusWeeks(2).with(java.time.DayOfWeek.MONDAY);
            String downloadDir = "target/downloads/specific-week";

            Path reportPath = reportManager.getSpecificWeekReport(weekStart, downloadDir);

            logger.info("Specific week report downloaded to: {}", reportPath);

            // Verify the file exists
            assert reportPath.toFile().exists() : "Specific week report file should exist";

        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }
}