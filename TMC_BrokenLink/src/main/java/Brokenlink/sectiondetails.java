package Brokenlink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class sectiondetails {
	public static void upload(String path) throws IOException, InterruptedException
	{
		Process proc=Runtime.getRuntime().exec("D:\\AutomationFramework\\TMCconfig\\src\\test\\resources\\upload.exe"+" "+path);
		
		System.err.println(proc.isAlive());

		for (int k = 0; k < 1;)  {
			if (proc.isAlive()) {
				System.err.println(" part1");
				Thread.sleep(3000);
			}else {
				System.err.println("else part");
				break;
			} 
		} 
		
	}

	public static void wait(WebDriver driver) {
		
		int i;
		for ( i = 0; i <1; ) {
			
	
		try {
			driver.findElement(By.xpath("//li[contains(text(),'1File uploaded')]")).isDisplayed();
			break;
		} catch (Exception e) {
		System.out.println("Path not found");
		 i = 0;
		}
		}
		
	}
	
	
	public static void waits(WebDriver d)
	{
		
		FluentWait wait = new FluentWait(d);
		//Specify the timout of the wait
		wait.withTimeout(5000, TimeUnit.MILLISECONDS);
		//Sepcify polling time
		wait.pollingEvery(350, TimeUnit.MILLISECONDS);
		//Specify what exceptions to ignore
		wait.ignoring(NoSuchElementException.class);
		
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		TMC_Brokenlinks tbl = new TMC_Brokenlinks();

		WebDriver driver = null;

		WebDriverManager.chromedriver().setup();
		ArrayList<String> arrayList = null;
		ArrayList<String> arrayList1 = null;
		driver = new ChromeDriver();

		driver.get("https://thanecity.gov.in/tmc/CitizenHome.html");
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		tbl.checkPageIsReady(driver);

		tbl.lang(driver);
		tbl.checkPageIsReady(driver);
		
		driver.findElement(By.linkText("Login")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Portal Admininstrator Login")));
		driver.findElement(By.linkText("Portal Admininstrator Login")).click();
		
		
		driver.findElement(By.id("emploginname")).click();
		driver.findElement(By.id("emploginname")).sendKeys("sysadmin");
		driver.findElement(By.id("adminEmployee.emppassword")).click();
		driver.findElement(By.id("adminEmployee.emppassword")).sendKeys("Abm@tmc2020");
		driver.findElement(By.id("captchaSessionLoginValue")).click();
		 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"adminLoginForm\"]/div[5]/div[1]/input")));
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id=\"adminLoginForm\"]/div[5]/div[1]/input")).click();

		//excel
		ReadGuru99ExcelFile objExcelFile = new ReadGuru99ExcelFile();
	    String filePath = "C:\\Users\\swapnil.patil\\Desktop\\Photos\\";
	     objExcelFile.readExcel(filePath,"Exp.xlsx","Sheet1");
	     for (int i = 0; i < objExcelFile.map1.get("Link").size(); i++) {
	  
	    	 
	    	 String LinkName=objExcelFile.map1.get("Link").get(i).toString();
	    	 String LinkNamereg=objExcelFile.map1.get("LinkMAR").get(i).toString();
	    	 String srno=objExcelFile.map1.get("SRNO").get(i).toString();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");
		
		Thread.sleep(2000);
		
driver.findElement(By.linkText("ADD SECTION DETAILS")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addFormSection")));
Thread.sleep(2000);
driver.findElement(By.xpath("//*[@id=\"linkSection_chosen\"]/a/span")).click();

wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("searchlist")));

driver.findElement(By.name("searchlist")).click();


driver.findElement(By.name("searchlist")).sendKeys(LinkName);
Thread.sleep(1000);
driver.findElement(By.name("searchlist")).sendKeys(Keys.ENTER);




File directoryPath = new File("C:\\Users\\swapnil.patil\\Desktop\\Photos\\Data\\"+LinkName+"\\");

File filesList[] = directoryPath.listFiles();
System.out.println(filesList.length);


for (int ii = 0; ii < filesList.length; ii++) {
	System.out.println("File path: "+filesList[ii]);
	
	if (ii ==0) {
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"AdminAdd\"]")).click();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"command\"]/div[1]/label")));
		driver.findElement(By.xpath("//*[@id=\"command\"]/div[1]/label")).click();

		String Path=filesList[ii].toString();
		upload(Path);
		wait(driver);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")));
		driver.findElement(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")).click();
		
		
		
	}else {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"button\"][value=\"Add Record\"]")));
		driver.findElement(By.cssSelector("input[type=\"button\"][value=\"Add Record\"]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"command\"]/div[1]/label")));
		driver.findElement(By.xpath("//*[@id=\"command\"]/div[1]/label")).click();

		String Path1=filesList[ii].toString();
		upload(Path1);
		wait(driver);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")));
		driver.findElement(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")).click();
		
	}
	
	
	
	

}




wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")));
driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")).click();

js.executeScript("window.scrollBy(0,500)");

Thread.sleep(2000);

driver.findElement(By.linkText("APPROVE SECTION DETAILS")).click();

wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AdminUpdate")));
Thread.sleep(2000);
driver.findElement(By.xpath("//*[@id=\"linkSection_chosen\"]/a/span")).click();

wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("searchlist")));

driver.findElement(By.name("searchlist")).click();


driver.findElement(By.name("searchlist")).sendKeys(LinkName);
Thread.sleep(1000);
driver.findElement(By.name("searchlist")).sendKeys(Keys.ENTER);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"button\"][value=\"Approve Record\"]")));
driver.findElement(By.cssSelector("input[type=\"button\"][value=\"Approve Record\"]")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AdminFaqChekerback")));






for (int k = 0; k < filesList.length; k++) {
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"sectionDataTable\"]/tbody/tr[1]/td[4]/a/u")));
	driver.findElement(By.xpath("//*[@id=\"sectionDataTable\"]/tbody/tr[1]/td[4]/a/u")).click();
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("entity.chekkerflag1")));
	Thread.sleep(1000);
	driver.findElement(By.id("entity.chekkerflag1")).click();
	
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")));
	driver.findElement(By.cssSelector("input[type=\"submit\"][value=\"Submit\"]")).click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AdminFaqChekerback")));
	Thread.sleep(2000);
	
	
}

wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")));
driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")).click();








//













	}
	}
}
