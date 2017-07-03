package cucumber.runtime.junit.jupiter;

import java.util.HashMap;
import java.util.Map;

import cucumber.api.Result;
import cucumber.runner.EventBus;

/**
 * A Reporter that decorates the provided classes with the ability to look up results by scenario or
 * scenario outline. Not thread safe. The logic relies on a result being associated with the most
 * recently started scenario or scenario outline.
 */
public class JunitJupiterReporter {
    private final boolean strict;
    // private TagStatement currentStatement = null;
    // private final Reporter reporter;
    private final Map<String, Result> results = new HashMap<>();

    public JunitJupiterReporter(EventBus bus, boolean strict) {
        this.strict = strict;
    }

    // public Result getResult(TagStatement statement) { return results.get(statement.getId()); }
    //
    // public void scenario(Scenario scenario) {
    // currentStatement = scenario;
    // results.put(scenario.getId(), Result.UNDEFINED);
    // }

    /**
     * Called once per step.
     * 
     * @param result The result of the step that has just been executed.
     */
    // @Override
    // public void result(Result result) {
    // reporter.result(result);
    // if (currentStatement != null) {
    // results.put(currentStatement.getId(), result);
    // // Once a scenario has a failed step, it's failed.
    // // Later steps will be recorded as skipped, but we don't want to let that change the result
    // of
    // the scenario.
    // if (Result.FAILED.equals(result.getStatus())) { currentStatement = null; }
    // }
    // }

    // @Override
    // public void before(Match match, Result result) { reporter.before(match, result); }

    // @Override
    // public void after(Match match, Result result) { reporter.after(match, result); }

    /**
     * Called when a Java step is matched and about to be called.
     * 
     * @param match The match, which includes details about the feature file.
     */
    // @Override
    // public void match(Match match) { reporter.match(match); }

    // @Override
    // public void embedding(String mimeType, byte[] data) { reporter.embedding(mimeType, data); }

    // @Override
    // public void write(String text) { reporter.write(text); }

}
