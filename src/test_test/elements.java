package test_test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/* class to check presents element on the page */
public class elements {
	public boolean isElementPresent(WebDriver driver, By locator) {
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		List<WebElement> list = driver.findElements(locator);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if (list.size() == 0) {
			return false;
		} else {
			return list.get(0).isDisplayed();
		}
	}
	
}
