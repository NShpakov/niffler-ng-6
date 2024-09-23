package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    public static final String FRIENDSPAGEPATH = "people/friends";
    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final SelenideElement emptyFriendsMsg = $(withText("There are no users yet"));

    public FriendsPage checkThatFriendIsExist(String friendName) {
        friendsRows.find(text(friendName)).shouldBe(visible);;
        return this;
    }

    public FriendsPage emptyFriendsMsgShouldAppear() {
        emptyFriendsMsg.should(appear);
        return this;
    }

    public FriendsPage checkThatIncomingRequestIsPresent(String name) {
        requestsRows.find(text(name)).shouldBe(visible);
        return this;
    }
}