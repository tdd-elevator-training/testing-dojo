package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class ConfigurationService {
    private int minorReleaseFrequency = 10;
    private int penaltyTimeOut = 1;
    private int penaltyValue;
    private double penaltyMultiplier;

    public int getMinorReleaseFrequency() {
        return minorReleaseFrequency;
    }

    public void setMinorReleaseFrequency(int minorReleaseFrequency) {
        this.minorReleaseFrequency = minorReleaseFrequency;
    }

    public void setPenaltyTimeOut(int penaltyTimeOut) {
        this.penaltyTimeOut = penaltyTimeOut;
    }

    public int getPenaltyTimeOut() {
        return penaltyTimeOut;
    }

    public int getPenaltyValue() {
        return penaltyValue;
    }

    public void setPenaltyValue(int penaltyValue) {
        this.penaltyValue = penaltyValue;
    }

    public void setPenaltyMultiplier(double penaltyMultiplier) {
        this.penaltyMultiplier = penaltyMultiplier;
    }

    public double getPenaltyMultiplier() {
        return penaltyMultiplier;
    }
}
