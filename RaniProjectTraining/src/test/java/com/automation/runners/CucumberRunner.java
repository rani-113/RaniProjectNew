package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "junit:target/cucumber-reports/CucumberTestReport.xml"
    },
    monochrome = true,
    dryRun = false,
    publish = false
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 