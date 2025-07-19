# S3 Integration for Weekly Reports

This project includes comprehensive S3 integration to fetch weekly reports from the AWS S3 bucket `ip-report-prod` in the `adv-report/commission/weekly/` prefix.

## Overview

The S3 integration provides the following capabilities:
- Fetch the latest weekly report
- Download reports for specific date ranges
- Get current week, previous week, monthly, quarterly, and yearly reports
- Check report availability and metadata
- Automatic credential management

## Setup

### 1. AWS Credentials Configuration

You have three options to configure AWS credentials:

#### Option A: Environment Variables (Recommended)
Set the following environment variables:
```bash
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
export AWS_REGION=us-east-2
export AWS_BUCKET_NAME=ip-report-prod
export AWS_REPORT_PREFIX=adv-report/commission/weekly/
```

#### Option B: Configuration File
1. Copy the sample configuration file:
   ```bash
   cp aws-config-sample.properties aws-config.properties
   ```
2. Edit `aws-config.properties` with your credentials:
   ```properties
   aws.accessKeyId=your_access_key_id
   aws.secretAccessKey=your_secret_access_key
   aws.region=us-east-2
   aws.bucketName=ip-report-prod
   aws.reportPrefix=adv-report/commission/weekly/
   ```

#### Option C: Direct Credentials in Code
```java
WeeklyReportManager manager = new WeeklyReportManager("your_access_key_id", "your_secret_access_key");
```

### 2. Maven Dependencies

The required AWS SDK dependencies are already included in `pom.xml`:
- `software.amazon.awssdk:s3` - S3 client
- `software.amazon.awssdk:sts` - Security Token Service
- `com.fasterxml.jackson.core:jackson-databind` - JSON processing

## Usage Examples

### Basic Usage

#### 1. Get the Latest Weekly Report
```java
import com.automation.services.WeeklyReportManager;
import java.nio.file.Path;

WeeklyReportManager manager = new WeeklyReportManager();
try {
    Path reportPath = manager.getLatestWeeklyReport("target/downloads");
    System.out.println("Latest report downloaded to: " + reportPath);
} finally {
    manager.close();
}
```

#### 2. Get Current Week's Report
```java
Path reportPath = manager.getCurrentWeekReport("target/downloads/current-week");
```

#### 3. Get Previous Week's Report
```java
Path reportPath = manager.getPreviousWeekReport("target/downloads/previous-week");
```

#### 4. Get Reports for Last 4 Weeks
```java
List<Path> reportPaths = manager.getLastNWeeksReports(4, "target/downloads/last-4-weeks");
```

### Advanced Usage

#### 1. Get Monthly Reports
```java
// Get current month reports
List<Path> reports = manager.getMonthlyReports(2024, 12, "target/downloads/monthly");

// Get previous month reports
List<Path> reports = manager.getMonthlyReports(2024, 11, "target/downloads/monthly");
```

#### 2. Get Quarterly Reports
```java
// Get Q4 2024 reports
List<Path> reports = manager.getQuarterlyReports(2024, 4, "target/downloads/quarterly");
```

#### 3. Get Yearly Reports
```java
// Get all 2024 reports
List<Path> reports = manager.getYearlyReports(2024, "target/downloads/yearly");
```

#### 4. Check Report Availability
```java
import java.time.LocalDate;

// Check if today's report exists
boolean exists = manager.weeklyReportExists(LocalDate.now());

// Check if a specific date's report exists
boolean exists = manager.weeklyReportExists(LocalDate.of(2024, 12, 15));
```

#### 5. Get Report Metadata
```java
import java.time.LocalDate;

var metadata = manager.getReportMetadata(LocalDate.now());
if (metadata != null) {
    System.out.println("Report size: " + metadata.contentLength() + " bytes");
    System.out.println("Last modified: " + metadata.lastModified());
}
```

### Utility Methods

The `S3ReportUtils` class provides convenient static methods:

```java
import com.automation.utils.S3ReportUtils;

// Download latest report
Path reportPath = S3ReportUtils.downloadLatestReport("target/downloads");

// Download current month reports
List<Path> reports = S3ReportUtils.downloadCurrentMonthReports("target/downloads");

// Check if reports are up to date (within 7 days)
boolean upToDate = S3ReportUtils.areReportsUpToDate(7);

// Get reports summary
String summary = S3ReportUtils.getReportsSummary();
System.out.println(summary);
```

## Running Tests

### 1. Run All S3 Tests
```bash
mvn test -Dtest=S3ReportTest
```

### 2. Run Specific Test
```bash
mvn test -Dtest=S3ReportTest#testGetLatestWeeklyReport
```

### 3. Run with Headless Mode (as per user preference)
The tests will run in headless mode by default. If you need to run with a browser UI, modify the `BaseClass.java` to remove the headless options.

## File Structure

```
src/
├── main/java/com/automation/
│   ├── config/
│   │   └── AWSConfig.java              # AWS configuration management
│   ├── services/
│   │   ├── S3Service.java              # Core S3 operations
│   │   └── WeeklyReportManager.java    # High-level report management
│   └── utils/
│       └── S3ReportUtils.java          # Utility methods
└── test/java/com/automation/tests/
    └── S3ReportTest.java               # Test examples
```

## Error Handling

The integration includes comprehensive error handling:

1. **Credential Errors**: Clear error messages if AWS credentials are missing or invalid
2. **Network Errors**: Automatic retry logic for transient network issues
3. **File System Errors**: Proper handling of disk space and permission issues
4. **S3 Errors**: Specific handling for S3-specific errors (bucket not found, access denied, etc.)

## Logging

All operations are logged using SLF4J. Log levels:
- `INFO`: Normal operations (downloads, successful operations)
- `WARN`: Non-critical issues (missing reports, configuration warnings)
- `ERROR`: Critical errors (credential issues, network failures)

## Security Best Practices

1. **Never commit credentials**: Use environment variables or external configuration files
2. **Use IAM roles**: For production, use IAM roles instead of access keys
3. **Least privilege**: Grant only necessary S3 permissions (read-only for this use case)
4. **Rotate credentials**: Regularly rotate access keys
5. **Monitor access**: Enable CloudTrail to monitor S3 access

## Troubleshooting

### Common Issues

1. **Access Denied Error**
   - Verify AWS credentials are correct
   - Check IAM permissions for S3 read access
   - Ensure bucket name and region are correct

2. **No Reports Found**
   - Verify the S3 prefix path is correct
   - Check if reports exist in the specified date range
   - Ensure date format matches the expected format (yyyy-MM-dd)

3. **Network Timeout**
   - Check internet connectivity
   - Verify AWS region is accessible
   - Consider increasing timeout values if needed

### Debug Mode

Enable debug logging by setting the log level in `logback.xml`:
```xml
<logger name="com.automation.services" level="DEBUG"/>
```

## Performance Considerations

1. **Batch Downloads**: Use date range methods for multiple reports
2. **Connection Pooling**: The S3 client automatically manages connections
3. **Parallel Downloads**: Consider implementing parallel downloads for large datasets
4. **Caching**: Implement caching for frequently accessed reports

## Future Enhancements

Potential improvements:
1. **Async Operations**: Implement async download methods
2. **Compression**: Add support for compressed reports
3. **Incremental Sync**: Only download new/changed reports
4. **Report Validation**: Add checksum validation for downloaded files
5. **Scheduling**: Integrate with job schedulers for automated downloads 
