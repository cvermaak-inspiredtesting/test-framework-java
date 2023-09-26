package com.inspiredtesting.githubtests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import static org.openqa.selenium.TimeoutException.*;

public class TestGithub
{
    private static ChromeOptions options;
    private static WebDriver driver;
    private static String baseUrl = "https://github.com/";
    private static String userName = "cvermaak-inspiredtesting";
    private static String url = baseUrl + userName;

    @BeforeAll
    public static void setupDriver()
    {
        options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @BeforeEach
    public void testArrange()
    {
        driver.get(url);
        driver.manage().window().maximize();

        // driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testUserName()
    {
        String pageUserName = driver.findElement(By.className("p-nickname")).getText();

        Assertions.assertEquals(userName, pageUserName);
    }

    @Test
    public void testRepositoryLink()
    {
        String repoName = "test-framework-java";

        WebDriverWait wait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));

        WebElement repoLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(repoName)));
        repoLink.click();
        WebElement repoHeader = null;
        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class);
        try {
            repoHeader = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[class='AppHeader-context-item'][css='2']")));
        }
        catch (TimeoutException ex)
        {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("./capturedScreenshots/repoScreenCapture.png"));
                // FileUtils.copyFile(Paths.get(driver.getPageSource().toString()), new File("./capturedPageSource/repoScreenSource.html"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            File pageSource = driver.getPageSource(OutputType.FILE);
        } finally {
            repoHeader = (repoHeader != null) ? repoHeader : null;
        }

        String repoUrl = driver.getCurrentUrl();

        Assertions.assertEquals(repoName, repoHeader);
        Assertions.assertEquals(url + "/" + repoName, repoUrl);
    }

    @AfterAll
    public static void tearDown()
    {
        driver.quit();
    }
}
