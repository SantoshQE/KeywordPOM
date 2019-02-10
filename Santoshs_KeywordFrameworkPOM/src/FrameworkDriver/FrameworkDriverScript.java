package FrameworkDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class FrameworkDriverScript 
{		
	static XSSFSheet  TestDataSheet; 
	static int TestDataRowCnt;
	static ArrayList ParamList = new ArrayList();
	static StringBuilder sb;
	@SuppressWarnings("null")
	@Test
	public  static  void ReadExcel() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException 
	{	
		String KeywordFile = System.getProperty("user.dir");
		File source = new File(KeywordFile+"\\Resources\\FrameworkDriverExcel\\FrameworkDriverExcel.xlsx");
		FileInputStream input= new FileInputStream(source);
		XSSFWorkbook wb= new XSSFWorkbook(input); 
		XSSFSheet TestCaseSheet = wb.getSheet("TestCases");   //TestCases Sheet
		XSSFSheet  TestDataSheet = wb.getSheet("TestData");   //TestData Sheet
		XSSFSheet GlobalConfigSheet = wb.getSheet("GlobalConfig"); //GlobalConfig Sheet 
		String testdatasheet = TestDataSheet.getSheetName();
		System.out.println(testdatasheet);
		int TestCaseRowCnt = TestCaseSheet.getLastRowNum();  //TestCase Sheet RowCount
		TestDataRowCnt = TestDataSheet.getLastRowNum();  //TestData Sheet RowCount
		for(int i = 1;i<=TestCaseRowCnt;i++)  //This is for ROW iterations
		{
			int colcnt  = TestCaseSheet.getRow(i).getLastCellNum();
			colcnt = colcnt + 1;
			for(int j=1;j<colcnt;j++)  //This is for COLUMN iterations
			{
				XSSFCell ExecuteFlag  = TestCaseSheet.getRow(i).getCell(1);
				XSSFCell TestCaseName  = TestCaseSheet.getRow(i).getCell(0);
				ArrayList<String> TDIteration = GetTestDataIterations(TestCaseName,TestDataSheet);  // GET THE TEST DATA ITERATIONS AND ROW NUMBERS
				System.out.println("Test Data Iteration is :    "+TDIteration);
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
					//FETCH THE TEST DATA ROW USING ::TDIteration:: ARRAYLIST 
					for(int TDnum = 0;TDnum<TDIteration.size();TDnum++)
					{
						System.out.println(TDIteration.get(TDnum));
						int PipeIndex  = TDIteration.get(TDnum).toString().indexOf("|")+1;
						String TDRowNum = TDIteration.get(TDnum).toString().substring(PipeIndex, TDIteration.get(TDnum).toString().length());
						System.out.println(TDRowNum);


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
									//------------------ DO NOTHING -----------------
								}
								else 
								{
									Reporter.log(" The flag on the row  " +i+" is set to Y for Execution for Test Case : " +CellValTC);
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
									//@BEFORETEST BLOCK :				//THIS LOOP IS TO FIND THE @BeforeTest ANNOTATION METHOD TO BE EXECUTED FIRST
									for(int annot=0;annot<TotalMethods;annot++)
									{
										String mthname   = KeywordNameClass.getDeclaredMethods()[annot].getName();
										Method mth1 = objKeywordName.getClass().getMethod(mthname,KeywordNameClass.getDeclaredMethods()[annot].getParameterTypes());
										Annotation[] annotname = mth1.getDeclaredAnnotations();
										System.out.println(annotname);
										System.out.println(annotname[0].annotationType().getCanonicalName());
										if(annotname[0].annotationType().getSimpleName().toString().equalsIgnoreCase("BeforeTest"))  
											//contains("BeforeTest"))
										{
											System.out.println(annotname.toString());
											mth1.invoke(objKeywordName);
											break;	
										}
									}   							
									//@TEST BLOCK :						//THIS LOOP IS TO FIND THE EXACT METHOD NAME IN THE CLASS AND EXECUTE THE @Test BLOCK  

									for(int mn=0;mn<=TotalMethods;mn++)
									{
										//code to get parameters
										String strMethodParam ,strParamFinal = null ;
										Parameter[] param = KeywordNameClass.getDeclaredMethods()[mn].getParameters();
										for(int pr=0;pr<param.length;pr++)
										{	
											System.out.println("-- " + param[pr].getName().getClass() + " --");
											String strMNParam = param[pr].getParameterizedType().getTypeName().toString();
											System.out.println(strMNParam);
											//Create method parameter string
											int LastIndex  = GetLastIndexofChar(strMNParam,'.')+1;
											strMethodParam = strMNParam.substring(LastIndex, strMNParam.length()).toString();
											//ParamList.add(strMethodParam  +" Param_"+pr + " ");
											ParamList.add(strMethodParam+".class");
										}

										System.out.println(ParamList.toString());
										//Prepare a string using arraylist
										String MethodArgs = ParamList.toString();
										if((MethodArgs.contains("[") && MethodArgs.contains("]")) && MethodArgs !="[]" && MethodArgs != "")
										{
											String MethodArgsFinal1 = MethodArgs.replace("[", "");
											String MethodArgsFinal = MethodArgsFinal1.replace("]", "");
											System.out.println(MethodArgsFinal);										
										}
										String methodName   = KeywordNameClass.getDeclaredMethods()[mn].getName();
										if(methodName.equals("") || methodName.equals(null) )
										{
											break;
										}
										else {	
											if(KeywordNameClass.getDeclaredMethods()[mn].getParameterTypes().length > 0 )
											{
												//Code to prepare a string for parameter list-->THIS BLOCK WILL TAKE CARE OF METHOD WITH PARAMETERS DYNAMICALLY
												System.out.println("Inside method with parameters");
												//Method setNameMethod = objKeywordName.getClass().getMethod(methodName.toString(),List.class);
												//Method setNameMethod = objKeywordName.getClass().getMethod(methodName.toString(),String.class ,int.class ,String.class ,String.class  );
												Method setNameMethod = objKeywordName.getClass().getMethod(methodName.toString(),KeywordNameClass.getDeclaredMethods()[mn].getParameterTypes());
												String keywordname = methodName.toString();
												if(strMethodName.equals(keywordname))
												{	
													System.out.println(setNameMethod.toString());		
													// setNameMethod.invoke(objKeywordName,"TC1",1,"TS","dsds" );     // EXECUTE THE METHOD
													
												
													int paramCount = KeywordNameClass.getDeclaredMethods()[mn].getGenericParameterTypes().length;
													sb = new StringBuilder(32);
													for(int ptn=0;ptn<paramCount;ptn++)
													{
														System.out.println(KeywordNameClass.getDeclaredMethods()[mn].getGenericParameterTypes()[ptn].toString());
														sb.append(KeywordNameClass.getDeclaredMethods()[mn].getParameterTypes()[ptn].toString()).append(" , ");
														
													}
													System.out.println(sb.toString());
													String finalParamList = sb.toString().replace("class", "");
													String[] ss  = finalParamList.split(",");
													ArrayList paramlist = new ArrayList<>();
													//paramlist.add(e)
													
													
													
													
													setNameMethod.invoke(objKeywordName,"TC112",12,"TS22","dsds2");  
													break;
												}																
											}
											else
											{
												Method setNameMethod = objKeywordName.getClass().getMethod(methodName,null);
												String keywordname = methodName.toString();
												if(strMethodName.equals(keywordname))
												{
													System.out.println(setNameMethod.toString());		
													setNameMethod.invoke(objKeywordName);     // EXECUTE THE METHOD
													break;
												}

											}

										} 
									}
									//@AFTERTEST BLOCK :				//THIS LOOP IS TO FIND THE @AfterTest ANNOTATION METHOD TO BE EXECUTED LAST
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

	public static ArrayList<String> GetTestDataIterations(XSSFCell TC_in_TD , XSSFSheet TDSheet) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException
	{	
		String[][] TestData = null;
		int DataIterations =0;
		ArrayList<String> TDList=new ArrayList<String>();
		ArrayList<String> TDIter = null  ;
		for(int td=0;td<=TestDataRowCnt;td++)
		{
			try
			{
				if(TC_in_TD.toString().equalsIgnoreCase(TDSheet.getRow(td).getCell(0).toString()))
				{
					//System.out.println(TDSheet.getRow(td).getCell(0).toString());
					DataIterations  = DataIterations + 1;
					//System.out.println("Test Data Row number is : " +TDSheet.getRow(td));
					int TDRowNum  = TDSheet.getRow(td).getCell(0).getRowIndex();
					TDList.add( TC_in_TD+"|"+TDRowNum);
					TDIter = TDList;

				}	 
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		return TDIter;
	}

	public static int GetLastIndexofChar(String str, char x)
	{
		// Returns last index of x if 
		{ 
			int index = -1; 
			for (int i = 0; i < str.length(); i++) 
				if (str.charAt(i) == x) 
					index = i; 
			return index; 
		}
	}



}
