package tests;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MegaMenu;

import static utils.Driver.closeDriver;

@Listeners(utils.Listener.class)
public class TC04_MaxMin_DegerleriniVer_UrunlerOAraliktaMi {

    @Test(testName = "TC04 min=500, max=1500 ücret gir ürün fiyatlari istenilen aralıkta oldugunun doğrulanmasi")
    public void testTC04() throws InterruptedException {

        HomePage homePage=new HomePage();
        homePage.anaSayfayaGit();
        homePage.megaMenuAppleSec();

        MegaMenu megaMenu=new MegaMenu();
        megaMenu.maxVeMinDegerleriniGiriniz(500,1500);
        megaMenu.urunlerinSecilenAraliktaOldugunuDogrulama(500,1500);

        closeDriver();
    }
}
