package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement archiveButton = $$(".MuiIconButton-sizeMedium").get(1);
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement activeCategoryList = $(".MuiChip-filled");
    private final SelenideElement successMessage = $(".MuiTypography-body1");
    private final SelenideElement showArchivedBtn = $(".MuiSwitch-switchBase");
    private final SelenideElement showArchivedButton = $x("//title");
    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement userName = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");


    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage shouldBeDisplayedForCategoryName(String categoryName) {
        categoryList.filterBy(text(categoryName))
                .first()
                .shouldHave(text(categoryName));
        return this;
    }


    public ProfilePage shouldActiveCategoryList(String value) {
        activeCategoryList.shouldHave(text(value));
        return this;
    }

    public ProfilePage clickArchiveButton() {
        archiveButton.click();
        return this;
    }

    public ProfilePage checkUsername(String username) {
        this.userName.should(value(username));
        return this;
    }

    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage shouldBeVisibleSuccessMessage() {
        successMessage.shouldBe(visible);
        return this;
    }

    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    public ProfilePage clickShowArchivedButton() {
        showArchivedBtn.click();
        return this;
    }

    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }

    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }
}