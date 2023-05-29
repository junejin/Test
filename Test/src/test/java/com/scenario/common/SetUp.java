package com.scenario.common;

import java.time.Duration;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SetUp {

	public WebDriver driver;
	public DesiredCapabilities caps;

	@BeforeSuite
	 public void beferoSuite() throws Exception {
		
	 }
	
	@BeforeClass
	@Parameters({ "platform" })
	public void setup(String platform) throws Exception {
		driver = this.setUpDriver(platform);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get("https://haechi-labs.github.io/face-sample-dapp/");
	}
	@AfterClass
	public void tearDownClass() {
		driver.quit();
	}

	WebDriver setUpDriver(String platform) throws Exception {
		caps = new DesiredCapabilities();
		if (platform.equalsIgnoreCase("chrome_PCWeb")) {

			WebDriverManager.chromedriver().setup();
			caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
			caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANY);
			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("window-size=1800,1300");
//			chromeOptions.addArguments("user-data-dir=/Users/ethan/Library/Application Support/Google/Chrome");
//			chromeOptions.addArguments("headless");
//			chromeOptions.addArguments("no-sandbox");
//			chromeOptions.addArguments("single-process");
//			chromeOptions.addArguments("disable-dev-shm-usage");
			chromeOptions.merge(caps);
			driver = new ChromeDriver(chromeOptions);
		} 
		else if (platform.equalsIgnoreCase("safari_PCWeb")) {
			WebDriverManager.safaridriver().setup();
			caps.setCapability(CapabilityType.BROWSER_NAME, "safari");
			caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);
			driver = new SafariDriver();
		 }
		else if (platform.equalsIgnoreCase("firefox_PCWeb")) {
		WebDriverManager.firefoxdriver().setup();
		caps.setCapability(CapabilityType.BROWSER_NAME, "firefox");
		FirefoxOptions ffOptions = new FirefoxOptions();
		ffOptions.merge(caps);
		ffOptions.setLogLevel(FirefoxDriverLogLevel.FATAL);
		driver = new FirefoxDriver(ffOptions);
		driver.manage().window().maximize();
	} 
//		else if (platform.equalsIgnoreCase("android")) {
//			caps.setCapability("deviceName", "Galaxy S10");
//			caps.setCapability("platformName", "Android");
//			caps.setCapability("platformVersion", "10.0");
//			caps.setCapability("skipUnlock", "true");
//			caps.setCapability("udid", "R39M3036Q2P");
////			caps.setCapability("udid", "emulator-5554");
//			caps.setCapability("appPackage", "com.gworks.oneapp.works.stage");
//			caps.setCapability("appActivity", "com.gworks.oneapp.entry.EntryActivity");
//			caps.setCapability("noReset", "true");
//			driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);
//
//		} 
//		else if (platform.equalsIgnoreCase("PCApp")) {
//			caps.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
//			caps.setCapability("platformVersion", "10");
//			caps.setCapability("deviceName", "WindowsPC");
//			caps.setCapability("automationName", "Windows");// Or Mac
//			caps.setCapability("app", "Microsoft.BingWeather_8wekyb3d8bbwe!App");
//			caps.setCapability("app", "C:\\Users\\NAVER\\AppData\\Roaming\\WorksMobile\\WorksMobileOneW\\WMOne.exe");
//
//			driver = new WindowsDriver<WindowsElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
//		}

		return driver;
	}

}
