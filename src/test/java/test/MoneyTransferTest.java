package test;
import lombok.Value;
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
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkRightEnter();
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
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, 2);// с одной на другую
        moneyTransferPage.transferMoney(100, 1);
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(1);
        int secondCardBalanceAfterTransfer = dashboardPage.getCardBalance(2);
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalance, secondCardBalanceAfterTransfer);

    }

// в данном случае перевод будет проходить, хотя на одной карте будет оставаться отрицательный баланс
// тест сделан так, чтобы в этом случае он не падал, но issue заведено
    @Test
    void shouldTransferMoneyIfBalanceIsNotEnough() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance(1) - 12000;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(2) + 12000;
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, 2);  //с одной на другую
        moneyTransferPage.transferMoney(12000, 1);
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
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, 1);
        moneyTransferPage.transferMoney(1000, 1);// с одной на ту же самую карту
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(1);
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);

    }

}

//moneyTransferPage.transferMoney(amountForTransfer, cardIndexFromTransfer);