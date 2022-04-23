package parser;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
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

//        driver.manage().window().maximize();

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
        WebElement pagesContainer = null;
        List<WebElement> pages = null;
        try {
            pagesContainer = driver.findElement(By.xpath("//div[contains(@class, 'pager') and contains(@class, 'rel') and contains(@class, 'clr')]"));
            pages = pagesContainer.findElements(By.xpath("//span[contains(@class, 'item') and contains(@class, 'fleft')]"));
        } catch (NoSuchElementException e) {
            pagesContainer = driver.findElement(By.cssSelector("div[data-testid='listing-grid']"));
            pages = pagesContainer.findElements(By.cssSelector("div[data-testid='pagination-wrapper']"));
        }

        String mainHandle= driver.getWindowHandle();

        for (int i = 1; i < pages.size(); i++) {
            WebElement offersTable;
            List<WebElement> offersList;
            try {
                offersTable = driver.findElement(By.xpath("//*[@id=\"offers_table\"]"));
                offersList = offersTable.findElements(By.className("marginright5"));
            } catch (NoSuchElementException e) {
                offersTable = driver.findElement(By.cssSelector("div[data-testid='listing-grid']"));
                offersList = offersTable.findElements(By.cssSelector("div[data-cy='l-card']"));
            }
            offersList.forEach(offer -> {
                //filter exclusions
                if (exclusions.stream().noneMatch((new String(offer.getText().toLowerCase().getBytes(StandardCharsets.UTF_8), Charset.forName("CP1251"))::contains))) {
                    offer.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
                    Utils.randomPause();

                    List<String> handles = new ArrayList<>(driver.getWindowHandles()); //LinkedHashSet
                    driver.switchTo().window(handles.get(handles.size() - 1)); //first one is always mainhandle
                    extractInfo(driver, mainHandle);
                }
            });
            Utils.randomPause(2000);
            System.out.println("\nBEFORE switch page");
            pagesContainer = driver.findElement(By.xpath("//div[contains(@class, 'pager') and contains(@class, 'rel') and contains(@class, 'clr')]"));
            pages = pagesContainer.findElements(By.xpath("//span[contains(@class, 'item') and contains(@class, 'fleft')]"));
            // switch to next listing page
            driver.get(pages.get(i).findElement(By.tagName("a")).getAttribute("href"));
        }


    }

    private void extractInfo(WebDriver driver, String mainHandle) {
        WebElement sellerCard = driver.findElement(By.cssSelector("div[data-cy='seller_card']"));

        WebElement sellerPhoneButton = sellerCard.findElement(By.cssSelector("button[data-cy='ad-contact-phone']"));
        sellerPhoneButton.sendKeys("\n");
        System.out.println("CLICK ON PHONE BUTTON");
        Utils.randomPause();
//        WebElement sellerPhone = sellerPhoneButton.findElement(By.cssSelector("a[data-testid='contact-phone']"));
//        Utils.printCP1251ToUtf8(sellerPhone.getText());
//        WebElement sellerName = sellerCard.findElement(By.cssSelector("h2"));
//        Utils.printCP1251ToUtf8(sellerName.getText());
//
//        WebElement sellerLocation = driver.findElement(By.cssSelector("section[class='css-1tgtvwl']"));
//        Utils.printCP1251ToUtf8(sellerLocation.getText());
//
//        WebElement infoBox = driver.findElement(By.className("css-1wws9er"));
//        WebElement offerDatePublished = infoBox.findElement(By.cssSelector("span[data-cy='ad-posted-at']"));
//        Utils.printCP1251ToUtf8(offerDatePublished.getText());
//        WebElement offerPriceAndCurrency = infoBox.findElement(By.xpath("//h3[contains(@class, 'css-okktvh-Text') and contains(@class, 'eu5v0x0')]"));
//        Utils.printCP1251ToUtf8(offerPriceAndCurrency.getText());
//        WebElement offerHeader = infoBox.findElement(By.cssSelector("h1[data-cy='ad_title']"));
//        Utils.printCP1251ToUtf8(offerHeader.getText());
//        WebElement offerDescription = infoBox.findElement(By.cssSelector("div[data-cy='ad_description']")).findElement(By.xpath("div"));
//        Utils.printCP1251ToUtf8(offerDescription.getText());

        driver.close();
        driver.switchTo().window(mainHandle);
    }
}
