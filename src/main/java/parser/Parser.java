package parser;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.Utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
//        System.out.println("main handle: " + mainHandle);

        WebElement offersTable = driver.findElement(By.xpath("//*[@id=\"offers_table\"]"));
        List<WebElement> offersList = offersTable.findElements(By.className("marginright5"));
        offersList.forEach(i -> {
            if (exclusions.stream().noneMatch((new String(i.getText().toLowerCase().getBytes(StandardCharsets.UTF_8), Charset.forName("CP1251"))::contains))) {
//                Utils.printCP1251ToUtf8(i.getText());

                i.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
                Utils.randomPause();

                List<String> handles = new ArrayList<>(driver.getWindowHandles()); //LinkedHashSet
                driver.switchTo().window(handles.get(handles.size() - 1));
//                System.out.println("current handle: " + driver.getWindowHandle());

                WebElement infoBox = driver.findElement(By.className("css-1wws9er"));
                WebElement offerDatePublished = infoBox.findElement(By.cssSelector("span[data-cy='ad-posted-at']"));
                Utils.printCP1251ToUtf8(offerDatePublished.getText());
                WebElement offerPriceAndCurrency = infoBox.findElement(By.xpath("//h3[contains(@class, 'css-okktvh-Text') and contains(@class, 'eu5v0x0')]"));
                Utils.printCP1251ToUtf8(offerPriceAndCurrency.getText());
                WebElement offerHeader = infoBox.findElement(By.cssSelector("h1[data-cy='ad_title']"));
                Utils.printCP1251ToUtf8(offerHeader.getText());
                WebElement offerDescription = infoBox.findElement(By.cssSelector("div[data-cy='ad_description']")).findElement(By.xpath("div"));
                Utils.printCP1251ToUtf8(offerDescription.getText());

                WebElement sellerCard = driver.findElement(By.cssSelector("div[data-cy='seller_card']"));
                WebElement sellerName = sellerCard.findElement(By.cssSelector("h2"));
                Utils.printCP1251ToUtf8(sellerName.getText());

                WebElement sellerPhoneButton = sellerCard.findElement(By.cssSelector("button[data-cy='ad-contact-phone']"));
                sellerPhoneButton.sendKeys("\n");
                System.out.println("CLICK ON PHONE BUTTON");
                Utils.randomPause();
                WebElement sellerPhone = sellerPhoneButton.findElement(By.cssSelector("a[data-testid='contact-phone']"));
                Utils.printCP1251ToUtf8(sellerPhone.getText());

                WebElement sellerLocation = driver.findElement(By.cssSelector("section[class='css-1tgtvwl']"));
                Utils.printCP1251ToUtf8(sellerLocation.getText());
                driver.switchTo().window(mainHandle);

            }
        });
    }
}
