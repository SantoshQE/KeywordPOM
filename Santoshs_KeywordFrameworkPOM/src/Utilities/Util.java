package Utilities;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import Base.Testbase;
import FrameworkDriver.FrameworkDriver_RunTestCases;

public class Util extends FrameworkDriver_RunTestCases 
{	
	
	public Util(WebDriver driver)
	{
		 Testbase.driver = driver;
	}
	
	@Test
	public XSSFCell getTestData(String TCName,int TDRowNum, String ColHeader) 
	{
		
		int rowCnt = TestDataSheet.getLastRowNum();
		int colCnt = TestDataSheet.getRow(0).getLastCellNum();
		int i,j,colIndex;
		colIndex = 0;
		XSSFCell testDataVal = null;
		for(i=0;i<colCnt;i++)
		{
			if(TestDataSheet.getRow(0).getCell(i).toString().equalsIgnoreCase(ColHeader))
			{
				colIndex = i;
				break;
			}  
		}
		if(colIndex ==0)
		{
			System.out.println("No such column exists...");
		}
		else
		{
			testDataVal = TestDataSheet.getRow(0).getCell(colIndex);

		}
		return  testDataVal;  
	}
}
