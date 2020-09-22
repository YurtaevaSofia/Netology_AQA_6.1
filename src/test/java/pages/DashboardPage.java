package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
    }

    public int extractCardBalance(String text) {
        String [] words = text.split(" ");
        int balance = Integer.parseInt(words[5]);
        return balance;
    }

    public int getCardBalance(String id) {
       String [] characters = id.split("");
        String lastFourNumbers = "**** **** **** 000"+characters[15];;
        String text = $$("[data-test-id]").find(text(lastFourNumbers)).text();
        return extractCardBalance(text);
    }

    public MoneyTransferPage chooseACardToAddAmountTo(String id) {
        String [] characters = id.split("");
        String lastFourNumbers = "**** **** **** 000"+characters[15];
        SelenideElement card = $$("[data-test-id]").find(text(lastFourNumbers));
        card.$("[class=button__text]").click();
        return new MoneyTransferPage();
    }

    public MoneyTransferPage moneyTransfer(DashboardPage dashboardPage, String id){
        MoneyTransferPage moneyTransferPage = dashboardPage.chooseACardToAddAmountTo(id);
        return moneyTransferPage;

}

    public void checkRightEnter(){
    $("[data-test-id=dashboard]").shouldHave(text("Личный кабинет".trim()));
    }

}
