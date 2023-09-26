package com.inspiredtesting.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class TestBasics
{
    private static ChromeOptions options;
    private static WebDriver driver;
    @BeforeAll
    public static void setupDriver()
    {
        options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @BeforeEach
    public void repeatSetup()
    {
        driver.get("https://demo.guru99.com/test/newtours/");
        driver.manage().window().fullscreen();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void validateTitle() {
        Assertions.assertEquals("Welcome: Mercury Tours", driver.getTitle());
    }

    @AfterAll
    public static void cleanUp()
    {
        driver.quit();
    }

    private static int sum(int a, int b)
    {
        return  a + b;
    }
}
