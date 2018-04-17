package com.dotdash.testbase;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.dotdash.util.TestUtil;
import com.dotdash.util.WebEventListener;

//This is base class it will read property file and initialize the Selenium webdriver

public class TestBase {
	public static Properties prop;
	public static WebDriver driver;
	public static WebEventListener eventListener;
	public static EventFiringWebDriver e_driver;
	public static int REPSONSE_STATUS_CODE_200=200;
	public static void intialize() {
		
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\com\\dotdash\\config\\config.properties");
			
			prop = new Properties();
			prop.load(fis);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\Meena.Somasundaram\\Downloads\\chromedriver_win32\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		if (browserName == "firefox") {
			System.setProperty("webdriver.gecko.driver",
					"C:\\Users\\Meena.Somasundaram\\Downloads\\geckodriver-v0.20.0-win64\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
		e_driver = new EventFiringWebDriver(driver);
		// Now create object of EventListerHandler to register it with
		// EventFiringWebDriver
		eventListener = new WebEventListener();
		e_driver.register(eventListener);
		driver = e_driver;
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
	}
	
}
