package FrameworkDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class TestReadExcel {
  @Test
  public void ReadExcel() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException 
  {
	  System.out.println("this is a test");
	  String KeywordFile = System.getProperty("user.dir");
		KeywordFile = KeywordFile.replace("\\", "\\\\");
		File source = new File(KeywordFile+"\\Resources\\FrameworkDriverExcel\\FrameworkDriverExcel.xlsx");
		FileInputStream input= new FileInputStream(source);
		XSSFWorkbook wb= new XSSFWorkbook(input);
		XSSFSheet TestCaseSheet = wb.getSheet("TestCases");   //TestCases Sheet
		XSSFSheet TestDataSheet = wb.getSheet("TestData");   //TestData Sheet
		XSSFSheet GlobalConfigSheet = wb.getSheet("GlobalConfig"); //GlobalConfig Sheet
		//System.out.println(GlobalConfigSheet.getSheetName());
		String testdatasheet = TestDataSheet.getSheetName();
		System.out.println(testdatasheet);
		Reporter.log("========== Second sheetname is " +testdatasheet+"===========");
		int TestCaseRowCnt = TestCaseSheet.getLastRowNum();  //TestCase Sheet RowCount
		int TestDataRowCnt = TestDataSheet.getLastRowNum();  //TestData Sheet RowCount
		//TestCaseRowCnt = TestCaseRowCnt + 1;
		//TestDataRowCnt = TestDataRowCnt + 1;
		for(int i = 1;i<=TestCaseRowCnt;i++)  //This is for ROW iterations
		{
			int colcnt  = TestCaseSheet.getRow(i).getLastCellNum();
			colcnt = colcnt + 1;
			for(int j=1;j<colcnt;j++)  //This is for COLUMN iterations
			{
				XSSFCell ExecuteFlag  = TestCaseSheet.getRow(i).getCell(1);
				XSSFCell TestCaseName  = TestCaseSheet.getRow(i).getCell(0);
				if(ExecuteFlag.toString().isEmpty() || ExecuteFlag.toString().equals("N") )
				{
					System.out.println("Execute Flag for the Test Case "+ TestCaseName +" is blank OR Set to N");
					break;
				}else
				{
					System.out.println("Execute Flag for the Test Case is  "+ExecuteFlag);
				}
				String CellVal = ExecuteFlag.getStringCellValue();
				if( CellVal.equalsIgnoreCase("Y")) 
					{
					XSSFCell CellValTC  = TestCaseSheet.getRow(i).getCell(0);
					String CellValTCSheet = CellValTC.getStringCellValue();
						if(TestCaseSheet.getRow(i).getCell(j)== null ||TestCaseSheet.getRow(i).getCell(j).toString().equalsIgnoreCase("N"))
						{
							break;
						}
						else
						{
						if( TestCaseSheet.getRow(i).getCell(j) != null)
							{
								String getKeyword = TestCaseSheet.getRow(i).getCell(j).toString();
								System.out.println(getKeyword.toString());
								if(getKeyword.contains("|") == false) 
								{
									//DO Nothing
								}
								else 
								{
									Reporter.log("========== The flag on the row  " +i+" is set to Y for Execution ===========");
									String KeywordName = getKeyword;
									//CALL THE KEYWORD ONE BY ONE
									String[] parts = KeywordName.split("\\|");
									String strClassName = parts[0];  
									String strMethodName = parts[1]; 	  					 		
									//Reflection API
									String PkgName = "Tests";
									String KeywordNameClassName = PkgName+"."+strClassName;
									Class<?> KeywordNameClass = Class.forName(KeywordNameClassName); 
									Object objKeywordName = KeywordNameClass.newInstance();
									int TotalMethods   = KeywordNameClass.getDeclaredMethods().length;
									System.out.println("Total methods in class are : " +TotalMethods);
								
									
	//THIS LOOP IS TO FIND THE @BeforeTest ANNOTATION METHOD TO BE EXECUTED FIRST
	for(int annot=0;annot<TotalMethods;annot++)
	{
		String mthname   = KeywordNameClass.getDeclaredMethods()[annot].getName();
		Method mth1 = objKeywordName.getClass().getMethod(mthname);
		Annotation[] annotname = mth1.getDeclaredAnnotations();
		System.out.println(annotname);
		System.out.println(annotname[0].annotationType().getCanonicalName());

		if(annotname[0].annotationType().getSimpleName().toString().contains("BeforeTest"))  
			//contains("BeforeTest"))
		{
			System.out.println(annotname.toString());
			mth1.invoke(objKeywordName);
			break;	
		}
	}   							
//THIS LOOP IS TO FIND THE EXACT METHOD NAME IN THE CLASS AND EXECUTE THE @Test BLOCK  
									for(int mn=0;mn<=TotalMethods;mn++)
									{
										String methodName   = KeywordNameClass.getDeclaredMethods()[mn].getName();
										if(methodName.equals("") || methodName.equals(null) )
										{
											break;
										}
										else {
											Method setNameMethod = objKeywordName.getClass().getMethod(methodName);
											String keywordname = methodName.toString();
											if(strMethodName.equals(keywordname))
											{
												System.out.println(setNameMethod.toString());		
												setNameMethod.invoke(objKeywordName);     // EXECUTE THE METHOD
												break;
											}
										} 
									}
	//THIS LOOP IS TO FIND THE @AfterTest ANNOTATION METHOD TO BE EXECUTED LAST
	for(int annot=0;annot<TotalMethods;annot++)
	{
		String mthname   = KeywordNameClass.getDeclaredMethods()[annot].getName();
		Method mth1 = objKeywordName.getClass().getMethod(mthname);
		Annotation[] annotname = mth1.getDeclaredAnnotations();
		System.out.println(annotname);
		System.out.println(annotname[0].annotationType().getCanonicalName());

		if(annotname[0].annotationType().getSimpleName().toString().contains("AfterTest"))  
			//contains("BeforeTest"))
		{
			System.out.println(annotname.toString());
			mth1.invoke(objKeywordName);
			break;	
		}
	}
			if((TestCaseSheet.getRow(i).getCell(j)== null))
			{
				System.out.println("No more keywords to execute");
				break;
			}
			else
			{
				getKeyword  = TestCaseSheet.getRow(i).getCell(j).toString();
			}
		
								}
							}
						
						}

					}
			}
			
		}
}
}

