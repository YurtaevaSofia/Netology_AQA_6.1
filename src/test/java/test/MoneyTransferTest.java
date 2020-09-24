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
    DataGenerator dataGenerator = new DataGenerator();

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
        int expectedFirstCardBalance = dashboardPage.getCardBalance(dataGenerator.getFirstCardNumber()) - smallTransferAmount;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(dataGenerator.getSecondCardNumber()) + smallTransferAmount;
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, dataGenerator.getSecondCardNumber());// с одной на другую
        moneyTransferPage.transferMoney(smallTransferAmount, dataGenerator.getFirstCardNumber());
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(dataGenerator.getFirstCardNumber());
        int secondCardBalanceAfterTransfer = dashboardPage.getCardBalance(dataGenerator.getSecondCardNumber());
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
        int expectedFirstCardBalance = dashboardPage.getCardBalance(dataGenerator.getFirstCardNumber()) - bigTransferAmount;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(dataGenerator.getSecondCardNumber()) + bigTransferAmount;
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, dataGenerator.getSecondCardNumber());  //с одной на другую
        moneyTransferPage.transferMoney(bigTransferAmount, dataGenerator.getFirstCardNumber());
        moneyTransferPage.lookForMessage();
    }
// пытаемся перевести деньги с карты на нее саму
    @Test
    void shouldNotTransferMoneyIfTryingToTransferMoneyFromSameCard() {

        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int expectedFirstCardBalance = dashboardPage.getCardBalance(dataGenerator.getFirstCardNumber());    //остается таким же
        MoneyTransferPage moneyTransferPage = dashboardPage.moneyTransfer(dashboardPage, dataGenerator.getFirstCardNumber());
        moneyTransferPage.transferMoney(smallTransferAmount, dataGenerator.getFirstCardNumber());// с одной на ту же самую карту
        int firstCardBalanceAfterTransfer = dashboardPage.getCardBalance(dataGenerator.getFirstCardNumber());
        assertEquals(expectedFirstCardBalance, firstCardBalanceAfterTransfer);

    }

}
