package br.com.aurum.astrea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.PhantomJsDriverManager;

public class IndexPageTest {
	
	private WebDriver driver;
	
//	private final String baseUrl = "http://www.google.com";
	private final String baseUrl = "http://localhost:8080";
//	private static final String driverPath = "D:\\Arquivos de Programas\\phantomjs-2.1.1\\bin\\phantomjs.exe";
	
	@BeforeClass
	public static void init() {
//		System.setProperty("webdriver.chrome.driver", "D:\\Arquivos de Programas\\chromedriver\\chromedriver.exe");
//		driver = new ChromeDriver();
		
//		ChromeDriverManager.getInstance().setup();
//		driver = new ChromeDriver();

//		DesiredCapabilities caps = new DesiredCapabilities();
//		caps.setJavascriptEnabled(true);                
//		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);
//		driver = new PhantomJSDriver(caps);
		
//		PhantomJsDriverManager.getInstance().setup();
		PhantomJsDriverManager.getInstance().setup("phantomjs-2.1.1");
		
//		PhantomJsDriverManager.getInstance().useTaobaoMirror().setup();
//		driver = new PhantomJSDriver();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		driver.get(baseUrl);
	}
	
	@Before
	public void setUp() {
//		File phantomjs = Phanbedder.unpack(); //Phanbedder to the rescue!
//		DesiredCapabilities dcaps = new DesiredCapabilities();
//		dcaps.setJavascriptEnabled(true);   
//		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
//		
//		this.driver = new PhantomJSDriver(dcaps);
//		System.err.println("\n\nDRIVER: " + phantomjs.getAbsolutePath() + "\n\n");
		
		
		List<String> cliArgsCap = new ArrayList<String>();
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability(
		    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		capabilities.setCapability(
		    PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
		        new String[] { "--logLevel=2" });
		
		capabilities.setJavascriptEnabled(true);   
//		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
		
//		this.driver = new PhantomJSDriver(capabilities);
	
//		
		this.driver = new PhantomJSDriver();
//		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.MINUTES);
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
	}
	
	@After
	public void teardown() {
		if (this.driver != null) {
			this.driver.quit();
		}
	}
	
	@Test
	public void checksIfIndexPageHasLoaded() {
		this.driver.get(this.baseUrl);
//		this.driver.navigate().to(this.baseUrl);
		
		WebDriverWait wait = new WebDriverWait(this.driver, 10);  // 10 secs max wait
//		wait.until(ExpectedConditions.presenceOfElementLocated( By.name("teste") )); 
		
		System.err.println("\n\nSOURCE:\n\n" + this.driver.getPageSource() + "\n\n");
//		
		String title = "Avalia Candidatos";
//		assertEquals(title, this.driver.getTitle());
		
//		WebElement webElement = this.driver.findElement(By.name("contatos"));
//		assertTrue(webElement != null);
	}
}