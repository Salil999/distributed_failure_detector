package ece428.mp1;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class MembershipListEntry {
    private int heartBeatCounter;
    private long localTime;
    private boolean isAlive;

    public MembershipListEntry() {
        this.heartBeatCounter = 0;
        this.localTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.isAlive = true;
    }

    public MembershipListEntry(final int heartBeatCounter) {
        this.heartBeatCounter = heartBeatCounter;
        this.localTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.isAlive = true;
    }

    public MembershipListEntry(final int heartBeatCounter, final long localTime, final boolean isAlive) {
        this.heartBeatCounter = heartBeatCounter;
        this.localTime = localTime;
        this.isAlive = isAlive;
    }


    public int getHeartBeatCounter() {
        return this.heartBeatCounter;
    }

    public void setHeartBeatCounter(final int heartBeatCounter) {
        this.heartBeatCounter = heartBeatCounter;
    }

    public long getLocalTime() {
        return this.localTime;
    }

    public synchronized void updateLocalTime() {
        this.localTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public boolean getAlive() {
        return this.isAlive;
    }

    public void setAlive(final boolean alive) {
        this.isAlive = alive;
    }

    public synchronized void updateEntry(final MembershipListEntry other, final NodeID nodeID) {
        final int otherHeartBeatCount = other.getHeartBeatCounter();
        final int thisHeartBeatCount = this.getHeartBeatCounter();
        final boolean shouldKill = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - this.getLocalTime() > 3000;


        if (otherHeartBeatCount > thisHeartBeatCount) {
            this.setHeartBeatCounter(otherHeartBeatCount);
            this.updateLocalTime();
            this.setAlive(true);
        } else if (shouldKill) {
            this.setAlive(false);
            System.out.println("node failed: " + nodeID.getIPAddress());
        }
    }
}
