package parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Parser {

    public void readPage(String keyphrase) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String baseUrl = "https://www.olx.kz/";

        // launch chromedriver and direct it to the Base URL
        driver.get(baseUrl);

        // get the actual value of the title
        String actualTitle = driver.getTitle();
        WebElement inputSearchBox = driver.findElement(By.xpath("//*[@id=\"headerSearch\"]"));
        inputSearchBox.sendKeys(keyphrase);
        WebElement searchLaunchButton = driver.findElement(By.xpath("//*[@id=\"searchmain\"]/div/fieldset/span"));
        searchLaunchButton.click();

        System.out.println("button pushed");

        Thread.sleep(4000);
        driver.close();
    }

}
