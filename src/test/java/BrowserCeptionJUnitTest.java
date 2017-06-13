/**
 * Created by haroonnaderi on 6/12/17.
 */

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import com.browserstack.local.Local;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parallelized.class)
public class BrowserCeptionJUnitTest {


    RemoteWebDriver driver;
    WebDriverWait webDriverWait;
    private Local l;

    private static JSONObject config;

    @Parameter(value = 0)
    public int taskID;

    @Parameters
    public static Iterable<? extends Object> data() throws Exception {
        List<Integer> taskIDs = new ArrayList<Integer>();

        if(System.getProperty("config") != null) {
            JSONParser parser = new JSONParser();
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + System.getProperty("config")));

            int envs = ((JSONArray)config.get("environments")).size();

            for(int i=0; i<envs; i++) {
                taskIDs.add(i);
            }
        }

        return taskIDs;
    }

    @Before
    public void setUpTest() throws Exception{

        System.out.println("setting up test");

        JSONArray envs = (JSONArray) config.get("environments");

        DesiredCapabilities capabilities = new DesiredCapabilities();

        Map<String, String> envCapabilities = (Map<String, String>) envs.get(taskID);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
        }

        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(capabilities.getCapability(pair.getKey().toString()) == null){
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }
        String username = System.getenv("bsuser");
        if(username == null) {
            username = (String) config.get("user");
        }

        String accessKey = System.getenv("bskey");
        if(accessKey == null) {
            accessKey = (String) config.get("key");
        }

        if(capabilities.getCapability("browserstack.local") != null && capabilities.getCapability("browserstack.local") == "true"){
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", accessKey);
            l.start(options);
        }

        driver = new RemoteWebDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);

        webDriverWait = new WebDriverWait(driver, 5);

    }


    @After
    public void TearDown(){
        System.out.println("Test is completed");
        driver.close();
        driver.quit();

    }





}
