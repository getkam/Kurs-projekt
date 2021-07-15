import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


public class ProjektKoncowy {
    public static WebDriver driver;


    @BeforeTest
    public void Inicjalizacja (){
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://www.selenium-shop.pl/sklep");
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        //zmiana commit 3
    }


    public void takeScreenShot() {

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(src, new File("src/main/resources/screenshots/" + System.currentTimeMillis()+ "_screenshot.png"));
            // System.currentTimeMillis();

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }

    @Test
    public void test(driver){

        WebElement IkonaKoszulki = driver.findElement(By.xpath ("//*[contains(text() , 'Koszulka West Ham United')]"));
        IkonaKoszulki.click();
        takeScreenShot();
        Assert.assertEquals(driver.getTitle(), "Koszulka West Ham United – Selenium Shop Automatyzacja Testów");

        //2. Zweryfikuj nazwę produktu
        WebElement NazwaProduktu = driver.findElement(By.xpath("//h1[@class = 'product_title entry-title']"));
        System.out.println(NazwaProduktu.getText());
        Assert.assertEquals(NazwaProduktu.getText(), "KOSZULKA WEST HAM UNITED");

        //3. Zweryfikuj cene produktu
        WebElement CenaProduktu = driver.findElement(By.xpath("//span[@class='woocommerce-Price-amount amount']"));
        System.out.println(CenaProduktu.getText());
        Assert.assertEquals(CenaProduktu.getText(), "90,00 ZŁ");

        //4. Ilość
        WebElement IloscProduktow = driver.findElement(By.xpath("//input[@class='input-text qty text']"));
        System.out.println(IloscProduktow.getAttribute("value"));
        Assert.assertEquals(IloscProduktow.getAttribute("value"), "1");

        //5. Kliknij przycisk DODAJ DO KOSZYKA oraz zweryfikuj, czy wyświetlony komunikat zawiera następującą treść: “Koszulka West Ham United” został dodany do koszyka.
        WebElement DodajDoKoszykaButton = driver.findElement(By.xpath("//button[@name='add-to-cart']"));
        DodajDoKoszykaButton.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='woocommerce-message']")));

        WebElement Komunikat = driver.findElement(By.xpath("//*[@class='woocommerce-message']"));           //<--------------------------------------------------
        System.out.println(Komunikat.getText());
        String komunikatIIczesc = Komunikat.getText().substring(14, Komunikat.getText().length());
        System.out.println("Komunikat II czesc: "+ komunikatIIczesc);
        Assert.assertEquals(komunikatIIczesc, "“Koszulka West Ham United” został dodany do koszyka.");

        //6. Kliknij Zobacz koszyk i zweryfikuj tytuł strony, na którą zostałeś przeniesiony.
        //Zrób screen po przeniesieniu się na stronę koszyka i zapisz go w katalogu src/main/resources

        WebElement ZobaczKoszykButton = driver.findElement(By.xpath("//a[@class='button wc-forward']"));
        ZobaczKoszykButton.click();
        System.out.println("*******KOSZYK********");
        Assert.assertEquals(driver.getTitle(), "Koszyk – Selenium Shop Automatyzacja Testów");
        takeScreenShot();

        //7. Zweryfikuj czy domyślnie została zaznaczona pozycja: Darmowa wysyłka
        WebElement DarmowaWysylkaRadioButton = driver.findElement(By.id("shipping_method_0_free_shipping2"));
        System.out.println("Darmowa wysyłka radio button is selected: "+ DarmowaWysylkaRadioButton.isSelected());
        Assert.assertTrue(DarmowaWysylkaRadioButton.isSelected());

        //8.Kliknij przycisk: PRZEJDŹ DO KASY oraz zweryfikuj tytuł strony, na którą zostałeś przeniesiony.
        //Zrób screen po przeniesieniu się na ekran podsumowania zamówienia i zapisz go w katalogu src/main/resources

        WebElement PrzejdzDoKasyButton = driver.findElement(By.linkText("PRZEJDŹ DO KASY"));
        PrzejdzDoKasyButton.click();
        Assert.assertEquals(driver.getTitle(), "Zamówienie – Selenium Shop Automatyzacja Testów");
        takeScreenShot();
        System.out.println("*******STRONA ZAMOWIENIE*********");

        //9. Zweryfikuj nazwę produktu
        WebElement ZamowienieProductName = driver.findElement(By.xpath("//td[@class='product-name']"));
        String ZamowienieProductNameOld = ZamowienieProductName.getText();
        System.out.println("ZamówienieProductNameStringOld: "+ ZamowienieProductNameOld);
        Integer indexOfX = ZamowienieProductNameOld.indexOf("×")-1; //-1 bo spacja
        System.out.println("IndefxOfX: "+ indexOfX);
        String ZamówienieProductNameStringNew = ZamowienieProductNameOld.substring(0,indexOfX-1);
        System.out.println("ZamówienieProductNameStringNew: "+ ZamówienieProductNameStringNew);

        Assert.assertEquals(ZamówienieProductNameStringNew,"Koszulka West Ham United" );                  //<-----------------------------------

        // 10. Zweryfikuj cenę produktu
        WebElement ZamowienieCenaProduktu = driver.findElement(By.xpath("//td[@class='product-total']"));
        System.out.println("ZamowienieCenaProduktu: "+ ZamowienieCenaProduktu.getText());
        Assert.assertEquals(ZamowienieCenaProduktu.getText(), "90,00 zł");


        //11. Zweryfikuj czy domyślnie została zaznaczona pozycja: Darmowa wysyłka
        WebElement ZamowienieDarmowaWysylkaRadioButton = driver.findElement(By.id("shipping_method_0_free_shipping2"));
        System.out.println("Zamówienie Darmowa wysyłka radio button is selected: "+ ZamowienieDarmowaWysylkaRadioButton.isSelected());
        Assert.assertTrue(ZamowienieDarmowaWysylkaRadioButton.isSelected());

        //12.Zweryfikuj kwotę całkowitą produktu zamówienia (pole Suma)
        WebElement ZamowienieSuma = driver.findElement(By.xpath("//tr[@class='order-total']/td"));
        Assert.assertEquals(ZamowienieSuma.getText(), "90,00 zł");


    }


    @AfterTest
    public void Finalizacja (){
        driver.close();
    }
}
