package Tests;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Base.Testbase;
import FrameworkDriver.FrameworkDriver_RunTestCases;
import Utilities.UtilFunctions;
import Pages.MercuryPageObject;

public class TC1_MercurySite extends Testbase {
	WebDriver driver = null;	

	@BeforeTest
	public void beforeTest() throws IOException 
	{
		 driver = Testbase.driver;    
		//Testbase tBase = new Testbase();
		//tBase.initializeDriver();
		log.debug("Selenium webdriver Initialized..");
		if(driver == null)
		{
			log.debug("Browser Selected == Chrome");
			Reporter.log ("");
			String  ProjectPath = System.getProperty("user.dir");
			System.setProperty("webdriver.chrome.driver",ProjectPath+"\\resources\\executables\\Chromedriver\\chromedriver.exe");

			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(Config.getProperty("testsiteUrl"));
			log.debug("URL loaded");
		}
	}
	@Test
	public  void TC1_MercurySite_LoginTest(String TCName,int TDRowNum,String UserName , String Pwd ) throws SecurityException, NoSuchMethodException 
	{
		System.out.println("TCName :"+TCName + " " + " Test Data RowNum :"+TDRowNum + " "+UserName + " "+Pwd);
		log.debug("Login Test Started..");
		Reporter.log ("**** TC1_MercurySite_LoginTest Test Started ****");
		// PULL DATA FROM TEST DATA SHEET USING UTIL FUNCTIONS  --- START
		FrameworkDriver_RunTestCases fRun = new FrameworkDriver_RunTestCases();		
		UtilFunctions uData = new UtilFunctions(fRun.TestDataSheet);		
		XSSFCell userName = uData.getTestData(TCName, TDRowNum, "Username");
		XSSFCell password = uData.getTestData(TCName, TDRowNum, "password");
		// PULL DATA FROM TEST DATA SHEET USING UTIL FUNCTIONS  --- END
		
		MercuryPageObject mercuryObj = new MercuryPageObject(driver);
		mercuryObj.setUsername_mercury(userName.toString());
		mercuryObj.setPwd_mercury(password.toString());
		//Click sign in button
		mercuryObj.clickSignInBtn();
		log.debug("Sign in button clicked..");
	}
	@Test
	public void Browserback() 
	{
		System.out.println(".... Browserback ....");
		log.debug("Inside Browserback ...");
		driver.navigate().back();

	}	
	@Test
	public void CloseBrowser() 
	{
		System.out.println(".... CloseBrowser ....");
		log.debug("Inside CloseBrowser ...");
		driver.close();
		driver = null;
		Testbase.driver=null;
	}	
	@AfterTest
	public void teardowntest() 
	{
		driver.close();
		System.out.println("Test Completed Successfully");  
	}
}
