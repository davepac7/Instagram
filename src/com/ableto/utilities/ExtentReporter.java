package com.ableto.utilities;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import com.ableto.base.BaseTest;


public class ExtentReporter extends BaseTest implements ITestListener {


	public static String platform;
	public static String fileName;
	public static String onlyFileName;
	public static String report;
	public static String ExcelFileName;
	private static ExtentReports extent;
	private static ExtentHtmlReporter htmlReport;
//	private static ExtentTest test;
	private static ExtentTest childTest;

	@Override
	public void onStart(ITestContext context) {
		String reportFolder = "ExtentReports";
		File reportFile = new File(reportFolder);
		try {
			if (reportFile.exists()) {
				FileUtils.deleteDirectory(reportFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String filePath = System.getProperty("user.dir") + "/ExtentReports" +  "/"
				+ context.getName() + "_" + getDate() + ".html";
		htmlReport = (new ExtentHtmlReporter(filePath));
		htmlReport.loadConfig(((System.getProperty("user.dir") + "/ReportsConfig.xml")));
		extent = (new ExtentReports());
		extent.attachReporter(htmlReport);
		report = context.getName();
		System.out.println(filePath);
	}

	@Override
	public void onTestStart(ITestResult result) {
		childTest = (extent.createTest(result.getName()));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		childTest.log(Status.PASS, result.getName() + " is PASSED");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		screencapture();
		childTest.log(Status.FAIL, result.getName() + " is FAILED");
		childTest.log(Status.INFO, "StackTrace Result: " + result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		childTest.log(Status.SKIP, result.getName() + " is SKIPPED");
	}

	public void HeaderChildNode(String header) {
		childTest = (childTest.createNode(header));
	}

	public void extentLogger(String stepName, String details) {
		childTest.log(Status.INFO, details);
	}

	public void extentLoggerFail(String stepName, String details) {
		childTest.log(Status.FAIL, details);
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult context) {
	}

	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String name = dateFormat.format(date).toString().replaceFirst(" ", "_").replaceAll("/", "_").replaceAll(":",
				"_");
		return name;
	}

	public void screencapture() {
		try {
			driver = DriverFactory.getInstance().getDriver();
			 String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			childTest.addScreenCaptureFromBase64String(screenshotBase64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
