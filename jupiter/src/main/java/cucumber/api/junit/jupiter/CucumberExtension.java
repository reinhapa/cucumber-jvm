package cucumber.api.junit.jupiter;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ContainerExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import cucumber.api.formatter.Formatter;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.jupiter.JunitJupiterReporter;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.ScenarioDefinition;

/**
 * A Junit Jupiter extension to inject Cucumber scenarios into a TestFactory. Annotate your Cucumber
 * runner class with {@code @ExtendsWith(CucumberExtension.class)}. Declare a {@code TestFactory}
 * method with a {@code Stream<DynamicTest>} parameter and return the provided parameter from the
 * method. You can annotate the class with {@code CucumberOptions}.
 *
 * <pre>
 * &#64;CucumberOptions(plugin = {"pretty"}, features = {"classpath:features"})
 * &#64;ExtendWith(CucumberExtension.class)
 * public class BehaviourRunner {
 *     &#64;TestFactory
 *     public Stream&lt;DynamicTest&gt; runAllCucumberScenarios(Stream&lt;DynamicTest&gt; scenarios) {
 *         return scenarios;
 *     }
 * }
 * </pre>
 */
public class CucumberExtension implements ParameterResolver, AfterAllCallback {
    private JunitJupiterReporter jupiterReporter;
    private Formatter formatter;
    private Runtime runtime;

    @Override
    public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return (extensionContext.getTestClass().isPresent()
                // Only applies on TestFactory methods.
                && extensionContext.getTestMethod().isPresent()
                && extensionContext.getTestMethod().get()
                        .getAnnotationsByType(TestFactory.class).length > 0
                && parameterContext.getParameter().getType().equals(Stream.class)
                && parameterContext.getParameter().getParameterizedType()
                        .equals(DynamicTest.class));
    }

    @Override
    public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        if (!extensionContext.getTestClass().isPresent()) {
            throw new IllegalStateException(
                    "resolve has been called even though the supports check failed");
        }

        Class<?> testClass = extensionContext.getTestClass().get();
        RuntimeOptions runtimeOptions = new RuntimeOptionsFactory(testClass).create();
        ClassLoader classLoader = testClass.getClassLoader();
        MultiLoader resourceLoader = new MultiLoader(classLoader);
        ResourceLoaderClassFinder classFinder =
                new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        List<CucumberFeature> cucumberFeatures =
                runtimeOptions.cucumberFeatures(resourceLoader, runtime.getEventBus());
        formatter = runtimeOptions.formatter(classLoader);
        jupiterReporter =
                new JunitJupiterReporter(runtime.getEventBus(), runtimeOptions.isStrict());

        return cucumberFeatures.stream().flatMap(feature -> feature.getGherkinFeature().getFeature()
                .getChildren().stream().map(this::createDynamicTest));
    }

    private DynamicTest createDynamicTest(ScenarioDefinition scenarioDefinition) {
        return dynamicTest(scenarioDefinition.getName(), () -> {
            // jupiterReporter.scenario((Scenario) scenarioDefinition.getGherkinModel());
            // scenarioDefinition.run(formatter, reporter, runtime);
            // Result result = jupiterReporter.getResult(scenarioDefinition.getGherkinModel());

            // Throwable error = result.getError();
            // if (error != null) {
            // throw error;
            // }

            // If the scenario is skipped, then the test is aborted (neither passes nor fails).
            // assumeFalse(Result.SKIPPED == result);
        });
    }

    @Override
    public void afterAll(ContainerExtensionContext context) throws Exception {
        // formatter.done();
        // formatter.eof();
        // formatter.close();
    }
}
