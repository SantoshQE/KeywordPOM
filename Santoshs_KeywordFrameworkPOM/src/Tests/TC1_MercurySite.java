package Tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import Base.Testbase;
import FrameworkDriver.FrameworkDriver_RunTestCases;
import Utilities.UtilFunctions;
import Pages.MercuryPageObject;
import Tests.ExtentReportsClassVersion3;

public class TC1_MercurySite extends Testbase {
	static ExtentTest test;
	static ExtentHtmlReporter htmlReporter;
	WebDriver driver = null;
	static ExtentReportsClassVersion3 Ex = new ExtentReportsClassVersion3();


	@BeforeTest
	public void beforeTest() throws IOException {
		/****************************************************************************************************************/
		
		Ex.htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/STMExtentReport.html");
		Ex.extent = new ExtentReports();
		Ex.extent.attachReporter(Ex.htmlReporter);
		Ex.extent.setSystemInfo("Host Name", "SanntoshPC");
		Ex.extent.setSystemInfo("Environment", "Automation Testing");
		Ex.extent.setSystemInfo("User Name", "Santosh");
		Ex.htmlReporter.config().setDocumentTitle("KeywordFramework");
		Ex.htmlReporter.config().setReportName("TC_Report");
		Ex.htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		Ex.htmlReporter.config().setTheme(Theme.STANDARD);
		
		/****************************************************************************************************************/

		driver = Testbase.driver;
		// Testbase tBase = new Testbase();
		// tBase.initializeDriver();
		log.debug("Selenium webdriver Initialized..");
		if (driver == null) {
			log.debug("Browser Selected == Chrome");
			Reporter.log("");
			String ProjectPath = System.getProperty("user.dir");
			System.setProperty("webdriver.chrome.driver",
					ProjectPath + "\\resources\\executables\\Chromedriver\\chromedriver.exe");

			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(Config.getProperty("testsiteUrl"));
			log.debug("URL loaded");
		}
	}

	@Test
	public void TC1_MercurySite_LoginTest(String TCName, int TDRowNum, String UserName, String Pwd)
			throws SecurityException, NoSuchMethodException {
	
		System.out.println("TCName :" + TCName + " " + " Test Data RowNum :" + TDRowNum + " " + UserName + " " + Pwd);
		log.debug("Login Test Started..");
		Reporter.log("**** TC1_MercurySite_LoginTest Test Started ****");
		// PULL DATA FROM TEST DATA SHEET USING UTIL FUNCTIONS --- START
		FrameworkDriver_RunTestCases fRun = new FrameworkDriver_RunTestCases();
		UtilFunctions uData = new UtilFunctions(fRun.TestDataSheet);
		XSSFCell userName = uData.getTestData(TCName, TDRowNum, "Username");
		XSSFCell password = uData.getTestData(TCName, TDRowNum, "password");
		// PULL DATA FROM TEST DATA SHEET USING UTIL FUNCTIONS --- END

		MercuryPageObject mercuryObj = new MercuryPageObject(driver);
		mercuryObj.setUsername_mercury(userName.toString());
		mercuryObj.setPwd_mercury(password.toString());
		// Click sign in button
		mercuryObj.clickSignInBtn();
		log.debug("Sign in button clicked..");

		test = Ex.extent.createTest("TC1_MercurySite_LoginTest");
		Assert.assertTrue(true);
		//Ex.logger.log(Status.PASS, MarkupHelper.createLabel("Test Case Passed is passTest", ExtentColor.GREEN));
		//Ex.extent.flush();
	}

	@Test
	public void Browserback() {
		System.out.println(".... Browserback ....");
		log.debug("Inside Browserback ...");
		driver.navigate().back();
		test = Ex.extent.createTest("Browserback");
		Assert.assertTrue(true);
		
	}

	@Test
	public void CloseBrowser() {
		System.out.println(".... CloseBrowser ....");
		log.debug("Inside CloseBrowser ...");
		test = Ex.extent.createTest("CloseBrowser");
		Assert.assertTrue(true);
		Ex.extent.flush();
		driver.close();
		driver = null;
		Testbase.driver = null;
	}

	@AfterTest
	public void teardowntest() {
		driver.close();
		System.out.println("Test Completed Successfully");
	}

	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			// logger.log(Status.FAIL, "Test Case Failed is "+result.getName());
			// MarkupHelper is used to display the output in different colors
			Ex.logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File Dest = new File("C:\\Users\\Santosh\\git\\KeywordwithPOM\\Santoshs_KeywordFrameworkPOM\\test-output\\" + System.currentTimeMillis()+ ".png");
			String errflpath = Dest.getAbsolutePath();
			FileUtils.copyFile(scrFile, Dest);

			Ex.logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SKIP) {
			// logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			Ex.logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
	}
	@AfterTest
	public void endReport(){
		Ex.extent.flush();
    }


}
