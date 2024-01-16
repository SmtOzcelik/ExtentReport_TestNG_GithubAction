package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import utils.Driver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.ExtentReport.bilgiNotu;

public class MegaMenu {
    Actions actions = new Actions(Driver.getDriver());

    public MegaMenu() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(css = "[for='mz-fc-0-33']")
    private WebElement red;
    @FindBy(xpath = "//label[contains(@aria-describedby,'tooltip')]")
    private WebElement toolTip;
    @FindBy(xpath = "//div[@id=\"mz-filter-content-0\"]//div[@data-role=\"rangeslider\"]/span")
    private WebElement top1;
    @FindBy(xpath = "//div[@id=\"mz-filter-content-0\"]//div[@data-role=\"rangeslider\"]/span/following-sibling::span")
    private WebElement top2;
    @FindBy(xpath = "//div[@id=\"mz-filter-panel-0-0\"]//input[@name=\"mz_fp[min]\"]")
    private WebElement minBox;
    @FindBy(xpath = "//div[@id=\"mz-filter-panel-0-0\"]//input[@name=\"mz_fp[max]\"]")
    private WebElement maxBox;
    @FindBy(xpath = "//span[@class=\"price-new\"]")
    private List<WebElement> priceList;


    public void redYazisiniVeGorunurlugunuDogrula() {
        actions.scrollToElement(red).perform();
        actions.moveToElement(red).perform();
        String redText = red.getAttribute("data-original-title");
        Assert.assertEquals(redText, "Red");
        Assert.assertTrue(toolTip.isDisplayed());
        bilgiNotu("Red yazisi ve görünürlüğü doğrulandi.");

    }

    public void fiyatAraliginiCubukUzerindenYap() {
        actions.clickAndHold(top1).moveByOffset(+30, 0).release().perform();
        actions.clickAndHold(top2).moveByOffset(-40, 0).release().perform();
        bilgiNotu("Fiyat araliği çubuk üzerinden gerçekleştirildi.");
    }

    public void maxVeMinDegerleriniCekYazdir() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        String minValue = (String) js.executeScript("return arguments[0].value;", minBox);
        String maxValue = (String) js.executeScript("return arguments[0].value;", maxBox);
        System.out.println("Minimum Değer: " + minValue);
        System.out.println("Maksimum Değer: " + maxValue);
        bilgiNotu("Maxsimum ve minimum değerler alındi ve yazdırildi.");
    }

    public void maxVeMinDegerleriniGiriniz(int min, int max) throws InterruptedException {
        minBox.clear();
        maxBox.clear();
        minBox.sendKeys(String.valueOf(min));
        maxBox.sendKeys(String.valueOf(max));
        Thread.sleep(3000);
        bilgiNotu("Fiyat araliği için min : " + min + ", " + "max : " + max + " girildi.");
    }

    public void urunlerinSecilenAraliktaOldugunuDogrulama(int min, int max) {
        String priceText;
        int priceValue;
        for (WebElement each : priceList) {
            priceText = each.getText();
            priceValue = extractPriceValue(priceText);

            if (priceValue < min && priceValue > max) {
                System.out.println("Fiyat aralığı dışında: " + priceValue);
            }
        }
        bilgiNotu("Ürünlerin "+min+" ile "+max+" arasinda olup olmadiği kontrol edildi.");
    }








    private static int extractPriceValue(String priceText) {
        // "$165.00" gibi bir formattan sayıyı çıkarmak için regex kullanma
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(priceText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return 0; // Hata durumu, uygun sayı bulunamadı
        }
    }
}
