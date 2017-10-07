package ece428.mp1;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class MembershipListEntry {
    private int heartBeatCounter;
    private LocalDateTime localTime;
    private boolean isAlive;

    public MembershipListEntry() {
        this.heartBeatCounter = 0;
        this.localTime = LocalDateTime.now();
        this.isAlive = true;
    }

    public MembershipListEntry(final int heartBeatCounter) {
        this.heartBeatCounter = heartBeatCounter;
        this.localTime = LocalDateTime.now();
        this.isAlive = true;
    }

    public int getHeartBeatCounter() {
        return this.heartBeatCounter;
    }

    public void setHeartBeatCounter(final int heartBeatCounter) {
        this.heartBeatCounter = heartBeatCounter;
    }

    public LocalDateTime getLocalTime() {
        return this.localTime;
    }

    public void updateLocalTime() {
        this.localTime = LocalDateTime.now();
    }

    public boolean getAlive() {
        return this.isAlive;
    }

    public void setAlive(final boolean alive) {
        this.isAlive = alive;
    }

    public void updateEntry(final MembershipListEntry other) {
        final int otherHeartBeatCount = other.getHeartBeatCounter();
        final int thisHeartBeatCount = this.getHeartBeatCounter();
        final boolean shouldKill = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                this.getLocalTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                > 6000;

        if (otherHeartBeatCount > thisHeartBeatCount) {
            this.setHeartBeatCounter(otherHeartBeatCount);
            this.updateLocalTime();
            this.setAlive(true);
        } else if (shouldKill) {
            this.setAlive(false);
        }
    }
}
