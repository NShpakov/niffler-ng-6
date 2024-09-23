package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

public class ListPeoplePage {
    private final ElementsCollection listPeopleRows = $$("#all tr");
    public ListPeoplePage checkThatOutcomeRequestIsPresent(String name) {
        listPeopleRows.findBy(text(name)).$(byText("Waiting...")).should(appear);
        return this;
    }
}