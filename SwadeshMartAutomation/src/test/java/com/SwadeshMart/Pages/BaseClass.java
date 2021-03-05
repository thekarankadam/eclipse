package com.SwadeshMart.Pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import com.SwadeshMart.Utility.BrowserFactory;
import com.SwadeshMart.Utility.ConfigDataProvider;
import com.SwadeshMart.Utility.ExcelDataProvider;
import com.SwadeshMart.Utility.Helper;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class BaseClass {
	public WebDriver driver;
	public ExcelDataProvider excel;
	public ConfigDataProvider config;
	public ExtentReports report;
	public ExtentTest logger;
	
	@BeforeSuite
	public void setupsuite() {
		Reporter.log("Setting up reort and Test getting ready", true);
		excel=new ExcelDataProvider();
		config=new ConfigDataProvider();
		
		ExtentHtmlReporter extent=new ExtentHtmlReporter(new File(System.getProperty("user.dir")+"/ReportFiles/"+Helper.getcurrentDateTime()+"_FreeCRM.html"));
		report=new ExtentReports();
		report.attachReporter(extent);
		Reporter.log("Setting Done", true);
	}
	@Parameters({"browser","urlToBeTested"})
	@BeforeClass
	public void setup(String browser,String url) {
		Reporter.log("Trying to start Browser", true);
		//Passing parameters by using config.properties file
		//driver = BrowserFactory.startApplication(driver, config.getBrowser(), config.getstagingUrl());
		//Passing paramters from maven build
		driver = BrowserFactory.startApplication(driver, browser, url);
	}

	@AfterClass
	public void tearDown() {
		BrowserFactory.quitBrowser(driver);
	}
	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE) {
			Helper.captureScreenShot(driver);
			logger.fail("Test Failed",MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenShot(driver)).build());
		}
		if(result.getStatus()==ITestResult.SUCCESS) {
			Helper.captureScreenShot(driver);
			logger.fail("Test Passed",MediaEntityBuilder.createScreenCaptureFromPath(Helper.captureScreenShot(driver)).build());
		}
		report.flush();
	}

}
