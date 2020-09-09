package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    private ElementsCollection cards = $$(".list__item");

public int getCardBalance(int index) {
    if (index == 1) {
        String text = cards.first().text();
        String [] words = text.split(" ");
        int balance = Integer.parseInt(words[5]);
        return balance;
    }
    else if (index == 2) {
        String text = cards.last().text();
        String [] words = text.split(" ");
        int balance = Integer.parseInt(words[5]);
        return balance;
    }
    else System.out.println("Выберите карту номер 1 или 2");
    return 0;
}

    public MoneyTransferPage chooseACardToAddAmountTo(int index) {
        if (index == 1) {
            cards.first().$("[class=button__text]").click();
        }
        else if (index == 2) {
            cards.last().$("[class=button__text]").click();
        }
        else System.out.println("Выберите карту номер 1 или 2");
        return new MoneyTransferPage();
    }
public void moneyTransfer (DashboardPage dashboardPage, int cardIndexForTransfer, int cardIndexFromTransfer, int amountForTransfer){
    int firstCardBalance = dashboardPage.getCardBalance(1);
    int secondCardBalance = dashboardPage.getCardBalance(2);
    MoneyTransferPage moneyTransferPage = dashboardPage.chooseACardToAddAmountTo(cardIndexForTransfer);
    moneyTransferPage.transferMoney(amountForTransfer, cardIndexFromTransfer);
}

}
