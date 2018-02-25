package test_test;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/* class to open documentations */
public class docs_page extends elements {

	private WebDriver driver;

	private By searchHostRu = By.id("search_host_ru");
	private By butSearch = By.xpath("(//input[@value='Поиск'])[1]");
	private By searchResult = By.className("search_results");
	
	/* default constructor */
	public docs_page() {
		
	}
	
	/* constructor with parameter (must to use) */
	public docs_page(WebDriver dr) {
		driver = dr;
	}

	/* opening page */
	public boolean initPage() {
		driver.manage().window().maximize();
		driver.get("https://docs.gurtam.com/");
		
		if (isElementPresent(driver, searchHostRu)) {
			return true;
		}
		return false;
	}

	/* close opened page */
	public void closePages() {
		driver.close();
	}

	/* method to switch to window from other */
	public boolean switchPage(WebDriver parent) {
		Set<String> handles = parent.getWindowHandles();
		
		driver = parent.switchTo().window((String) handles.toArray()[handles.size() - 1]);
		if (!isElementPresent(driver, searchHostRu)) {
			return false;
		}
		return true;
	}
	
	/* finding GPS */
	public boolean step5() {
		if (!isElementPresent(driver, searchHostRu)) {
			return false;
		}

		driver.findElement(searchHostRu).sendKeys("GPS");
		driver.findElement(butSearch).click();
		
		if (!isElementPresent(driver, searchResult)) {
			return false;
		}
		return true;
	}
	
	/* check results */
	public boolean step6(boolean printLinks) {
		if (!isElementPresent(driver, searchResult)) {
			return false;
		}

		List<WebElement> wb = driver.findElement(searchResult).findElements(By.cssSelector("a"));
		System.out.println("Number of links in the results " + wb.size());
		if (wb.size() == 20) {
			System.out.println("Result OK");
		} else {
			System.out.println("Result Error");
		}
		
		if (printLinks) {
			for (WebElement webEl : wb) {
				System.out.println(webEl.getAttribute("href"));
			}
		}
		
		return true;
	}
}
