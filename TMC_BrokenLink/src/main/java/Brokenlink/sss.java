package Brokenlink;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class sss {

	public static void main(String[] args) {
		File directoryPath = new File("C:\\Users\\swapnil.patil\\Desktop\\SKDSL\\Data\\");

		File filesList[] = directoryPath.listFiles();
		System.out.println(filesList.length);

		for (int no = 0; no < filesList.length; no++) {
			// System.out.println("File path: "+filesList[i]);
			File filesList1[] = filesList[no].listFiles();
			for (int no1 = 0; no1 < filesList1.length; no1++) {

				if (filesList1[no1].toString().contains(".xlsx")) {
					System.out.println("File path: " + filesList1[no1]);
					 Path path = Paths.get(filesList1[no1].toString()); 
					  
				    
				        Path fileName = path.getFileName(); 
				      //  System.err.println("FileName: "  + fileName.toString().replaceAll(".xlsx", "")); 
				      
				       // System.out.println("FileName: "  + fileName.toString()); 
					
				}

			}

		}
	}}
