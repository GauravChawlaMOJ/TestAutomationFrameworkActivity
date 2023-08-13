package tests;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set up WebDriver before each test
        System.setProperty("webdriver.chrome.driver", "/Users/gauravchawla/Downloads/chromedriver-mac-x64/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://login.live.com");
    }

    @After
    public void tearDown() {
        // Clean up WebDriver after each test
        driver.quit();
    }

    @Test
    public void testValidLogin() {
        driver.findElement(By.id("i0116")).sendKeys("gaurav-chawla@hotmail.com");
        //driver.findElement(By.id("password")).sendKeys("valid_password");
        driver.findElement(By.id("idSIButton9")).click();

        // Assert that the user is redirected to the expected page
        assertEquals("https://login.live.com/", driver.getCurrentUrl());
    }

    @Test
    public void testValidLogin2() {
        driver.findElement(By.id("i0116")).sendKeys("gaurav-chawla@hotmail.com");
        //driver.findElement(By.id("password")).sendKeys("valid_password");
        driver.findElement(By.id("idSIButton9")).click();

        // Assert that the user is redirected to the expected page
        assertEquals("https://login.live.com/", driver.getCurrentUrl());
        driver.findElement(By.id("i0118")).sendKeys("Schneider123@");
        driver.findElement(By.id("idSIButton9")).click();
        String s = driver.getPageSource();
        assertTrue(driver.getPageSource().contains("Gaurav Chawla"));
    }

    @Test
    public void testInvalidLogin() {
        driver.findElement(By.id("i0116")).sendKeys("gaurav-chawlahotmail.com");
        //driver.findElement(By.id("password")).sendKeys("invalid_password");
        driver.findElement(By.id("idSIButton9")).click();
        assertTrue(driver.getPageSource().contains("usernameError"));
        // Assert that an error message is displayed
    }

    @Test
    public void testEmptyUsernameLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("", "valid_password");
        assertFalse(result);
    }

    @Test
    public void testEmptyPasswordLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("valid_username", "");
        assertFalse(result);
    }

    @Test
    public void testEmptyCredentialsLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("", "");
        assertFalse(result);
    }

    @Test
    public void testPasswordEncoding() {
        PasswordEncoder encoder = new PasswordEncoder();
        String hashedPassword = encoder.encode("valid_password");
        assertTrue(encoder.verify("valid_password", hashedPassword));
    }

    @Test
    public void testAccountLockout() {
        LoginServiceTest loginService = new LoginServiceTest();
        for (int i = 0; i < LoginServiceTest.MAX_LOGIN_ATTEMPTS; i++) {
            boolean result = loginService.login("valid_username", "invalid_password");
            assertFalse(result);
        }
        boolean result = loginService.login("valid_username", "valid_password");
        assertFalse(result);  // Account should be locked out
    }

    @Test
    public void testDisabledAccountLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("disabled_username", "valid_password");
        assertFalse(result);  // Account is disabled
    }

    @Test
    public void testLockedAccountLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("locked_username", "valid_password");
        assertFalse(result);  // Account is locked
    }

    @Test
    public void testInactiveAccountLogin() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean result = loginService.login("inactive_username", "valid_password");
        assertFalse(result);  // Account is inactive
    }

    @Test
    public void testSessionManagement() {
        LoginServiceTest loginService = new LoginServiceTest();
        boolean loginResult = loginService.login("valid_username", "valid_password");
        assertTrue(loginResult);

        boolean accessResult = loginService.accessProtectedResource();
        assertTrue(accessResult);
    }
}