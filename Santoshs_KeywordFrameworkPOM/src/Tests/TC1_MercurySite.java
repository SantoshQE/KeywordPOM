package Tests;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Base.Testbase;
import Pages.MercuryPageObject;

public class TC1_MercurySite extends Testbase {
	WebDriver driver = null;	

	@BeforeTest
	public void beforeTest() throws IOException 
	{
		driver = Testbase.driver;
		log.debug("Selenium webdriver Initialized..");
	}
	@Test
	public void TC1_MercurySite_LoginTest(String TCName,int TDRowNum,String UserName , String Pwd ) 
	{
		System.out.println("TCName :"+TCName + " " + " TD RowNum :"+TDRowNum + " "+UserName + " "+Pwd);
		log.debug("Login Test Started..");
		MercuryPageObject mercuryObj = new MercuryPageObject(driver);
		//Enter username and password
		mercuryObj.setUsername_mercury("mecury");
		mercuryObj.setPwd_mercury("mecury");
		//Click sign in button
		mercuryObj.clickSignInBtn();
		log.debug("Sign in button clicked..");
	}
	@Test
	public void Method2() 
	{
		System.out.println(".... Method2 ....");
		log.debug("Inside method 2...");
	}	
	@AfterTest
	public void teardowntest() 
	{
		driver.close();
		System.out.println("Test Completed Successfully");  
	}
}
