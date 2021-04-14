package Brokenlink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class submenu {
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
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");
		
		Thread.sleep(2000);

driver.findElement(By.linkText("ADD SUB MENU")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add New Section")));
Thread.sleep(2000);
driver.findElement(By.linkText("Add New Section")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("entity.linksMaster.linkId")));
WebElement ModuleName = driver.findElement(By.name("entity.linksMaster.linkId"));
ModuleName.sendKeys("Smart City");



////////////sec name
String LinkName=objExcelFile.map1.get("Link").get(i).toString();
String LinkNamereg=objExcelFile.map1.get("LinkMAR").get(i).toString();
String srno=objExcelFile.map1.get("SRNO").get(i).toString();


driver.findElement(By.id("subLinkNameEn")).sendKeys(LinkName);
driver.findElement(By.id("entity.subLinkNameRg")).sendKeys(LinkNamereg);

driver.findElement(By.id("entity.subLinkOrder")).sendKeys(srno);






WebElement highlight = driver.findElement(By.name("entity.isLinkModify"));
highlight.sendKeys("True");



WebElement st = driver.findElement(By.name("entity.sectionType0"));
st.sendKeys("Photo Gallery");

wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"SectionEntryForm\"]/div[9]/input")));
driver.findElement(By.xpath("//*[@id=\"SectionEntryForm\"]/div[9]/input")).click();


///////////eleement
wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add New Element")));
Thread.sleep(2000);
driver.findElement(By.linkText("Add New Element")).click();


wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameEn")));
driver.findElement(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameEn")).sendKeys("Photo");

driver.findElement(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameRg")).sendKeys("Photo");



wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("subLinkFieldMapping.subLinkFieldlist[0].fieldType")));
WebElement tof = driver.findElement(By.name("subLinkFieldMapping.subLinkFieldlist[0].fieldType"));
tof.sendKeys("Photo");

driver.findElement(By.xpath("//*[@id=\"command\"]/div[4]/input[1]")).click();
Thread.sleep(2000);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add New Element")));
driver.findElement(By.linkText("Add New Element")).click();


wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameEn")));
driver.findElement(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameEn")).sendKeys("Caption");

driver.findElement(By.id("subLinkFieldMapping.subLinkFieldlist0.fieldNameRg")).sendKeys("Caption");



wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("subLinkFieldMapping.subLinkFieldlist[0].fieldType")));
WebElement tof1 = driver.findElement(By.name("subLinkFieldMapping.subLinkFieldlist[0].fieldType"));
tof1.sendKeys("TextField");

driver.findElement(By.xpath("//*[@id=\"command\"]/div[4]/input[1]")).click();


///submit

wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"command\"]/input[3]")));
driver.findElement(By.xpath("//*[@id=\"command\"]/input[3]")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnNo")));
driver.findElement(By.id("btnNo")).click();


///////////////Checker flow
Thread.sleep(1000);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")));
driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")).click();


js.executeScript("window.scrollBy(0,500)");

Thread.sleep(2000);

driver.findElement(By.linkText("APPROVE SUB MENU")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"command\"]/div[2]/button")));

wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("moduleId")));
WebElement moduleId = driver.findElement(By.name("moduleId"));
moduleId.sendKeys("Smart City");

WebElement functionId = driver.findElement(By.name("functionId"));
functionId.sendKeys(LinkName);



driver.findElement(By.xpath("//*[@id=\"command\"]/div[2]/button")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td[3])[2]")));
String id =driver.findElement(By.xpath("(//td[3])[2]")).getText();





wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt=\"Edit Details\"]")));
Thread.sleep(2000);
driver.findElement(By.cssSelector("img[alt=\"Edit Details\"]")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("entity.chekkerflag1")));
Thread.sleep(1000);
driver.findElement(By.id("entity.chekkerflag1")).click();



wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"button\"][value=\"Save and Continue\"]")));
driver.findElement(By.cssSelector("input[type=\"button\"][value=\"Save and Continue\"]")).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\"submit\"][value=\"Save Section\"]")));
driver.findElement(By.cssSelector("input[type=\"submit\"][value=\"Save Section\"]")).click();



wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnNo")));
driver.findElement(By.id("btnNo")).click();

Thread.sleep(1000);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")));
driver.findElement(By.xpath("//*[@id=\"text-resize\"]/div[1]/ol/li[1]/a")).click();



System.out.println(srno+"|"+LinkName+"|"+id);



	}
	}
}
