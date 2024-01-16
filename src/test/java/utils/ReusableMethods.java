

package utils;


import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReusableMethods {

    //===================dosya yolu sayfa ismi satir hucre girince datayi verir
    // Bir method olusturalim
    // dosya yolu, sayfa ismi ve satir , hucre indexini verince hucre bilgisini dondursun.
    public static Cell hucreGetir(String path,String sayfaIsmi, int satirIndex, int hucreIndex){
        Cell cell=null;
        try {
            FileInputStream fis = new FileInputStream(path);
            Workbook workbook= WorkbookFactory.create(fis);
            cell=workbook.getSheet(sayfaIsmi).getRow(satirIndex).getCell(hucreIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cell;
    }

    public static String getScreenshot(String name) throws IOException {
        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/test-output/Screenshots/" + name + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }


    //========Switching Window=====//
    public static void switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.getDriver().switchTo().window(origin);
    }


    //========Hover Over=====//
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //==========Return a list of string given a list of Web Element====////
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }
    //========Returns the Text of the element given an element locator==//
    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //   HARD WAIT WITH THREAD.SLEEP
    //   waitFor(5);  => waits for 5 second
    public static void waitFor(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //===============Explicit Wait==============//
    public static WebElement waitForVisibility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }


    public static void waitForPageToLoad(long timeout) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeout + " seconds");
        }
    }

    //======Fluent Wait====//
    public static WebElement fluentWait(final WebElement webElement, int timeout) {
        //FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver()).withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec, TimeUnit.SECONDS);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(3))//Wait 3 second each time
                .pollingEvery(Duration.ofSeconds(1));//Check for the element every 1 second
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    //==========Excelli map'e aktarip kullanma methodu
    public static Map<String,String> mapOlustur(String path, String sayfaAdi) {
        Map<String,String> excelMap=new TreeMap<>();

        Workbook workbook=null;
        // ilk adim excelde istenen sayfaya ulasmak
        try {
            FileInputStream fis=new FileInputStream(path);
            workbook=WorkbookFactory.create(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int satirSayisi=workbook.getSheet(sayfaAdi).getLastRowNum();
        String key="";
        String value="";

        for (int i = 0; i <=satirSayisi ; i++) {
            // ikinci adim tablodaki hucreleri map'a uygun hale donusturmek
            key=workbook.getSheet(sayfaAdi).getRow(i).getCell(0).toString();
            value=workbook.getSheet(sayfaAdi).getRow(i).getCell(1).toString()+
                    ", "+workbook.getSheet(sayfaAdi).getRow(i).getCell(2).toString()+
                    ", "+workbook.getSheet(sayfaAdi).getRow(i).getCell(3).toString();
            // ucuncu adim key-value haline getirdigimiz satirlari map'a eklemek
            excelMap.put(key,value);
        }

        return excelMap;
    }




    //========ScreenShot Web Element(Bir webelementin resmini alma)=====//
    public static String getScreenshotWebElement(String name, WebElement element) throws IOException {
        String date = new SimpleDateFormat("yyyy-MM-dd-hhmm").format(new Date());

        // TakesScreenshot, ekran görüntüsünü alan bir selenyum arayüzüdür.
        File source = element.getScreenshotAs(OutputType.FILE);

        // ekran görüntüsü konumunun tam yolu
        String wElementSS = System.getProperty("user.dir") + "/target/WElementScreenshots/" + name + "_" + date + ".png";
        File finalDestination = new File(wElementSS);

        // ekran görüntüsünü verilen yola kaydedin
        FileUtils.copyFile(source, finalDestination);
        return wElementSS;
    }

    public static WebElement waitForClickable(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickable(By locator, int timeout) {

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public static Random random() { //

        Random random;
        return random = new Random();
    }


    public static Actions getActions() { //getActions method

        Actions actions;
        return actions = new Actions(Driver.getDriver());
    }

    public static Select select(WebElement ddm) { //select method

        Select select;

        return select = new Select(ddm);
    }

    public static void jsScrollClick(WebElement webElement) {  //kaydir ve tikla
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        try {
            webElement.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].scrollIntoView(true);", webElement);
            js.executeScript("arguments[0].click()", webElement);
        }
    }
    public static void jsScroll(WebElement webElement) {  //

        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", webElement);
    }
    public static void jsClick(WebElement webElement) {

        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        // Belirli butonuna js ile click yapalim
        js.executeScript("arguments[0].click();", webElement);
    }
    public static void jsScrollToBottom(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    public static void jsScrollDown(){
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("window.scrollBy(0,250)", "");
    }
    public static void toBeClickableWait(WebElement webElement) {

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static void visibilityOfWait(WebElement webElement) {

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }


    public static void visibilityOfElementLocatedWait(By located) {

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(located));
    }



    //Bu method text den aldığımız $165.00 değerini int 165 e çevrilmesidir
    public static int extractPriceValue(String priceText) {
        // "$165.00" gibi bir formattan sayıyı çıkarmak için regex kullanma
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(priceText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return 0; // Hata durumu, uygun sayı bulunamadı
        }
    }
    // Metindeki sayıyı çıkarmak için yardımcı fonksiyon
    public static int extractProductCount(String productInfoText) {
        // "Showing 1 to 10 of 35 (1 Pages)" gibi bir formattan 10 sayıyı çıkarmak için regex kullanma
        Pattern pattern = Pattern.compile("Showing \\d+ to (\\d+) of \\d+ \\(\\d+ Pages\\)");
        Matcher matcher = pattern.matcher(productInfoText);

        // Metindeki sayıları bul ve ilk bulunan sayıyı al
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0; // Hata durumu, uygun sayı bulunamadı
    }
    public static int extractPagesNumber(String productInfoText) {
        // "Showing 1 to 10 of 35 (1 Pages)" gibi bir formattan sayıyı çıkarmak için regex kullanma
        Pattern pattern = Pattern.compile("Showing \\d+ to \\d+ of \\d+ \\((\\d+) Pages\\)");
        Matcher matcher = pattern.matcher(productInfoText);

        // Metindeki sayıları bul ve ilk bulunan sayıyı al
        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0; // Hata durumu, uygun sayı bulunamadı
    }
}

