package automationFrameWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestClass {

	public static void main(String[] args) throws InterruptedException, IOException, ParseException {
		// TODO Auto-generated method stub
		
		System.setProperty("webdriver.chrome.driver",
		           "C:\\Users\\suraj\\Downloads\\Compressed\\chromedriver.exe");
		
		//Create a map to store  preferences 
		Map<String, Object> prefs = new HashMap<String, Object>();

		//add key and value to map as follow to switch off browser notification
		//Pass the argument 1 to allow and 2 to block
		prefs.put("profile.default_content_setting_values.notifications", 2);

		//Create an instance of ChromeOptions 
		ChromeOptions options = new ChromeOptions();

		// set ExperimentalOption - prefs 
		options.setExperimentalOption("prefs", prefs);
		
		
		WebDriver driver = new ChromeDriver(options);
		
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.redbus.in");	
		driver.manage().window().maximize();
		 
					
		File file = new File("D:\\Workplace_Eclipse\\RedBusAutomation\\TestData\\TestData.xlsx");
		FileInputStream fis = new FileInputStream(file);
		Workbook wb = new XSSFWorkbook(fis);
		Sheet sheet = wb.getSheet("Sheet1");
		
		String src = sheet.getRow(1).getCell(0).toString();
		driver.findElement(By.xpath("//input[@id='src']")).sendKeys(src);
		driver.findElement(By.xpath("//ul[@class='autoFill']/li[1]")).click();
		
		String dest = sheet.getRow(1).getCell(1).toString();
		driver.findElement(By.xpath("//input[@id='dest']")).sendKeys(dest);
		driver.findElement(By.xpath("//ul[@class='autoFill']/li[1]")).click();
		driver.findElement(By.xpath("//label[@for='onward_cal']")).click();
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		Date onwardDate1 = sdf1.parse(sheet.getRow(1).getCell(2).getStringCellValue());
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		String onwardDate = sdf.format(onwardDate1);
		String monthYear = onwardDate.toString().substring(onwardDate.toString().indexOf(" ")+1);
		String dateValue = onwardDate.toString().substring(0,2);
		String currMonthYear = driver.findElement(By.xpath("//td[@class='monthTitle']")).getAttribute("innerHTML");
		if(currMonthYear.equalsIgnoreCase(monthYear)) {
			driver.findElement(By.xpath("//*[@id='rb-calendar_onward_cal']//*[text()='"+dateValue+"']")).click();
		} else {
			driver.findElement(By.xpath("//*[@id='rb-calendar_onward_cal']//*[@class='next']//button")).click();
			driver.findElement(By.xpath("//*[@id='rb-calendar_onward_cal']//*[text()='"+dateValue+"']")).click();
		}
		driver.findElement(By.xpath("//*[@id='search_btn']")).click();
		driver.findElement(By.xpath("//*[text()='View Seats']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Click on an Available seat to proceed with your transaction.']"))));
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath("//canvas")),0,0).moveByOffset(105,30).click().build().perform();
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File("D:\\Workplace_Eclipse\\RedBusAutomation\\ScreenShots\\booking.png"));
		wb.close();
		driver.quit();
	}

}
