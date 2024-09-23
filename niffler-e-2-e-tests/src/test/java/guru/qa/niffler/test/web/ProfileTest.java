package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();
    private final ProfilePage profilePage = new ProfilePage();

    @Category(
            username = "duck",
            name = "Категория11",
            archived = false)
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345678");
        mainPage.clickProfileButton();
        profilePage.clickArchiveButton()
                .clickArchiveButtonSubmit()
                .shouldBeVisibleSuccessMessage()
                .clickShowArchivedButton()
                .shouldForCategoryName(category.name());
    }

    @Category(
            username = "duck",
            name = "Категория12",
            archived = false)
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345678");

        mainPage.clickProfileButton();
        profilePage.shouldActiveCategoryList(category.name());
        Thread.sleep(5000);
    }
}