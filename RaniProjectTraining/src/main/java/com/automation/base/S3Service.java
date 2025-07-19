package com.automation.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private final S3Client s3Client;
    private final String bucketName = "ip-report-prod";
    private final String region = "us-east-2";
    private final String prefix = "adv-report/commission/weekly/";

    public S3Service(String accessKeyId, String secretAccessKey) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
        logger.info("S3 client initialized for bucket: {}", bucketName);
    }

    /**
     * Get all weekly reports from the S3 bucket
     * 
     * @return List of S3 object keys for weekly reports
     */
    public List<String> getAllWeeklyReports() {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            List<String> reportKeys = response.contents().stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());

            logger.info("Found {} weekly reports in S3 bucket", reportKeys.size());
            return reportKeys;
        } catch (S3Exception e) {
            logger.error("Failed to list weekly reports: {}", e.getMessage());
            throw new RuntimeException("Failed to list weekly reports", e);
        }
    }

    /**
     * Get weekly reports for a specific date range
     * 
     * @param startDate Start date for the range
     * @param endDate   End date for the range
     * @return List of S3 object keys for reports in the date range
     */
    public List<String> getWeeklyReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            List<String> allReports = getAllWeeklyReports();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            List<String> filteredReports = allReports.stream()
                    .filter(key -> {
                        // Extract date from the key and check if it's in range
                        String fileName = key.substring(key.lastIndexOf('/') + 1);
                        if (fileName.contains(".")) {
                            String datePart = fileName.split("\\.")[0];
                            try {
                                LocalDate reportDate = LocalDate.parse(datePart, formatter);
                                return !reportDate.isBefore(startDate) && !reportDate.isAfter(endDate);
                            } catch (Exception e) {
                                logger.warn("Could not parse date from filename: {}", fileName);
                                return false;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            logger.info("Found {} reports in date range {} to {}",
                    filteredReports.size(), startDate, endDate);
            return filteredReports;
        } catch (Exception e) {
            logger.error("Failed to get weekly reports by date range: {}", e.getMessage());
            throw new RuntimeException("Failed to get weekly reports by date range", e);
        }
    }

    /**
     * Get the latest weekly report
     * 
     * @return S3 object key of the latest report
     */
    public String getLatestWeeklyReport() {
        try {
            List<String> allReports = getAllWeeklyReports();
            if (allReports.isEmpty()) {
                logger.warn("No weekly reports found in S3 bucket");
                return null;
            }

            // Sort by key (assuming keys contain dates) and get the latest
            String latestReport = allReports.stream()
                    .sorted()
                    .reduce((first, second) -> second)
                    .orElse(null);

            logger.info("Latest weekly report: {}", latestReport);
            return latestReport;
        } catch (Exception e) {
            logger.error("Failed to get latest weekly report: {}", e.getMessage());
            throw new RuntimeException("Failed to get latest weekly report", e);
        }
    }

    /**
     * Download a specific report from S3
     * 
     * @param objectKey S3 object key of the report to download
     * @param localPath Local path where to save the file
     * @return Path to the downloaded file
     */
    public Path downloadReport(String objectKey, String localPath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            Path downloadPath = Paths.get(localPath);
            Files.createDirectories(downloadPath.getParent());

            s3Client.getObject(getObjectRequest, downloadPath);

            logger.info("Downloaded report {} to {}", objectKey, downloadPath);
            return downloadPath;
        } catch (S3Exception | IOException e) {
            logger.error("Failed to download report {}: {}", objectKey, e.getMessage());
            throw new RuntimeException("Failed to download report", e);
        }
    }

    /**
     * Download the latest weekly report
     * 
     * @param localPath Local path where to save the file
     * @return Path to the downloaded file
     */
    public Path downloadLatestWeeklyReport(String localPath) {
        String latestReport = getLatestWeeklyReport();
        if (latestReport == null) {
            throw new RuntimeException("No weekly reports found to download");
        }
        return downloadReport(latestReport, localPath);
    }

    /**
     * Download all weekly reports from a date range
     * 
     * @param startDate      Start date for the range
     * @param endDate        End date for the range
     * @param localDirectory Local directory where to save the files
     * @return List of paths to downloaded files
     */
    public List<Path> downloadWeeklyReportsByDateRange(LocalDate startDate, LocalDate endDate, String localDirectory) {
        List<String> reportKeys = getWeeklyReportsByDateRange(startDate, endDate);
        List<Path> downloadedPaths = new ArrayList<>();

        for (String reportKey : reportKeys) {
            String fileName = reportKey.substring(reportKey.lastIndexOf('/') + 1);
            String localPath = localDirectory + File.separator + fileName;
            Path downloadedPath = downloadReport(reportKey, localPath);
            downloadedPaths.add(downloadedPath);
        }

        logger.info("Downloaded {} reports to directory: {}", downloadedPaths.size(), localDirectory);
        return downloadedPaths;
    }

    /**
     * Get report metadata (size, last modified, etc.)
     * 
     * @param objectKey S3 object key of the report
     * @return HeadObjectResponse containing metadata
     */
    public HeadObjectResponse getReportMetadata(String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            HeadObjectResponse response = s3Client.headObject(headObjectRequest);
            logger.info("Retrieved metadata for report: {}", objectKey);
            return response;
        } catch (S3Exception e) {
            logger.error("Failed to get metadata for report {}: {}", objectKey, e.getMessage());
            throw new RuntimeException("Failed to get report metadata", e);
        }
    }

    /**
     * Check if a report exists in S3
     * 
     * @param objectKey S3 object key to check
     * @return true if report exists, false otherwise
     */
    public boolean reportExists(String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            logger.error("Error checking if report exists {}: {}", objectKey, e.getMessage());
            return false;
        }
    }

    /**
     * Close the S3 client
     */
    public void close() {
        if (s3Client != null) {
            s3Client.close();
            logger.info("S3 client closed");
        }
    }
}