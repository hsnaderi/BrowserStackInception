/**
 * Created by haroonnaderi on 6/8/17.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class InceptionTest {


    WebDriver driver;
    public static final String USERNAME = System.getenv("BSUSERNAME");
    public static final String AUTOMATE_KEY = System.getenv("BSKEY");
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";


    public static void main(String[] args) throws Exception{


        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", "Opera");
//        caps.setCapability("browser_version", "9.0");
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("build", "Browserception");
        caps.setCapability("project", "tech onboarding 1");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        WebDriverWait wait = new WebDriverWait(driver, 15);



        driver.get("http://www.browserstack.com");

        try { driver.findElement(By.xpath("//a[contains(@href, '/users/sign_in')]")).click();}
        catch (ElementNotVisibleException e){
            driver.findElement(By.id("primary-menu-toggle")).click();
            driver.findElement(By.xpath("//a[contains(@href, '/users/sign_in')]")).click();
        }

        Thread.sleep(5000);
        driver.findElement(By.id("user_email_login")).sendKeys(System.getenv("BROWSERSTACKUSERNAME"));
        driver.findElement(By.id("user_password")).sendKeys(System.getenv("BROWSERSTACKPASSWORD"));
        driver.findElement(By.id("user_submit")).click();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("installExtension")));
        Thread.sleep(10000);


        try { driver.findElement(By.id("skip-local-installation")).click(); }
        catch (NoSuchElementException e)
        { }
        driver.findElement(By.className("rf-win10-os-ico")).click();
        driver.findElement(By.xpath("//li[contains(@data-named-version, 'win10_chrome_latest')]")).click();
        Thread.sleep(25000);


        driver.switchTo().activeElement().sendKeys("BrowserStack");
        driver.switchTo().activeElement().sendKeys(Keys.RETURN);
        Thread.sleep(2000);

        driver.close();
        driver.quit();



    }
}
