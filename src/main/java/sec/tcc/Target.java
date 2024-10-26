package sec.tcc;

public abstract class Target {

    protected final String vulnerabilityName;
    protected final String[] targets;

    protected Target(String vulnerabilityName, String[] targets) {
        this.vulnerabilityName = vulnerabilityName;
        this.targets = targets;
    }

    public String getVulnerabilityName() {
        return vulnerabilityName;
    }

    public String[] getTargets() {
        return targets;
    }
}
