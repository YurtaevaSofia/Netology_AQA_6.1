package pages;


import com.codeborne.selenide.SelenideElement;
import data.DataGenerator;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferPage {

    private SelenideElement amountField = $("[type=text]");
    private SelenideElement fromCardField = $("[type=tel]");
    private SelenideElement Button = $("[data-test-id=action-transfer]");

    public void transferMoney(int amount, String id){
        amountField.doubleClick().sendKeys(Keys.BACK_SPACE);
        amountField.doubleClick().sendKeys(Keys.BACK_SPACE);
        amountField.setValue(Integer.toString(amount));
        fromCardField.setValue(id);
        Button.click();
    }

    public void lookForMessage(){
        $("[denied_message]").shouldHave(text("Недостаточно средств!"));
    }
}
