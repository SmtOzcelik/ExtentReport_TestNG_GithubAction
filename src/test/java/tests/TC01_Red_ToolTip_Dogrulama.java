package tests;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MegaMenu;

import static utils.Driver.closeDriver;

@Listeners(utils.Listener.class)
public class TC01_Red_ToolTip_Dogrulama {
         /*
        Task: megaMenu -> Apple -> Color
              Kırmızı renginin üzerinde bekle (Red) doğrula
         */

    @Test(testName = "TC01 Red ToolTip Dogrulama")
    public void testRedDogrulama() {
        HomePage homePage=new HomePage();
        homePage.anaSayfayaGit();
        homePage.megaMenuAppleSec();

        MegaMenu megaMenu=new MegaMenu();
        megaMenu.redYazisiniVeGorunurlugunuDogrula();

        closeDriver();

    }
}
