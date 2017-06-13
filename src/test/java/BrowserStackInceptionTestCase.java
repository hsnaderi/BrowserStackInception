import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by haroonnaderi on 6/12/17.
 */


public class BrowserStackInceptionTestCase extends BrowserCeptionJUnitTest {

    @Test
    public void TestCase() throws Exception{
        try {
            driver.get("http://www.browserstack.com");


            try { driver.findElement(By.xpath("//a[contains(@href, '/users/sign_in')]")).click();}
            catch (ElementNotVisibleException e){
                driver.findElement(By.id("primary-menu-toggle")).click();
                driver.findElement(By.xpath("//a[contains(@href, '/users/sign_in')]")).click();
            }

            Thread.sleep(5000);

            JSONParser parser = new JSONParser();
            JSONObject settings = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + System.getProperty("config")));
            driver.findElement(By.id("user_email_login")).sendKeys(settings.get("bsdcuser").toString());
            driver.findElement(By.id("user_password")).sendKeys(settings.get("bsdcpw").toString());
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

        }


        catch (Exception e){
            String sessionId = driver.getSessionId().toString();
            URI uri = new URI("https://haroonnaderi1:D2VVGCgUDXDjmmvT2z6X@www.browserstack.com/automate/sessions/"+sessionId+".json");
            HttpPut putRequest = new HttpPut(uri);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add((new BasicNameValuePair("status", "failed")));
            nameValuePairs.add((new BasicNameValuePair("reason", e.getMessage())));
            putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpClientBuilder.create().build().execute(putRequest);

        }
    }


}
