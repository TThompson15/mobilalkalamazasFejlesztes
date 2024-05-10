package com.example.gazorajelento.model;

import java.time.LocalDateTime;

public class GasMeterInfo {

    private LocalDateTime actualDateTime;   //LocalDateTime.now()
    private long currentAmount;

    public GasMeterInfo() {
    }

    public GasMeterInfo(LocalDateTime actualDateTime, long currentAmount) {
        this.actualDateTime = actualDateTime;
        this.currentAmount = currentAmount;
    }

    public LocalDateTime getActualDateTime() {
        return actualDateTime;
    }

    public void setActualDateTime(LocalDateTime actualDateTime) {
        this.actualDateTime = actualDateTime;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
    }

    @Override
    public String toString() {
        return "GasMeterInfo{" +
                "actualDateTime=" + actualDateTime +
                ", currentAmount=" + currentAmount +
                '}';
    }
}
