package sec.tcc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Target {

    protected final String vulnerabilityName;
    protected final String[] targets;
    protected final String[] potentialVulRegex;

    protected Target(String vulnerabilityName, String[] targets, String[] potentialVulRegex) {
        this.vulnerabilityName = vulnerabilityName;
        this.targets = targets;
        this.potentialVulRegex = potentialVulRegex;
    }

    public String getVulnerabilityName() {
        return vulnerabilityName;
    }

    public String[] getTargets() {
        return targets;
    }

    public Pattern[] getCompiledVulRegex() {
        List<Pattern> compiledPatterns = new ArrayList<>();

        for (String regex : potentialVulRegex) {
            compiledPatterns.add(Pattern.compile(regex));
        }

        return compiledPatterns.toArray(new Pattern[0]);
    }

}
