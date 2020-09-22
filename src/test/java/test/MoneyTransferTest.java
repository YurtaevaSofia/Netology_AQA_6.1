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
    int smallTransferAmount = 100;
    int bigTransferAmount = 12000;

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
        int expectedFirstCardBalance = dashboardPage.getCardBalance("5559000000000001") - smallTransferAmount;
        int expectedSecondCardBalance = dashboardPage.getCardBalance("5559000000000002") + smallTransferAmount;
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, "5559000000000002");// с одной на другую
        moneyTransferPage.transferMoney(smallTransferAmount, "5559000000000001");
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance("5559000000000001");
        int secondCardBalanceAfterTransfer = dashboardPage.getCardBalance("5559000000000002");
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);
        assertEquals(expectedSecondCardBalance, secondCardBalanceAfterTransfer);

    }

    @Test
    void shouldTransferMoneyIfBalanceIsNotEnough() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance("5559000000000001") - bigTransferAmount;
        int expectedSecondCardBalance = dashboardPage.getCardBalance("5559000000000002") + bigTransferAmount;
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, "5559000000000002");  //с одной на другую
        moneyTransferPage.transferMoney(bigTransferAmount, "5559000000000001");
        $("[denied_message]").shouldHave(text("Welcome, user!"));
    }
// пытаемся перевести деньги с карты на нее саму
    @Test
    void shouldNotTransferMoneyIfTryingToTransferMoneyFromSameCard() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance("5559000000000001");    //остается таким же
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, "5559000000000001");
        moneyTransferPage.transferMoney(smallTransferAmount, "5559000000000001");// с одной на ту же самую карту
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance("5559000000000001");
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);

    }

}
