package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.page.FriendsPage.FRIENDSPAGEPATH;

public class MainPage {
    private static final Config CFG = Config.getInstance();
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsHeader = $x("//h2[text()='Statistics']");
    private final SelenideElement historyHeader = $x("//h2[text()='History of Spendings']");
    private final SelenideElement menuButton = $("[aria-label=Menu]");
    private final SelenideElement profileButton = $x("//a[@href='/profile']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public FriendsPage openFriendsByUrl() {
        return Selenide.open(CFG.frontUrl() + FRIENDSPAGEPATH, FriendsPage.class);
    }

    public ListPeoplePage openAllPeopleByUrl() {
        return Selenide.open(CFG.frontUrl() + "people/all", ListPeoplePage.class);
    }

    public MainPage shouldPresentHistoryHeader() {
        historyHeader.shouldHave(appear);
        return this;
    }

    public MainPage shouldHistoryHeader(String value) {
        historyHeader.shouldHave(text(value));
        return this;
    }

    public MainPage shouldBeDispayedStatisticsHeader(String value) {
        statisticsHeader.shouldHave(text(value));
        return this;
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage clickMenuButton() {
        menuButton.click();
        return this;
    }

    public ProfilePage goToProfilePage() {
        profileButton.click();
        return new ProfilePage();
    }

}