package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import Base.Testbase;

public class MercuryPageObject extends Testbase{
	// WebDriver driver =  null;
	 By txtusername_mercury = By.name("userName");
	 By txtpassword_mercury = By.name("password");
	 By button_Search =  By.name("login");
	 
	 
	 
	public MercuryPageObject(WebDriver driver)
	{
		 Testbase.driver = driver;
	}
	public void setUsername_mercury(String text)
	{
		driver.findElement(txtusername_mercury).sendKeys(text);
	}
	public void setPwd_mercury(String text)
	{
		driver.findElement(txtpassword_mercury).sendKeys(text);
	}
	public void clickSignInBtn()
	{
		driver.findElement(button_Search).sendKeys(Keys.RETURN);
	}	
}
