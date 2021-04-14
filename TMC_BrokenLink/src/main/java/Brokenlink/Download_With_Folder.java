package Brokenlink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Download_With_Folder {
	public static void DownloadPDFFromURL(String link, String path) {
		try {

			URL url = new URL(link);
			InputStream in = url.openStream();
			String pdfname[] = link.split("/");

			String filename = pdfname[pdfname.length - 1].replace("%20", " ");
			FileOutputStream fos = new FileOutputStream(new File(path + filename));
			int length = -1;
			byte[] buffer = new byte[50000];
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			fos.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		TMC_Brokenlinks tbl = new TMC_Brokenlinks();

		WebDriver driver = null;

		WebDriverManager.chromedriver().setup();

		ArrayList<String> arrayList = null;
		driver = new ChromeDriver();

		driver.get("https://thanecity.gov.in/tmc/CitizenHome.html");
		driver.manage().window().maximize();

		tbl.checkPageIsReady(driver);
		tbl.lang(driver);
		arrayList = new ArrayList<String>();
		arrayList.add("https://thanecity.gov.in/tmc/CitizenHome.html?schemes");
		arrayList.add("https://thanecity.gov.in/tmc/CitizenHome.html?usefullLink");

		for (int i = 0; i < arrayList.size(); i++) {
			driver.navigate().to(arrayList.get(i));
			tbl.checkPageIsReady(driver);
			tbl.All(driver);
			tbl.checkPageIsReady(driver);
			WebElement contentanimated = driver.findElement(By.className("widget"));
			Thread.sleep(5000);
			List<WebElement> list = contentanimated.findElements(By.tagName("a"));

			
			for (int j = 0; j < list.size(); j++) {
				String Parentpath = "C:\\Users\\swapnil.patil\\Desktop\\analyssi\\EIP_ANNOUNCEMENT\\";
				 System.out.println(list.get(j).getAttribute("href"));

				// String
				// link1=list.get(j).getAttribute("href").replace("https://thanecity.gov.in/tmc/cache/1/",
				// "");
				// String link2=link1.replace("https://thanecity.gov.in/tmc/cache/1/", "");
				 
				 
				 
				 
				 if (list.get(j).getAttribute("href")!=null) {
					 String pdfname[] = list.get(j).getAttribute("href").replace("https://thanecity.gov.in/tmc/cache/1/", "").split("/");
						String path = "";
						for (int k = 0; k < pdfname.length-1; k++) {

							
								path = path+pdfname[k] + "\\";
							

						}
						System.out.println(Parentpath+path);
						  File file = new File(Parentpath+path);
						  file.mkdirs();
						
						DownloadPDFFromURL(list.get(j).getAttribute("href"), Parentpath+path);

				}
				
			}

		}

	}

}
