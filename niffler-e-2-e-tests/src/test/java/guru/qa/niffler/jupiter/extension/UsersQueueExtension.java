package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public static record StaticUser(String username,
                             String password,
                             String friend,
                             String income,
                             String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    //Внести своих пользователей
    static {
        EMPTY_USERS.add(new StaticUser("duck", "12345678", "", "", ""));
        WITH_FRIEND_USERS.add(new StaticUser("Mixa", "qwerty", "Albert", "", ""));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("Vova", "12345", "", "Mixa", ""));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("Vasya", "qwerty1", "", "", "Albert"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                            UserType ut = p.getAnnotation(UserType.class);
                            Queue<StaticUser> queue = getQueueByUserType(ut.value());
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();

                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(queue.poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase ->
                                    testCase.setStart(new Date().getTime()));
                            user.ifPresentOrElse(
                                    u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                            .getOrComputeIfAbsent(
                                                    context.getUniqueId(),
                                                    key -> new HashMap<>()
                                            )).put(ut, u),
                                    () -> {
                                        throw new IllegalStateException("Can`t obtain user after 30s.");
                                    }
                            );
                        }
                );

    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if ( users != null) {
            users.forEach((k, v) -> getQueueByUserType(k.value()).add(v));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (StaticUser) extensionContext.
                getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class).get(parameterContext.findAnnotation(UserType.class)
                        .get());
    }

    public Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        switch (type) {
            case EMPTY:
                return EMPTY_USERS;
            case WITH_FRIEND:
                return WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST:
                return WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST:
                return WITH_OUTCOME_REQUEST_USERS;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}