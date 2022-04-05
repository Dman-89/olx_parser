package parser;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Parser {

//    public WebDriver driver = new ChromeDriver();

    public void readPageAndSearchBy(String keyphrase, List<String> exclusions) throws InterruptedException {
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

        System.out.println("search submitted");

        loopSearchResults(driver, exclusions);

        Thread.sleep(4000);
        driver.close();
    }

    public void loopSearchResults(WebDriver driver, List<String> exclusions) {
        WebElement pagesContainer = driver.findElement(By.xpath("//div[contains(@class, 'pager') and contains(@class, 'rel') and contains(@class, 'clr')]"));
        List<WebElement> pages = pagesContainer.findElements(By.xpath("//span[contains(@class, 'item') and contains(@class, 'fleft')]"));

        String mainHandle= driver.getWindowHandle();
        System.out.println("main handle" + mainHandle);

        WebElement offersTable = driver.findElement(By.xpath("//*[@id=\"offers_table\"]"));
        List<WebElement> offersList = offersTable.findElements(By.className("marginright5"));
        Random random = new Random();
        offersList.forEach(i -> {
            if (exclusions.stream().noneMatch((new String(i.getText().toLowerCase().getBytes(StandardCharsets.UTF_8), Charset.forName("CP1251"))::contains))) {
//                System.out.println(new String(i.getText().toLowerCase().getBytes(StandardCharsets.UTF_8), Charset.forName("CP1251")));
                i.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
                try {
                    Thread.sleep(random.nextInt(7000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String handle = driver.getWindowHandle();
                System.out.println("curr handle" + handle);
                List<String> handles = new ArrayList<>(driver.getWindowHandles()); //LinkedHashSet
//                handles.forEach(System.out::println);
                driver.switchTo().window(handles.get(handles.size() - 1));

            }
        });
    }

}
