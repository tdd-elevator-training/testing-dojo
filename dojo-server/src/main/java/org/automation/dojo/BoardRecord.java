package org.automation.dojo;

/**
 * @author serhiy.zelenin
 */
public class BoardRecord implements Comparable <BoardRecord>{
    private String player;
    private int total;
    private int relativeScore;

    public BoardRecord(String player, int total) {
        this.player = player;
        this.total = total;
    }

    public String getPlayer() {
        return player;
    }

    public int getTotal() {
        return total;
    }

    public int compareTo(BoardRecord record) {
        return record.total - total;
    }

    public void setRelativeScore(int relativeScore) {
        this.relativeScore = relativeScore;
    }

    public int getRelativeScore() {
        return relativeScore;
    }
}
