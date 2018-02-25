package test_test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/* class to open page gurtam.com */
public class gurtam_page extends elements {

	private WebDriver driver;
		
	private By lang = By.id("w0");
	private By select_lang = By.id("w1");
	private By navbar = By.id("navbar");
	private By en_lang = By.linkText("EN");
	private By community = By.linkText("COMMUNITY");
	private By helpCenter = By.linkText("Help center");
	private By techDoc = By.linkText("Technical documentation");
	
	/* default constructor */
	public gurtam_page() {
		
	}
	
	/* constructor with parameters (must to use) */
	public gurtam_page(WebDriver dr, String browserName) {
		driver = dr;
		switch (browserName) {
		case "chrome":
			break;
		case "firefox":
			community = By.linkText("Community");
			break;
		default:

		}
	}
	
	/* opening page */
	public boolean initPage() {
		driver.manage().window().maximize();
		driver.get("https://gurtam.com/");
		
		if (isElementPresent(driver, lang) && isElementPresent(driver, navbar)) {
			return true;
		}
		return false;
	}
	
	/* close all pages */
	public void closePages() {
		driver.quit();
	}
	
	/* check for language and chose en (if needed) */
	public boolean setLangEn() {
		if (!isElementPresent(driver, lang)) {
			return false;
		}
		WebElement wbLang = driver.findElement(lang);
		String l = wbLang.getText();
		if (!l.matches("EN")) {
			wbLang.click();
			
			if (isElementPresent(driver, select_lang)) {
				driver.findElement(en_lang).click();
				
				if (!isElementPresent(driver, lang)) {
					return false;
				}
				wbLang = driver.findElement(lang);
				l = wbLang.getText();
				if (l.matches("EN")) {
					return true;
				}
			} 
			return false;
		} else {
			return true;
		}
	}
	
	/* check Community */
	public boolean step2() {
		if (!isElementPresent(driver, community)) {
			return false;
		}
		driver.findElement(community).click();
		
		if (!isElementPresent(driver, helpCenter)) {
			return false;
		}
		
		return true;
	}
	
	/* check Help Center */
	public boolean step3() {
		if (!isElementPresent(driver, helpCenter)) {
			return false;
		}
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(helpCenter)).build().perform();
		
		if (!isElementPresent(driver, techDoc)) {
			return false;
		}
		
		return true;		
	}
	
	/* opening documentation from link Help Center */
	public boolean step4() {
		if (!isElementPresent(driver, techDoc)) {
			return false;
		}
		WebElement wb = driver.findElement(techDoc);
		wb.click();
		System.out.println(wb.getAttribute("href"));
		
		return true;
	}
	
	/* general method to get current driver */
	public WebDriver getDriver() {
		return driver;
	}
}
