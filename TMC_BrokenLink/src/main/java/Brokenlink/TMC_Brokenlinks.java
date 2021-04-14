package Brokenlink;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TMC_Brokenlinks {
	public static int counts = 0;
	public static int counts1 = 0;

	public static void cmsdataRead(WebDriver driver,String data) throws IOException {
		String homePage = "http://13.234.173.159/Skdcl/";
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		ExcelWriting ew = new ExcelWriting();
		String sectionname = driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[2]/div/div/div[1]/h2")).getText()
				.toString().replaceAll("[^a-zA-Z0-9]", " ");
		String excelpath = data + sectionname + ".xlsx";
		createfile(excelpath);
	//	createfile(data+"FinalSheet.xlsx");
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
							if (links.get(i).getAttribute("onclick") != null) {

								if (links.get(i).getAttribute("onclick").contains("downloadFile")) {
									// System.out.println(links.get(i).getAttribute("onclick"));
									JavascriptExecutor js = (JavascriptExecutor) driver;
									js.executeScript(links.get(i).getAttribute("onclick"));
									wind = new ArrayList<String>();
									Set<String> allWindowHandles1 = driver.getWindowHandles();
									for (String handle : allWindowHandles1) {
										wind.add(handle);
									}

									driver.switchTo().window(wind.get(1));
									checkPageIsReady(driver);
									System.out.print("||" + driver.getCurrentUrl());
									l.add(driver.getCurrentUrl());

									driver.close();
									driver.switchTo().window(wind.get(0));

								}
							}
						} catch (Exception e) {

							// e.printStackTrace();
						}

					}

					String p = data;

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
							System.out.print("| " + Failedtoload(url));
							valSetOne1.add(Failedtoload(url));
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
				ew.excelWriting(data+"FinalSheet.xlsx", counts, valSetOne2);
				System.out.println();
			}
			ew.excelWriting(excelpath, t, valSetOne1);
			System.out.println();

		}

	}

	public static void createfile(String Data) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();

		Workbook wb = new XSSFWorkbook();
		Sheet sheet1 = wb.createSheet("Sheet1");
		FileOutputStream fileOut = new FileOutputStream(Data);
		wb.write(fileOut);
		fileOut.close();
		wb.close();
	}

	public static String Failedtoload(String link) {
		String Status = "";
		try {
		
			URL url = new URL(link);
			InputStream in = url.openStream();
			String pdfname[] = link.split("/");

			int length = -1;
			byte[] buffer = new byte[50000];
			if (in.read(buffer) == -1) {

				Status = "Failed to load pdf";
			} else {
				Status = "Pass";
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Status="Pdf Not Present On Server";
		}

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

	public static void panel(WebDriver driver) {

		try {

			if (driver.findElement(By.xpath("//h4[@class=\"panel-title\"]")).isDisplayed()) {
			}

			List<WebElement> lists = driver.findElements(By.xpath("//a[starts-with(@href,'#panel')]"));
			for (int j = 1; j <= lists.size(); j++) {
				WebElement button = driver.findElement(By.xpath("(//a[starts-with(@href,'#panel')])[" + j + "]"));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", button);
			}

		} catch (Exception e) {
			// TODO: handle exception
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

	static ArrayList<String> valSetOne1 = null;
	static ArrayList<String> valSetOne2 = null;

	public static void ckeditor(WebDriver driver,String data) throws IOException {
		counts1 = 0;
		String homePage = "http://13.234.173.159/Skdcl/";
		String url = "";
		HttpURLConnection huc = null;
		int respCode = 200;
		ExcelWriting ew = new ExcelWriting();

		panel(driver);

		All(driver);
		checkPageIsReady(driver);

		WebElement contentanimated = driver.findElement(By.className("widget"));
		List<String> l = new ArrayList<String>();
		List<WebElement> list = contentanimated.findElements(By.tagName("a"));
		if (list.size() > 0) {

			String sectionname = driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[2]/div/div/div[1]/h2"))
					.getText().toString().replaceAll("[^a-zA-Z0-9]", " ");
			// String sectionname =
			// driver.findElement(By.xpath("//*[@id=\"nischay\"]/div/div[1]/h2")).getText().toString().replaceAll("[^a-zA-Z0-9]",
			// " ");

			String excelpath = data + sectionname + ".xlsx";
			createfile(excelpath);

			for (int i = 0; i < list.size(); i++) {
				valSetOne1 = new ArrayList<String>();
				valSetOne2 = new ArrayList<String>();

				valSetOne1.add(sectionname);
				l = new ArrayList<String>();
				// System.err.println(list.get(i).getAttribute("href"));

				if (list.get(i).getAttribute("href") != null) {

					if (list.get(i).getAttribute("href").contains("SectionInformation.html?editForm&rowId")) {

					} else {

						l.add(list.get(i).getAttribute("href"));
						Iterator<String> it = l.iterator();
						while (it.hasNext()) {

							url = it.next();
							valSetOne1.add(list.get(i).getText());
							valSetOne1.add(url);
							// System.out.println(url);

							if (url == null || url.isEmpty()) {
								// System.out.print("| URL is either not configured for anchor tag or it is
								// empty");
								valSetOne1.add("URL is either not configured for anchor tag or it is empty");
								continue;
							}

							if (!url.startsWith(homePage)) {
								// System.out.print("| URL belongs to another domain, skipping it.");
								valSetOne1.add("URL belongs to another domain, skipping it.");
								continue;
							}

							try {
								huc = (HttpURLConnection) (new URL(url).openConnection());

								huc.setRequestMethod("HEAD");

								huc.connect();
								valSetOne1.add(Failedtoload(url));
								respCode = huc.getResponseCode();
								// System.err.println(respCode);
								if (respCode >= 400) {
									// System.out.print(url + " is a broken link");
									valSetOne1.add("broken link");
								} else {
									// System.out.print(url + " is a valid link");
									valSetOne1.add("valid link");
								}

							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						counts1++;
						ew.excelWriting(excelpath, counts1, valSetOne1);

						if (valSetOne1.contains("broken link")) {
							valSetOne2.addAll(valSetOne1);
							counts++;
							ew.excelWriting(data+"FinalSheet.xlsx",
									counts, valSetOne2);

						}

					}
				}
			}
		}

	}
	public static String[] pathsFetchr() throws IOException {
		String currentDir = System.getProperty("user.dir");
	     Date now = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy  hh mm ss");
	    String time = dateFormat.format(now);
	    File dir = new File("Report Date"+time);
	    dir.mkdir();
	  
	    File dir1 = new File(currentDir+"\\"+"Report Date"+time+"\\"+"\\CMS");
	    File dir2 = new File(currentDir+"\\"+"Report Date"+time+"\\"+"\\Ckeditor");
	    dir1.mkdir();
	    dir2.mkdir();
	
	    String[] path = new String[3];
	    path[0]=currentDir;
	    path[1]=currentDir+"\\"+"Report Date"+time+"\\"+"\\CMS\\";
	    path[2]=currentDir+"\\"+"Report Date"+time+"\\"+"\\Ckeditor\\";
	    createfile(currentDir+"\\"+"Report Date"+time+"\\"+"\\CMS\\"+"FinalSheet.xlsx");
	    createfile(currentDir+"\\"+"Report Date"+time+"\\"+"\\Ckeditor\\"+"FinalSheet.xlsx");
	    return path;
	}

	public static void main(String[] args) throws IOException {
		WebDriver driver = null;

		WebDriverManager.chromedriver().setup();
		
	
		ArrayList<String> arrayList = null;
		ArrayList<String> wind = new ArrayList<String>();
		driver = new ChromeDriver();

		driver.get("http://13.234.173.159/Skdcl/CitizenHome.html");
		driver.manage().window().maximize();
		/*
		 * checkPageIsReady(driver); lang(driver); arrayList=new ArrayList<String>();
		 * 
		 * 
		 * List<String> linklists = Arrays.asList(
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=36",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=5",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=6",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=7",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=8",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=9",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=10",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=11",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=74",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=75",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=350",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=54",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=35",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=56",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=34",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=14",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=15",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=16",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=17",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=18",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=209",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=210",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=30",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=41",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=42",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=62",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=19",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=20",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=21",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=22",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=47",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=53",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=61",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=64",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=65",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=67",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=57",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=27",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=28",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=29",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=45",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=69",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=70",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=58",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=59",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=60",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=1",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=2",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=3",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=4",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=24",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=26",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=31",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=32",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=33",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=43",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=44",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=72",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=76",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=77",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=78",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=79",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=80",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=81",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=82",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=83",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=84",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=85",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=86",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=87",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=88",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=89",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=90",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=91",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=92",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=93",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=94",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=95",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=96",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=97",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=98",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=99",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=100",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=101",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=102",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=103",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=104",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=105",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=106",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=107",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=109",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=110",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=111",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=112",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=113",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=114",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=115",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=116",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=117",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=118",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=119",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=120",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=121",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=122",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=123",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=124",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=125",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=126",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=127",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=128",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=129",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=130",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=131",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=132",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=133",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=134",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=135",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=136",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=137",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=138",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=139",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=140",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=141",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=142",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=143",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=144",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=145",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=146",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=147",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=148",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=149",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=150",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=151",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=152",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=153",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=154",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=155",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=156",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=157",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=158",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=159",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=160",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=161",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=162",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=163",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=164",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=165",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=166",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=167",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=168",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=169",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=170",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=171",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=172",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=173",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=174",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=175",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=176",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=177",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=178",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=179",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=180",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=181",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=182",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=183",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=184",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=185",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=186",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=187",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=188",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=189",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=190",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=191",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=192",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=193",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=194",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=195",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=196",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=197",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=198",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=199",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=200",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=202",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=203",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=204",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=205",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=206",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=207",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=208",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=211",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=216",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=217",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=218",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=219",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=220",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=221",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=222",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=223",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=224",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=225",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=226",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=227",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=228",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=229",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=230",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=231",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=232",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=233",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=234",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=235",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=236",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=237",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=238",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=239",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=240",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=241",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=242",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=243",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=244",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=245",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=246",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=247",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=248",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=249",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=250",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=251",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=252",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=253",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=254",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=255",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=256",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=257",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=258",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=259",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=260",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=261",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=262",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=263",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=264",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=265",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=266",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=267",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=268",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=269",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=270",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=271",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=272",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=273",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=274",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=275",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=276",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=277",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=278",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=279",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=280",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=281",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=282",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=283",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=284",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=285",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=286",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=287",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=288",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=289",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=290",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=291",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=292",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=293",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=294",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=295",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=296",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=297",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=298",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=299",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=300",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=301",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=302",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=303",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=304",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=305",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=306",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=307",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=308",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=309",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=310",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=311",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=312",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=313",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=314",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=315",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=316",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=317",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=318",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=319",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=320",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=321",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=322",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=323",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=324",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=325",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=326",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=327",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=328",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=329",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=330",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=331",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=332",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=333",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=334",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=335",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=336",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=337",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=338",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=339",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=340",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=341",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=342",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=343",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=344",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=345",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=346",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=347",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=348",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=349",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=351",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=352",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=353",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=354",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=357",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=358",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=359",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=360",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=361",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=362",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=363",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=364",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=365",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=366",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=367",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=368",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=369",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=370",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=371",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=372",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=373",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=374",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=375",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=376",
		 * "https://thanecity.gov.in/tmc/SectionInformation.html?editForm&rowId=73");
		 * 
		 * arrayList.addAll(linklists);
		 * 
		 * System.out.println(" arrayList.size()" + arrayList.size());
		 */
		
		
		checkPageIsReady(driver);
		lang(driver);

		driver.findElement(By.id("search_input")).sendKeys("Smart City");

		driver.findElement(By.id("search_input")).sendKeys(Keys.ENTER);

		checkPageIsReady(driver);

		java.util.List<WebElement> smartlinks = driver.findElements(By.cssSelector("a[class='internal']"));

		System.out.println("Smart city links" + smartlinks.size());

		arrayList = new ArrayList<String>();
		for (int j = 0; j < smartlinks.size(); j++)

		{
			try {
				// System.out.println(smartlinks.get(j).getAttribute("href"));
				if (smartlinks.get(j).getAttribute("href") == null) {

				} else {
					if (smartlinks.get(j).getAttribute("href").contains("SectionInformation.html?")) {
						if (!(arrayList.contains(smartlinks.get(j).getAttribute("href")))) {
							arrayList.add(smartlinks.get(j).getAttribute("href"));

						}

					}
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	//	driver.findElement(By.xpath("//*[@id=\"text-resize\"]/header[1]/nav/header/nav/ul/li[1]/a/i")).click();
		checkPageIsReady(driver);

		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id=\"text-resize\"]/header[1]/nav/div/div[3]/div/span[6]/a/i")));

		driver.findElement(By.xpath("//*[@id=\"text-resize\"]/header[1]/nav/div/div[3]/div/span[6]/a/i")).click();
		checkPageIsReady(driver);
		java.util.List<WebElement> sitemaplinks = driver.findElements(By.cssSelector("a[class='internal']"));
		System.out.println("Sitmap link" + sitemaplinks.size());

		for (int j = 0; j < sitemaplinks.size(); j = j + 1)

		{
			try {

				if (sitemaplinks.get(j).getAttribute("href") == null) {

				} else {
					if (sitemaplinks.get(j).getAttribute("href").contains("SectionInformation.html?")) {

						if (!(arrayList.contains(sitemaplinks.get(j).getAttribute("href")))) {
							// System.out.println("link added");
							arrayList.add(sitemaplinks.get(j).getAttribute("href"));

						}

					}

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		
		

		for (int i = 0; i < arrayList.size(); i++) {
			System.err.println(i + "arrayList href data ==>" + arrayList.get(i));
		}

		String p[]=pathsFetchr();
		
		
		for (int k = 0; k < arrayList.size(); k++) {

			// System.out.println(arrayList.get(k));

			driver.get(arrayList.get(k));

			checkPageIsReady(driver);
			All(driver);

			if (driver.findElements(By.xpath("//a[starts-with(@onclick,'downloadFile')]")).size() > 0) {
				cmsdataRead(driver,p[1]);
			} else {

				ckeditor(driver,p[2]);
			}

		}
	}

}
