package org.example.clansgoldapp.entity;

import java.util.Date;

public class OperationLogEntity {

    private Long id;
    private Long clanId;
    private int previousGold;
    private int currentGold;
    private int goldChange;
    private String reason;
    private Date timestamp;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClanId() {
        return clanId;
    }

    public void setClanId(Long clanId) {
        this.clanId = clanId;
    }

    public int getPreviousGold() {
        return previousGold;
    }

    public void setPreviousGold(int previousGold) {
        this.previousGold = previousGold;
    }

    public int getCurrentGold() {
        return currentGold;
    }

    public void setCurrentGold(int currentGold) {
        this.currentGold = currentGold;
    }

    public int getGoldChange() {
        return goldChange;
    }

    public void setGoldChange(int goldChange) {
        this.goldChange = goldChange;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
