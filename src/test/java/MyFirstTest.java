
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.json.simple.parser.JSONParser;


public class MyFirstTest {

    private WebDriver driver;

    @Before
    public void setup(){
        driver = new ChromeDriver();
        driver.navigate().to("http://the-internet.herokuapp.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    /*
    @Test
    public void testOne() throws InterruptedException, ParseException, IOException {
        readWriteJSON();
    }
    */

    @Test
    public void testOne() throws InterruptedException, ParseException, IOException {
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();

        String title = driver.findElement(By.cssSelector(".example h2")).getText();
        assertThat(title, is("Secure Area"));
    }

    @After
    public void teardown(){
        driver.close();
    }

    public String login(String username, String password) throws InterruptedException {

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.className("radius")).click();

        String title = driver.findElement(By.cssSelector(".example h2")).getText();

        if(title.equals("Secure Area")){
            driver.findElement(By.className("radius")).click();
            assertThat(title, is("Secure Area"));
            return "User gültig";
        }

        return "User ungültig";
    }


    @SuppressWarnings("unchecked")
    public void readWriteJSON() throws InterruptedException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try  {
            FileReader reader = new FileReader("Input.json");
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray usersList = (JSONArray) obj;
            System.out.println(usersList); //This prints the entire json file
            for(int i=0;i<usersList.size();i++) {
                JSONObject users = (JSONObject) usersList.get(i);
                System.out.println(users);//This prints every block - one json object
                JSONObject user = (JSONObject) users.get("users");
                System.out.println(user); //This prints each data in the block
                String username = (String) user.get("username");
                String password = (String) user.get("password");
                String result = login(username,password);
                user.put("result", result);
                //Write JSON file
                try (FileWriter file = new FileWriter("Output.json")) {
                    file.append(usersList.toJSONString());
                    file.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(user);
            }
        } catch (FileNotFoundException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

}

