package pages;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public DashboardPage() {
    }

    public int getCardBalance(int index) {
        if (index == 1) {
            String text = cards.first().text();
            String [] words = text.split(" ");
            int balance = Integer.parseInt(words[5]);
            return balance;
        }
        else {
            String text = cards.last().text();
            String [] words = text.split(" ");
            int balance = Integer.parseInt(words[5]);
            return balance;
        }}
    public MoneyTransferPage chooseACardToAddAmountTo(int index) {
        if (index == 1) {
            cards.first().$("[class=button__text]").click();
        }
        else  {
            cards.last().$("[class=button__text]").click();
        }
        return new MoneyTransferPage();
    }

    public MoneyTransferPage moneyTransfer (DashboardPage dashboardPage, int cardIndexForTransfer){

        MoneyTransferPage moneyTransferPage = dashboardPage.chooseACardToAddAmountTo(cardIndexForTransfer);
        return moneyTransferPage;

}

    public void checkRightEnter(){
    $("[data-test-id=dashboard]").shouldHave(text("Личный кабинет".trim()));
    }

}
