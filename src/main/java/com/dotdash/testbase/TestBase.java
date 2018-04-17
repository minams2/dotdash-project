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



public class TestBase {
	public static Properties prop;
	public static WebDriver driver;
	public static WebEventListener eventListener;
	public static EventFiringWebDriver e_driver;
	public static void intialize() {
		
		try {
			File src = new File(
					"C:\\Users\\Meena.Somasundaram\\eclipse-workspace\\Cenlar\\src\\main\\java\\com\\cenlar\\qa\\config\\config.properties");
			FileInputStream fis = new FileInputStream(src);
			prop = new Properties();
			prop.load(fis);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String browserName = prop.getProperty("browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\\\Users\\\\Meena.Somasundaram\\\\Downloads\\\\chromedriver_win32\\\\chromedriver.exe");
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
