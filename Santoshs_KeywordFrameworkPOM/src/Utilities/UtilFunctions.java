package Utilities;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.WebDriver;


import Base.Testbase;
import FrameworkDriver.FrameworkDriver_RunTestCases;

public class UtilFunctions extends FrameworkDriver_RunTestCases 
{	
	XSSFCell testDataVal = null;
		
	public UtilFunctions( XSSFSheet TestDataSheet)
	{
		this.TestDataSheet = TestDataSheet;
	}

	public XSSFCell getTestData(String TCName,int TDRowNum, String ColHeader) 
	{	
		int rowCnt = TestDataSheet.getLastRowNum();
		int colCnt = TestDataSheet.getRow(0).getLastCellNum();
		int i,j,colIndex;
		colIndex = 0;
		
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
			testDataVal = TestDataSheet.getRow(TDRowNum).getCell(colIndex);
		}
		return  testDataVal;  
	}
}
