package guru.qa.niffler.test.web;


import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.errors.Messages.BAD_CREDENTIALS;

@ExtendWith(BrowserExtension.class)
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {


        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123");

        new LoginPage().shouldBeDisplayedErrorForm(BAD_CREDENTIALS.getMessage());
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        final String statisticsHeader = "Statistics";
        final String historyHeader = "History of Spendings";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("niko", "12345678");

        mainPage.shouldBeDispayedStatisticsHeader(statisticsHeader)
                .shouldHistoryHeader(historyHeader);
    }
}