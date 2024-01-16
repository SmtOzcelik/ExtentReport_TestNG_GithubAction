package tests;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MegaMenu;

import static utils.Driver.closeDriver;

@Listeners(utils.Listener.class)
public class TC03_MaxMin_DegerlerinOkuma {

    @Test(testName = "TC03 Maxsimum ve Minimum Degerlerini Okuma")
    public void testTC03(){
        HomePage homePage=new HomePage();
        homePage.anaSayfayaGit();
        homePage.megaMenuAppleSec();

        MegaMenu megaMenu=new MegaMenu();
        megaMenu.fiyatAraliginiCubukUzerindenYap();
        megaMenu.maxVeMinDegerleriniCekYazdir();

        closeDriver();
    }
}
