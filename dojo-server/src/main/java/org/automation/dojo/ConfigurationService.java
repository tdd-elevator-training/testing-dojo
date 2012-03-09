package org.automation.dojo;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author serhiy.zelenin
 */
public class ConfigurationService implements Runnable {
    private long minorReleaseFrequency = 10 * 60 * 1000;
    private long penaltyTimeOut = 2 * 60 * 1000;
    private int penaltyValue = 2;
    private boolean manualReleaseTriggering = true;
    private int liarWeight = 20;
    
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private ReleaseEngine releaseEngine;
    @Autowired
    private TimeService timeService;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;
    private Date nextMinorRelease;
    private Date previousRelease;
    private Date nextPenaltyTickTime;
    private Date previousTick;
    private boolean paused = false;
    private int exceptionWeight;

    public ConfigurationService() {
    }


    public ConfigurationService(TimeService timeService, ScoreService scoreService, ReleaseEngine releaseEngine) {
        this.timeService = timeService;
        this.scoreService = scoreService;
        this.releaseEngine = releaseEngine;
    }

    public void init() {
        executor = new ScheduledThreadPoolExecutor(1);
        future = executor.scheduleAtFixedRate(this, 60, 60, TimeUnit.SECONDS);
        calculateNextTickTime();
    }

    public void destroy() {
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
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

    public boolean isManualReleaseTriggering() {
        return manualReleaseTriggering;
    }

    public void setManualReleaseTriggering(boolean manualReleaseTriggering) {
        this.manualReleaseTriggering = manualReleaseTriggering;
    }

    public void run() {
        try {
            if (nextPenaltyTickTime.getTime() < timeService.now().getTime()) {
                if (!paused) {
                    scoreService.tick(timeService.now().getTime());
                }
                previousTick = nextPenaltyTickTime;
                calculateNextTickTime();
            }

            if (nextMinorRelease == null || manualReleaseTriggering) {
                return;
            }
            if (nextMinorRelease.getTime() < timeService.now().getTime()) {
                if (!paused) {
                    releaseEngine.nextMinorRelease();
                }
                previousRelease = nextMinorRelease;
                calculateNextReleaseDate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Date getNextMinorReleaseTime() {
        return nextMinorRelease;
    }


    public void adjustChanges() {
        future.cancel(false);
        executor.scheduleAtFixedRate(this, penaltyTimeOut, penaltyTimeOut, TimeUnit.MILLISECONDS);
        calculateNextTickTime();

        if (manualReleaseTriggering) {
            return;
        }
        calculateNextReleaseDate();
    }

    private void calculateNextTickTime() {
        if (previousTick == null) {
            previousTick = timeService.now();
        }
        nextPenaltyTickTime = new Date(previousTick.getTime() + penaltyTimeOut);
    }

    private void calculateNextReleaseDate() {
        if (previousRelease == null) {
            previousRelease = timeService.now();
        }
        nextMinorRelease = new Date(previousRelease.getTime() + minorReleaseFrequency);
    }

    public Date getNextPenaltyTickTime() {
        return nextPenaltyTickTime;
    }

    public String getNextReleaseRemaining() {
        if (manualReleaseTriggering) {
            return "[ask trainer]";
        }

        Date diff = new Date(nextMinorRelease.getTime() - timeService.now().getTime());
        return new SimpleDateFormat("mm 'min' ss 'sec'").format(diff);        
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getLiarWeight() {
        return liarWeight;
    }

    public void setLiarWeight(int liarWeight) {
        this.liarWeight = liarWeight;
    }

    public void setExceptionWeight(int exceptionWeight) {
        this.exceptionWeight = exceptionWeight;
    }

    public int getExceptionWeight() {
        return exceptionWeight;
    }
}
