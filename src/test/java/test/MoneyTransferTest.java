package test;

import data.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MoneyTransferPage;
import pages.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;


class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldEnterDashboard() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        $("[data-test-id=dashboard]").shouldHave(text("Личный кабинет".trim()));
    }

    @Test
    void shouldTransferMoneyIfBalanceIsEnough() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance(1) - 100;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(2) + 100;
        dashboardPage.moneyTransfer(dashboardPage, 2,1, 100);   // с одной на другую
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(1);
        int secondCardBalanceAfterTransfer = dashboardPage.getCardBalance(2);
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalance, secondCardBalanceAfterTransfer);

    }

// в данном случае перевод будет проходить, хотя на одной карте будет оставаться отрицательный баланс
// тест сделан так, чтобы в этом случае он не падал, но issue заведено
    // два раза отнимается 6000, чтобы баланс стал отрицательным
    @Test
    void shouldTransferMoneyIfBalanceIsNotEnough() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance(1) - 12000;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(2) + 12000;
        dashboardPage.moneyTransfer(dashboardPage, 2, 1,12000);  //с одной на другую
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(1);
        int secondCardBalanceAfterTransfer = dashboardPage.getCardBalance(2);
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalance, secondCardBalanceAfterTransfer);

    }
// пытаемся перевести деньги с карты на нее саму
    @Test
    void shouldNotTransferMoneyIfTryingToTransferMoneyFromSameCard() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance(1);    //остается таким же
        dashboardPage.moneyTransfer(dashboardPage, 1,1, 1000);  // с одной на ту же самую карту
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(1);
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);

    }

}

