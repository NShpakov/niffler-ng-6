package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import guru.qa.niffler.api.SpendApiClient;

import static guru.qa.niffler.utils.faker.RandomDataUtils.randomName;

public class CategoryExtention implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private final Faker faker = new Faker();
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtention.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(ua -> {
                    if (ua.categories().length > 0) {
                        Category ac = ua.categories()[0];
                        String randomName = randomName();
                        CategoryJson category = new CategoryJson(
                                null,
                                randomName,
                                ua.username(),
                                false);
                        CategoryJson createdCategory = spendApiClient.addCategory(category);

                        if (ac.archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    createdCategory.id(),
                                    createdCategory.name(),
                                    createdCategory.username(),
                                    true
                            );
                            createdCategory = spendApiClient.updateCategory(archivedCategory);
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
                    }
                });


    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtention.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}