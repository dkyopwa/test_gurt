package test_test;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class test1 {
	
	private gurtam_page gp;
	private docs_page dp;
	
	@Parameters({ "browser", "driver_path" })
	@BeforeTest
	public void start(@Optional("chrome") String browserName, @Optional("") String driverPath) {
		start_browser brows = new start_browser(browserName, driverPath);
		gp = new gurtam_page(brows.getDriver(), brows.getBrowserName());
	}

	@Test(priority = 10)
	public void testA() {
		Assert.assertTrue(gp.initPage(), "Something went wrong with browser");
		Assert.assertTrue(gp.setLangEn(), "Wrong language");
		Assert.assertTrue(gp.step2(), "Didn't find elements 'Community' or 'Help Center'");
		Assert.assertTrue(gp.step3(), "FAILED: Didn't find elements 'Technical documentation'");
		Assert.assertTrue(gp.step4(), "FAILED");
		System.out.println("Done A");
	}
	
	@Test(priority = 20)
	public void testB() {
		if (gp == null) {
			System.out.println("FAILED: didn't open first page");
			Assert.fail("FAILED: testB");
		}
		dp = new docs_page();
		Assert.assertTrue(dp.switchPage(gp.getDriver()), "Error to switching to the second page");
		
		Assert.assertTrue(dp.step5(), "FAILED: Searching error");
		Assert.assertTrue(dp.step6(false), "FAILED: Results error");
		System.out.println("Done B");
	}
	
	@AfterTest
	public void afterTest() {
		gp.closePages();
	}
}

