package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Driver;
import static utils.Driver.wait;
import static utils.ExtentReport.bilgiNotu;


public class HomePage {
  //  WebDriverWait wait=new WebDriverWait(Driver.getDriver(), Duration.ofMinutes(5));
    Actions actions=new Actions(Driver.getDriver());
    public HomePage() {
        PageFactory.initElements(Driver.getDriver(),this);
    }

    @FindBy(xpath = "//li[contains(@class,'mega-menu')]")
    private WebElement megaMenu;
    @FindBy(css= "[title='Apple']")
    private WebElement apple;




    public void anaSayfayaGit(){
        Driver.getDriver().get("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");
        bilgiNotu("Anasayfaya girildi");
    }
    public void megaMenuAppleSec(){
        actions.moveToElement(megaMenu).perform();
        wait.until(ExpectedConditions.visibilityOf(apple));
        apple.click();
        bilgiNotu("Mega menuden apple seçildi.");
    }

}
