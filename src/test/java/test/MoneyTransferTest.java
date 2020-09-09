package test;

import data.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldEnterDashboard() {
        //open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();
        DataGenerator.AuthInfo authInfo = DataGenerator.getAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DataGenerator.VerificationCode verificationCode = DataGenerator.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        $("[data-test-id=dashboard]").shouldHave(text("Личный кабинет".trim()));
    }
}

