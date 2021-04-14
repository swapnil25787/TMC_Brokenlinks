package Brokenlink;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Ckeditor {
	
	static ArrayList<String> valSetOne1 = null;
	static ArrayList<String> valSetOne2 = null;
	public static int counts = 0;
	public static int counts1 = 0;
	
	public  static void createfile(String Data) throws IOException {
		 XSSFWorkbook workbook = new XSSFWorkbook();

		    Workbook wb = new XSSFWorkbook();
		    Sheet sheet1 = wb.createSheet("Sheet1");
		    FileOutputStream fileOut = new FileOutputStream(Data);
		    wb.write(fileOut);
		    fileOut.close();
		    wb.close();
	}

	public static String Failedtoload(String link, String path) {
		String Status = "";
		try {

			URL url = new URL(link);
			InputStream in = url.openStream();
			String pdfname[] = link.split("/");

			// System.out.println("Pdfname"+pdfname[pdfname.length-1].replace("%20", " "));
			//FileOutputStream fos = new FileOutputStream(new File(path + pdfname[pdfname.length - 1].replace("%20", " ")));
			int length = -1;
			byte[] buffer = new byte[50000];
			// System.err.println(in.read(buffer));
			if (in.read(buffer) == -1) {
				// System.out.println("=======================");
				Status = "Failed to load pdf";
			} else {
				Status = "Pass";
			}
			//fos.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(Status);
		return Status;

	}

	public static void All(WebDriver driver) {

		try {
			if (driver.findElement(By.xpath("//*[@id='DataTables_Table_0_length']/label/select")).isDisplayed()) {
				// System.err.println("pagination is there");
				Thread.sleep(1000);
				WebElement drpCountry1 = driver.findElement(By.name("DataTables_Table_0_length"));
				drpCountry1.sendKeys("All");
				Thread.sleep(4000);

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

		}
	}
	
	

	public static void panel(WebDriver driver) {
		
		
		
		try {
			
			if (driver.findElement(By.xpath("//h4[@class=\"panel-title\"]")).isDisplayed()) {}
			

			List<WebElement> lists=driver.findElements(By.xpath("//a[starts-with(@href,'#panel')]"));
			for (int j = 1; j <= lists.size(); j++) {
				WebElement button=driver.findElement(By.xpath("(//a[starts-with(@href,'#panel')])["+j+"]"));
				JavascriptExecutor executor = (JavascriptExecutor)driver;
				executor.executeScript("arguments[0].click();", button);
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	

	public static void checkPageIsReady(WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int j = 0; j < 1;) {

			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				// System.out.println("Page Is loaded.");
				break;
			} else {
				j = 0;
			}

		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void lang(WebDriver driver) {

		try {

			if (driver.findElement(By.xpath("//*[@id=\"text-resize\"]/header[1]/nav/div/div[3]/div/span[8]/a"))
					.isDisplayed()) {
				driver.findElement(By.xpath("//*[@id=\"text-resize\"]/header[1]/nav/div/div[3]/div/span[8]/a")).click();
			} else {
				System.err.println("Website is alerady in english language");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("Lang exception block");
		}

	}
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		WebDriver driver = null;

		WebDriverManager.chromedriver().setup();
		ArrayList<String> arrayList = null;
		ArrayList<String> wind = new ArrayList<String>();
		driver = new ChromeDriver();

		driver.get("https://thanecity.gov.in/tmc/CitizenHome.html");
		driver.manage().window().maximize();
		checkPageIsReady(driver);
		lang(driver);

		arrayList=new ArrayList<String>();
				
				arrayList.add("https://thanecity.gov.in/tmc/CitizenHome.html?newsAndEvent");
				arrayList.add("https://thanecity.gov.in/tmc/CitizenHome.html?schemes");
				arrayList.add("https://thanecity.gov.in/tmc/CitizenHome.html?usefullLink");
				checkPageIsReady(driver);
				panel(driver);
				
		for (int i = 0; i < arrayList.size(); i++) {
			driver.get(arrayList.get(i));
			All(driver);
			ckeditor(driver);
					
					
					
					
					
		}
					
			
			}

	
	
	
	public static void ckeditor(WebDriver driver) throws IOException {
		String homePage = "https://thanecity.gov.in/";
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		ExcelWriting ew = new ExcelWriting();
		String sectionname = driver.findElement(By.xpath("//*[@id=\"nischay\"]/div/div[1]/h2")).getText()
				.toString().replaceAll("[^a-zA-Z0-9]", " ");
		String excelpath = "C:\\Users\\swapnil.patil\\Desktop\\CMS\\" + sectionname + ".xlsx";
		createfile(excelpath);
		int size = driver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr")).size();
		System.out.println("row size of the table" + size);
		for (int t = 1; t <= size; t++) {
			valSetOne1 = new ArrayList<String>();
			valSetOne2 = new ArrayList<String>();
			valSetOne1.add(sectionname);
			int col = driver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr[" + t + "]/td")).size();
			if (col == 0) {
				continue;
			}
			
			for (int c = 1; c <= col; c++) {

				// System.out.print(driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["
				// + t + "]/td[" + c + "]")).getText() + "|");
				valSetOne1.add(driver
						.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr[" + t + "]/td[" + c + "]"))
						.getText());

				try {
					// System.out.println(driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["
					// + t + "]/td[" + c + "]/a")).getAttribute("onclick"));

					WebElement w = driver
							.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr[" + t + "]/td[" + c + "]"));
					// System.out.println(w.findElements(By.tagName("a")).size());

					//////////////////////////// broken add

					// WebElement contentanimated=driver.findElement(By.className("widget"));
					ArrayList<String> wind = new ArrayList<String>();
					List<WebElement> links = w.findElements(By.tagName("a"));
					List<String> l = new ArrayList<String>();
					for (int i = 0; i < links.size(); i++) {

						try {
							if (links.get(i).getAttribute("href") != null) {

								if (links.get(i).getAttribute("href").contains("http")) {
								
									checkPageIsReady(driver);
								
									l.add(links.get(i).getAttribute("href"));

								

								}
							}
						} catch (Exception e) {

							// e.printStackTrace();
						}

					}

					String p = "C:\\Users\\swapnil.patil\\Desktop\\CMS\\";

					// System.out.println("links"+l.size());
					Iterator<String> it = l.iterator();

					while (it.hasNext()) {

						url = it.next();
						valSetOne1.add(url);
						// System.out.println(url);

						if (url == null || url.isEmpty()) {
							System.out.print("| URL is either not configured for anchor tag or it is empty");
							valSetOne1.add("URL is either not configured for anchor tag or it is empty");
							continue;
						}

						if (!url.startsWith(homePage)) {
							System.out.print("| URL belongs to another domain, skipping it.");
							valSetOne1.add("URL belongs to another domain, skipping it.");
							continue;
						}

						try {
							huc = (HttpURLConnection) (new URL(url).openConnection());

							huc.setRequestMethod("HEAD");

							huc.connect();
							System.out.print("| " + Failedtoload(url, p));
							valSetOne1.add(Failedtoload(url, p));
							respCode = huc.getResponseCode();
							// System.err.println(respCode);
							if (respCode >= 400) {
								System.out.print(url + " is a broken link");
								valSetOne1.add("broken link");
							} else {
								System.out.print(url + " is a valid link");
								valSetOne1.add("valid link");
							}

						} catch (MalformedURLException e) { // TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) { // TODO Auto-generated catch
							e.printStackTrace();
						}
					}

				} catch (Exception e) {

					// e.printStackTrace();
				}

			}

			if (valSetOne1.contains("Failed to load pdf") || valSetOne1.contains("broken link")) {
				valSetOne2.addAll(valSetOne1);
				counts++;
				ew.excelWriting("C:\\Users\\swapnil.patil\\Desktop\\CMS\\FinalSheet.xlsx", counts, valSetOne2);
				System.out.println();
			}
			ew.excelWriting(excelpath, t, valSetOne1);
			System.out.println();

		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
