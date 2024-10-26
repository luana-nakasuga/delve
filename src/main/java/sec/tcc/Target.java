package sec.tcc;

public abstract class Target {

    protected final String vulnerabilityName;
    protected final String[] targets;
    protected final String[] potentialVul;

    protected Target(String vulnerabilityName, String[] targets, String[] potentialVul) {
        this.vulnerabilityName = vulnerabilityName;
        this.targets = targets;
        this.potentialVul = potentialVul;
    }

    public String getVulnerabilityName() {
        return vulnerabilityName;
    }

    public String[] getTargets() {
        return targets;
    }

    public String[] getPotentialVul() {
        return potentialVul;
    }

}
