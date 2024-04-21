package sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class ArcgisBhavanaTesting {


	public static WebDriver driver;
	public static Properties prop;
	
	//Test case to set chromedriver setup , loading config.properties file. This method run before any method as BeforeTest annotation is declared
	@BeforeTest
	public void setUp() throws IOException {
		
		//loading global environment variables from config.properties file
		prop=new Properties();
		FileInputStream ip=new FileInputStream("C:\\Users\\sivaj\\eclipse-workspace\\"
				+ "sample\\src\\test\\java\\config\\config.properties");
		prop.load(ip);

		//chrome driver setup
		 WebDriverManager.chromedriver().setup();
		// creating a driver object for chrome browser
		 driver = new ChromeDriver();
		//adding synchronization and defining global wait time
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}

	//Test case to login with credentials
	@Test(priority=0)
	public void Login(){
		
		//Navigating to the url 
		driver.get(prop.getProperty("url"));
		System.out.println(driver.getTitle());
		//click on SignIn
		driver.findElement(By.linkText("Sign In")).click();
		//sending the credentials username, password 
		String username=prop.getProperty("username");
		String password=prop.getProperty("password");
		
		//finding the webelements for username and password using Xpath and sending inputs
		driver.findElement(By.xpath("//input[@id='user_username']")).sendKeys(username);
		driver.findElement(By.xpath("//input[@id='user_password']")).sendKeys(password);
		
		//clicking on signIn Button
		driver.findElement(By.xpath("//button[@id='signIn']")).click();
		driver.manage().window().maximize();
	}
	
	
	//Test case to select a BaseMap from "https://www.arcgis.com/index.html"
	@Test(priority=1)
	public void BaseMap() throws InterruptedException{
		//clicking on Map from header option
		driver.findElement(By.xpath("//a[@id='esri-header-menus-link-desktop-0-3']")).click();
		//clicking on Map viewer classic
		driver.findElement(By.xpath("//a[@id='esri-header-menus-link-desktop-1-0']")).click();
		//clicking on basemap option
		driver.findElement(By.xpath("//span[@id='webmap-measure_label']")).click();
		driver.findElement(By.xpath("//span[@id='dijit_form_ToggleButton_1']")).click();
	}
	
	
	//Test case to measure tool and finding distance between 2 points
	@Test(priority=2)
	public void MeasureTool() throws InterruptedException{
		
		//using Actions class for mouse interactions
		Actions a=new Actions(driver);
		WebElement click=driver.findElement(By.xpath("//div[@id='map_container']"));
		
		//clicking on starting point
		a.moveToElement(click).click().build().perform();
		//clicking on ending point
        a.moveByOffset(30,60).click().build().perform();
        
		//displaying the distance
		String distance=driver.findElement(By.xpath("//div[@id='dijit_layout_ContentPane_4']")).getText();
		System.out.println("Distance determined by using Measure tool is "+distance);
		
		//using TestNG assertion for negative scenario
		Assert.assertNotNull(distance,"distance is not determined as selected ponits are incorrect");
		
		//closing the measure tool selection
		driver.findElement(By.xpath("//a[@id='measureClose']")).click();
	}
	
	
	//Test case to ZoomInZoomOut functionality
	@Test(priority=3)
	public void ZoomInZoomOut() throws InterruptedException{
		
		// using do while to click on zoom in multiple times
		int i=0;
		do {
		//clicking on zooming button
		driver.findElement(By.xpath("//div[@id='map_zoom_slider']/div[1]")).click();
		i++;
		}while(i<=2);
		
		//clicking on zoom out button
		driver.findElement(By.xpath("//div[@id='map_zoom_slider']/div[3]")).click();
		
	}
	
	
	//Test case to find address fucntionality
	@Test(priority=4)
	public void FindAddress() throws InterruptedException{
		
		//clicking on find address and sending an address
		WebElement findAddress=driver.findElement(By.xpath("//input[@id='geocoder_input']"));
		
		//taking the input from system
		Scanner scan1 = new Scanner(System.in);
		String input1 = scan1.nextLine();
		findAddress.sendKeys(input1);
		String input2 = findAddress.getAttribute("value");
		System.out.println(input2);
		
		//considering empty string is not allowed in search bar
		if (input2.isEmpty()) {
		    System.out.println("empty search is not possible");
		} 
		
		// Considering max length is 40 characters in search bar
		else if (input2.length()>40){
		    System.out.println("maximum length should be 40 characters ");	
		}
		
		//Valid Address Search
		else {
			driver.findElement(By.xpath("//div[@id='geocoder']/div[1]/div[2]")).click();
			System.out.println("succefully serached the address : "+ input2 );
		}
		
	}
	

}
