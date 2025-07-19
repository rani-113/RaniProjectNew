package com.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AWSConfig {

    private static final Logger logger = LoggerFactory.getLogger(AWSConfig.class);
    private static final String CONFIG_FILE = "aws-config.properties";

    private String accessKeyId;
    private String secretAccessKey;
    private String region;
    private String bucketName;
    private String reportPrefix;

    public AWSConfig() {
        loadConfiguration();
    }

    public AWSConfig(String accessKeyId, String secretAccessKey) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.region = "us-east-2";
        this.bucketName = "ip-report-prod";
        this.reportPrefix = "adv-report/commission/weekly/";
    }

    /**
     * Load AWS configuration from properties file
     */
    private void loadConfiguration() {
        Properties properties = new Properties();

        try {
            // Try to load from classpath first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);

            if (inputStream == null) {
                // Try to load from file system
                inputStream = new FileInputStream(CONFIG_FILE);
            }

            properties.load(inputStream);

            this.accessKeyId = properties.getProperty("aws.accessKeyId");
            this.secretAccessKey = properties.getProperty("aws.secretAccessKey");
            this.region = properties.getProperty("aws.region", "us-east-2");
            this.bucketName = properties.getProperty("aws.bucketName", "ip-report-prod");
            this.reportPrefix = properties.getProperty("aws.reportPrefix", "adv-report/commission/weekly/");

            inputStream.close();
            logger.info("AWS configuration loaded successfully");

        } catch (IOException e) {
            logger.warn("Could not load AWS configuration file: {}. Using environment variables.", e.getMessage());
            loadFromEnvironment();
        }
    }

    /**
     * Load AWS configuration from environment variables
     */
    private void loadFromEnvironment() {
        this.accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
        this.secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        this.region = System.getenv("AWS_REGION");
        this.bucketName = System.getenv("AWS_BUCKET_NAME");
        this.reportPrefix = System.getenv("AWS_REPORT_PREFIX");

        // Set defaults if not found in environment
        if (this.region == null)
            this.region = "us-east-2";
        if (this.bucketName == null)
            this.bucketName = "ip-report-prod";
        if (this.reportPrefix == null)
            this.reportPrefix = "adv-report/commission/weekly/";

        logger.info("AWS configuration loaded from environment variables");
    }

    /**
     * Validate that required configuration is present
     */
    public void validateConfiguration() {
        if (accessKeyId == null || accessKeyId.trim().isEmpty()) {
            throw new IllegalStateException("AWS Access Key ID is required but not configured");
        }

        if (secretAccessKey == null || secretAccessKey.trim().isEmpty()) {
            throw new IllegalStateException("AWS Secret Access Key is required but not configured");
        }

        logger.info("AWS configuration validation passed");
    }

    // Getters
    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public String getRegion() {
        return region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getReportPrefix() {
        return reportPrefix;
    }

    /**
     * Create a sample configuration file
     */
    public static void createSampleConfigFile() {
        String sampleConfig = "# AWS Configuration\n" +
                "# Copy this file to aws-config.properties and update with your credentials\n\n" +
                "# AWS Credentials\n" +
                "aws.accessKeyId=YOUR_ACCESS_KEY_ID\n" +
                "aws.secretAccessKey=YOUR_SECRET_ACCESS_KEY\n\n" +
                "# AWS Region\n" +
                "aws.region=us-east-2\n\n" +
                "# S3 Bucket Configuration\n" +
                "aws.bucketName=ip-report-prod\n" +
                "aws.reportPrefix=adv-report/commission/weekly/\n";

        try {
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("aws-config-sample.properties"),
                    sampleConfig.getBytes());
            logger.info("Sample configuration file created: aws-config-sample.properties");
        } catch (IOException e) {
            logger.error("Failed to create sample configuration file: {}", e.getMessage());
        }
    }
}