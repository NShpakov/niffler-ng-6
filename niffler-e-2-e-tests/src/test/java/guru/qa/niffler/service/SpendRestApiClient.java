package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendRestApiClient implements SpendClient {
    SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return null;
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return null;
    }

    @Override
    public void removeCategory(CategoryJson category) {

    }
}
