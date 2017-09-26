package com.elong.mt.llz.data.crawler.selenium;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class SeleniumDownloaderTest {

	private String chromeDriverPath = "/Users/user/DevelopTools/driver/chromedriver";

	@Ignore("need chrome driver")
    @Test
	public void test() {
		WebDriver driver = new PhantomJSDriver();
		driver.getCurrentUrl();

	}


}