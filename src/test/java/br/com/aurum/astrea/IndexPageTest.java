package br.com.aurum.astrea;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IndexPageTest {
	
	private static WebDriver driver;
	
	private final static String baseUrl = "http://localhost:3000";
//	private static final String driverPath = "D:\\Arquivos de Programas\\phantomjs-2.1.1\\bin\\phantomjs.exe";
	
	@BeforeClass
	public static void init() {
//		System.setProperty("webdriver.chrome.driver", "D:\\Arquivos de Programas\\chromedriver\\chromedriver.exe");
//		driver = new ChromeDriver();

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);                
//		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);
//		driver = new PhantomJSDriver(caps);
		
//		PhantomJsDriverManager.getInstance().setup();
		
//		PhantomJsDriverManager.getInstance().setup();
//		driver = new PhantomJSDriver();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		driver.get(baseUrl);
	}
	
	@Test
	public void verifyContactsLinkExists() {
//		WebElement webElement = driver.findElement(By.name("contatos"));
//		assertTrue(webElement != null);
	}
}