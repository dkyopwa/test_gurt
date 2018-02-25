package test_test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class start_browser {

	private WebDriver driver;
	private String currBrowserName;
	private String currDriverPath;
	
	private static void write(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int len;
	    while ((len = in.read(buffer)) >= 0)
		    out.write(buffer, 0, len);
	    out.close();
	    in.close();
	}
	
	private String loadDriver() {
		URL driverFile = null;
		String os = System.getProperty("sun.desktop");
		String archModel = System.getProperty("sun.arch.data.model");
		String fileArchive = "";
		String fileDriver = "";
		
		if (!currDriverPath.isEmpty()) {
			return currDriverPath;
		}
		
		switch (currBrowserName) {
		case "chrome":
			if (currDriverPath.isEmpty()) {
				if (os.contains("windows")) {
					try {
						driverFile = new URL("http://chromedriver.storage.googleapis.com/2.33/chromedriver_win32.zip");
						fileArchive = "chromedriver_win32.zip";
						fileDriver = "chromedriver.exe";
					} catch (MalformedURLException e) {
						System.out.println("Can not open URL with browser driver");
						return null;
					}
				} else {
					// Linux x64
					try {
						driverFile = new URL("http://chromedriver.storage.googleapis.com/2.33/chromedriver_linux64.zip");
						fileArchive = "chromedriver_linux64.zip";
						fileDriver = "chromedriver";
					} catch (MalformedURLException e) {
						System.out.println("Can not open URL with browser driver");
						return null;
					}
				}
			}
			break;
		case "firefox":
			if (currDriverPath.isEmpty()) {
				if (os.contains("windows")) {
					String strURL = "https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-win" + archModel + ".zip";
					try {
						driverFile = new URL(strURL);
						fileArchive = "geckodriver-v0.19.1-win" + archModel + ".zip";
						fileDriver = "geckodriver.exe";
					} catch (MalformedURLException e) {
						System.out.println("Can not open URL with browser driver");
						return null;
					}
				} else {
					// Linux
					String strURL = "https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux" + archModel + ".tar.gz";
					try {
						driverFile = new URL(strURL);
						fileArchive = "geckodriver-v0.19.1-linux" + archModel + ".tar.gz";
						fileDriver = "geckodriver";
					} catch (MalformedURLException e) {
						System.out.println("Can not open URL with browser driver");
						return null;
					}
				}
			}
			break;
		default:
            throw new IllegalArgumentException("Invalid load browser '" + currBrowserName + "'");
		}
		
		ReadableByteChannel rbc;
		try {
			rbc = Channels.newChannel(driverFile.openStream());
		} catch (IOException e) {
			System.out.println("Can not open URL with browser driver");
			return null;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileArchive);
		} catch (FileNotFoundException e) {
			System.out.println("Can not create file driver");
			return null;
		}
		try {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			System.out.println("Can not load file driver");
			try {
				fos.close();
			} catch (IOException e1) {
			}
			return null;
		}
		try {
			fos.close();
		} catch (IOException e) {
			System.out.println("Can not load file driver");
			return null;
		}
		
		// zip
		File file = new File(fileDriver);
		ZipFile zip = null;
		try {
			zip = new ZipFile(fileArchive);
		} catch (IOException e) {
			System.out.println("Error opening zip file");
			try {
				if (zip != null)
					zip.close();
			} catch (IOException e1) {
				System.out.println("Error opening zip file");
			}
			return null;
		}
        Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            System.out.println(entry.getName());

            if (entry.isDirectory()) {
                new File(file.getParent(), entry.getName()).mkdirs();
            } else {
                try {
					write(zip.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(new File(file.getParent(), entry.getName()))));
				} catch (Exception e) {
					System.out.println("Error unpacking zip file");
					try {
						zip.close();
					} catch (IOException e1) {
						System.out.println("Error unpacking zip file");
					}
					return null;
				}
            }
        }
        try {
			zip.close();
		} catch (IOException e) {
		}

		return fileDriver;
	}

	public start_browser(String browserName, String driverPath) {
		currBrowserName = browserName;
		currDriverPath = driverPath;
		
		if (driverPath.isEmpty()) {
			driverPath = loadDriver();
			if (driverPath.isEmpty()) {
				System.out.println("Error loading driver");
				return;
			}
		}

		switch (browserName) {
		case "chrome":			
			System.setProperty("webdriver.chrome.driver", driverPath);
			driver = new ChromeDriver();
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", driverPath);
			driver = new FirefoxDriver();
			break;
		default:
            throw new IllegalArgumentException("Invalid name of browser '" + browserName + "'");
		}
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public String getBrowserName() {
		return currBrowserName;
	}
	
	public String getDriverPath() {
		return currDriverPath;
	}
}
