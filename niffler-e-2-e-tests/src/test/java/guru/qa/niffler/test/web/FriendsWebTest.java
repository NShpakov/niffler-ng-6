package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserType.Type.*;


@ExtendWith(UsersQueueExtension.class)
@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void friendShoudBePresentInFriendsTable(@UserType(WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .shouldPresentHistoryHeader()
                .openFriendsByUrl()
                .checkThatFriendIsExist(user.friend());
    }

    @Test
    void friendsTableShoudBeEmptyForNewUser(@UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .shouldPresentHistoryHeader()
                .openFriendsByUrl()
                .emptyFriendsMsgShouldAppear();

    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .shouldPresentHistoryHeader()
                .openFriendsByUrl()
                .checkThatIncomingRequestIsPresent(user.income());
    }

    @Test
    void outcomeInvitationBePresentInFriendsTable(@UserType(WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user){
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .shouldPresentHistoryHeader()
                .openAllPeopleByUrl()
                .checkThatOutcomeRequestIsPresent(user.outcome());

    }
}