package Brokenlink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Photogallery {

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
		ArrayList<String> arrayList1 = null;
		driver = new ChromeDriver();

		driver.get("https://thanecity.gov.in/tmc/CitizenHome.html");
		driver.manage().window().maximize();

		tbl.checkPageIsReady(driver);

		tbl.lang(driver);
		tbl.checkPageIsReady(driver);
		driver.navigate().to("https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=24");
		tbl.checkPageIsReady(driver);
		tbl.All(driver);
		String Parentpath = "C:\\Users\\swapnil.patil\\Desktop\\Photos\\Data\\";
		arrayList1 = new ArrayList<String>();
		WebElement contentanimated = driver.findElement(By.className("widget"));
		List<WebElement> list = contentanimated.findElements(By.tagName("a"));

		for (int i = 0; i < list.size(); i++) {
			
			tbl.checkPageIsReady(driver);
			arrayList1.add(list.get(i).getAttribute("href"));
			System.out.println(list.get(i).getAttribute("href"));
		}
		
for (int j = 0; j < arrayList1.size(); j++) {
	driver.navigate().to(arrayList1.get(j));
	tbl.checkPageIsReady(driver);
	Thread.sleep(6000);
	String title=driver.findElement(By.xpath("//*[@id=\"CitizenService\"]/li[2]")).getText();
	WebElement contentanimated1 = driver.findElement(By.className("widget"));
	List<WebElement> img = contentanimated1.findElements(By.tagName("a"));
	
	File file = new File(Parentpath+"Album"+j+"\\");
	  file.mkdirs();
	
	for (int jj = 0; jj < img.size(); jj++) {

		System.err.println(img.get(jj).getAttribute("href"));
		String imagepathlink=img.get(jj).getAttribute("href");
		
		  
		
		  DownloadPDFFromURL(imagepathlink.replaceAll(" ", "%20"), Parentpath+"Album"+j+"\\");
		
		
		

	}
}

		





	}

}
