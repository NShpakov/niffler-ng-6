package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();


    @User(
            username = "duck",
            categories =@Category (
                    archived = false))
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345678");
        mainPage.clickMenuButton()
                .goToProfilePage()
                .clickArchiveButton()
                .clickArchiveButtonSubmit()
                .shouldBeVisibleSuccessMessage()
                .clickShowArchivedButton()
                .shouldBeDisplayedForCategoryName(category.name());
        ;
    }

    @User(
            username = "duck",
            categories =@Category (
                    archived = false))
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345678");

        mainPage.clickMenuButton()
                .goToProfilePage()
                .shouldActiveCategoryList(category.name());
    }
}