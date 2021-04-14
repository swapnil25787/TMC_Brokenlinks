package Brokenlink;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReadGuru99ExcelFile {

	ArrayList<Cell> valSetOne1 = null;
	
	public static Map<String, ArrayList<Cell>> map1 = new LinkedHashMap<String, ArrayList<Cell>>();
    public void readExcel(String filePath,String fileName,String sheetName) throws IOException, InterruptedException{

  
    File file =    new File(filePath+"\\"+fileName);

    

    FileInputStream inputStream = new FileInputStream(file);

    Workbook guru99Workbook = null;

   

    String fileExtensionName = fileName.substring(fileName.indexOf("."));

  

    if(fileExtensionName.equals(".xlsx")){



    guru99Workbook = new XSSFWorkbook(inputStream);

    }

 

    else if(fileExtensionName.equals(".xls")){

      

        guru99Workbook = new HSSFWorkbook(inputStream);

    }
    Cell  key = null;
    


    Sheet guru99Sheet = guru99Workbook.getSheet(sheetName);



    int rowCount = guru99Sheet.getLastRowNum()-guru99Sheet.getFirstRowNum();
  
  


     // System.out.println(guru99Sheet.getRow(0).getLastCellNum());
      

        for (int j = 0; j < guru99Sheet.getRow(0).getLastCellNum(); j++) {
        	  valSetOne1 = new ArrayList<Cell>();
        	for (int ii = 0; ii < rowCount+1; ii++) {
        		 
        		if (ii==0) {
        		 	key=guru99Sheet.getRow(ii).getCell(j);
        		}else {
        			try {
        			
        				//System.out.println(ii+"ij"+j+"============>"+guru99Sheet.getRow(ii).getCell(j).CELL_TYPE_BLANK);
        				
        				
						valSetOne1.add(guru99Sheet.getRow(ii).getCell(j));
						
					} catch (Exception e) {
				
					}
        			
		
					
		
						
					}	map1.put(key.toString(), valSetOne1);
        		}
        	}
        
        	
        	
          
 
       


        }

        
        

    

    

    

    //Main function is calling readExcel function to read data from excel file

    public static void main(String args[]) throws IOException, InterruptedException{

 

    ReadGuru99ExcelFile objExcelFile = new ReadGuru99ExcelFile();
    String filePath = "D:\\AutomationFramework\\TMCconfig\\";
  //   objExcelFile.readExcel(filePath,"AppointmentsTransfer Orders.xlsx","Sheet2");
     objExcelFile.readExcel(filePath,"ExportExcel.xlsx","Sheet4");
   // System.err.println(map1);

   // System.err.println(map1.get("SN").get(2));
  //  System.err.println(map1.get("SN").size());
    }

}
