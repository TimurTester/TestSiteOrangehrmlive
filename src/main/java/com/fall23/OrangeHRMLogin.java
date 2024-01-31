package com.fall23;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class OrangeHRMLogin {
    WebDriver driver = new ChromeDriver();

    @BeforeClass(description = "Open and configures the browser")
    public void launchBrowser() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @Test(description = "Login with empty field username and password",
            dataProvider = "getEmptyLoginAndPassword", priority = 1)
    void loginWithEmptyLoginAndPasswordTest(String username, String password) {

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        WebElement usernameInputField = driver.findElement(By.name("username"));
        WebElement passwordInputField = driver.findElement(By.name("password"));

        usernameInputField.sendKeys(username);
        passwordInputField.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.tagName("button"));
        loginBtn.click();

        WebElement warningWindow = driver.findElement(By.tagName("span"));

        String actualWarningEmptyField = warningWindow.getText();
        String expectedWarningEmptyField = "Required";

        Assert.assertEquals(actualWarningEmptyField,expectedWarningEmptyField);

        driver.navigate().refresh();
    }

    @DataProvider(name="getEmptyLoginAndPassword")
    public Object[][] getEmptyLoginAndPassword(){
        return new Object[][]{
                {"", ""},
                {"", "1"},
                {"admin", ""},
        };
    }

    @Test(description = "Message output 'Invalid credentials', if login incorrect", priority = 2)
    void incorrectPasswordMessageTest() {
        WebElement usernameInputField = driver.findElement(By.name("username"));
        WebElement passwordInputField = driver.findElement(By.name("password"));

        usernameInputField.sendKeys("admin");
        passwordInputField.sendKeys("1234");

        WebElement loginBtn = driver.findElement(By.tagName("button"));
        loginBtn.click();

        WebElement warningWindow = driver.findElement(By.tagName("p"));

        String actualWarningInvalidLogin = warningWindow.getText();
        String expectedWarningInvalidLogin = "Invalid credentials";

        Assert.assertEquals(actualWarningInvalidLogin,expectedWarningInvalidLogin);
    }

    @Test(description = "Login with invalid username and password",
                        dataProvider = "getInvalidLoginAndPassword", priority = 3)
    void loginWithInvalidUsernameAndPasswordTest(String username, String password) {
        WebElement usernameInputField = driver.findElement(By.name("username"));
        WebElement passwordInputField = driver.findElement(By.name("password"));

        usernameInputField.sendKeys(username);
        passwordInputField.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.tagName("button"));
        loginBtn.click();

        String actualUrlSite = driver.getCurrentUrl();
        String expectedUrlSite = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
        Assert.assertNotEquals(actualUrlSite, expectedUrlSite);
    }

    @DataProvider(name="getInvalidLoginAndPassword")
    public Object[][] getInvalidLoginAndPassword(){
        return new Object[][]{
                {"admin", "123"},
                {"admin", "12"},
                {"admin", "1"},
                {"admi", "admin123"},
        };
    }

    @Test(description = "Login with valid username and password", priority = 4)
    void loginWithValidUsernameAndPasswordTest() {
        WebElement usernameInputField = driver.findElement(By.name("username"));
        WebElement passwordInputField = driver.findElement(By.name("password"));

        String username = "admin";
        String password = "admin123";

        usernameInputField.sendKeys(username);
        passwordInputField.sendKeys(password);

        WebElement loginBtn = driver.findElement(By.tagName("button"));
        loginBtn.click();

        String actualUrlSite = driver.getCurrentUrl();
        String expectedUrlSite = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
        Assert.assertEquals(actualUrlSite,expectedUrlSite);
    }

    @AfterClass
    public void closeUp() {
        driver.close();
    }
}
