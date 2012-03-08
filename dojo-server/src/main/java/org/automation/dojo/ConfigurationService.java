package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class ConfigurationService {
    private long minorReleaseFrequency = 10 * 60 * 1000;
    private long penaltyTimeOut = 60 * 1000;
    private int penaltyValue = 2;

    public long getMinorReleaseFrequency() {
        return minorReleaseFrequency;
    }

    public void setMinorReleaseFrequency(long minorReleaseFrequency) {
        this.minorReleaseFrequency = minorReleaseFrequency;
    }

    public void setPenaltyTimeOut(long penaltyTimeOut) {
        this.penaltyTimeOut = penaltyTimeOut;
    }

    public long getPenaltyTimeOut() {
        return penaltyTimeOut;
    }

    public int getPenaltyValue() {
        return penaltyValue;
    }

    public void setPenaltyValue(int penaltyValue) {
        this.penaltyValue = penaltyValue;
    }

}
