package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(ua -> {
                    if (ua.spendings().length > 0) {
                        Spending sa = ua.spendings()[0];
                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        sa.category(),
                                        ua.username(),
                                        false
                                ),
                                CurrencyValues.RUB,
                                sa.amount(),
                                sa.description(),
                                ua.username()
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendDbClient.createSpend(spend)
                        );
                    }
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        SpendJson spend = context.getStore(NAMESPACE).get(context.getUniqueId(), SpendJson.class);
        if (spend != null) {
            spendDbClient.deleteSpend(spend);
            spendDbClient.deleteCategory(spend.category());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}