package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.faker.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.templates.Messages.*;

@WebTest
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();
    String pass = randomPassword();

    @Test
    void shoudRegisterNewUser() {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(faker.name().username())
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(SUCCES_REGISTER.getMessage());
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String name = faker.name().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(SUCCES_REGISTER.getMessage())
                .clickSignInButton()
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldErrorRegister(USERNAME.getMessage() + name + ALREADY_EXIST.getMessage());
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String name = faker.name().username();
        final String messageErrorNotEqualPass = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUsername(name)
                .setPassword(faker.internet().password(3, 15))
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldErrorRegister(messageErrorNotEqualPass);
    }
}