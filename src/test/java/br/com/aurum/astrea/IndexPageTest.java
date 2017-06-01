package br.com.aurum.astrea;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import io.github.bonigarcia.wdm.PhantomJsDriverManager;

public class IndexPageTest {
	
	private WebDriver driver;
	
//	private final String baseUrl = "http://www.google.com/";
	private final String baseUrl = "http://www.google.com";
//	private final static String baseUrl = "http://localhost";
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
		
		PhantomJsDriverManager.getInstance().setup();
//		driver = new PhantomJSDriver();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		driver.get(baseUrl);
	}
	
	@Before
	public void setUp() {
//		ArrayList<String> cliArgsCap = new ArrayList<String>();
//		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
//		cliArgsCap.add("--web-security=false");
//		cliArgsCap.add("--ssl-protocol=any");
//		cliArgsCap.add("--ignore-ssl-errors=true");
//		capabilities.setCapability("takesScreenshot", true);
//		capabilities.setCapability(
//		    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
//		capabilities.setCapability(
//		    PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
//		        new String[] { "--logLevel=2" });
//		
//		capabilities.setJavascriptEnabled(true);   
//		this.driver = new PhantomJSDriver(capabilities);
	
//		
		System.err.println("entrou no teste1");
		this.driver = new PhantomJSDriver();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
		System.err.println("entrou no teste2");
	}
	
	@After
	public void teardown() {
		if (this.driver != null) {
			this.driver.quit();
		}
	}
	
	@Test
	public void verifyContactsLinkExists() {
		System.err.println("entrou no teste3");
//		this.driver.get(this.baseUrl);
//		this.driver.navigate().to(this.baseUrl);
		
//		WebDriverWait wait = new WebDriverWait(this.driver, 10);  // 10 secs max wait
//		wait.until(ExpectedConditions.presenceOfElementLocated( By.name("btnK") )); 
//		
//		WebElement webElement = this.driver.findElement(By.name("btnK"));
//		assertTrue(webElement != null);
		
		
//		WebElement webElement = driver.findElement(By.name("contatos"));
//		assertTrue(webElement != null);
	}
}