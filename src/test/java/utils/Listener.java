package utils;

import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;
import static utils.Driver.closeDriver;
import static utils.Driver.driver;
import static utils.ExtentReport.*;


public class Listener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        raporOlustur();
    }

    @Override
    public void onTestStart(ITestResult result) {
        //@Test(testName = "TC01 Pozitif Test") daha güzel isteğimizi yazalım ama çalışmadı
        testOlustur(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName());
        //testOlustur(result.getMethod().getMethodName()); //bu method ismini ceker
        test.info("Test başladı.");
    }
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test başarıyla tamamlandı.");
    }

    public void onTestFailure(ITestResult result) {
        // Hata mesajı
        test.fail("Test başarısız oldu. Çünkü: " + result.getThrowable().getMessage());

        // Ekran Görüntüsü alma
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        test.info("Ekran görüntüsü alındı:");


        // Ekran görüntüsünü dosyaya kaydetme
        File dosya = ts.getScreenshotAs(OutputType.FILE);
        String dosyaYolu = getProperty("user.dir") + File.separator + "raporlar" + File.separator + result.getMethod().getMethodName() + ".png";
        try {
            FileUtils.copyFile(dosya, new File(dosyaYolu));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Ekran görüntüsünü rapora ekleme
        try {
            test.addScreenCaptureFromPath(dosyaYolu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Driver.closeDriver();
    }
    public void onFinish(ITestContext context) {
        closeDriver();
        raporuKaydet();

    }
}
