package ece428.mp1;

import java.net.InetAddress;
import java.time.LocalDateTime;

public class NodeID {
    LocalDateTime startTime;
    InetAddress IPAddress;

    public NodeID() {
    }

    public NodeID(final InetAddress IPAddress) {
        this.startTime = LocalDateTime.now();
        this.IPAddress = IPAddress;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public InetAddress getIPAddress() {
        return this.IPAddress;
    }

    public void setIPAddress(final InetAddress IPAddress) {
        this.IPAddress = IPAddress;
    }

    @Override
    public String toString() {
        return this.startTime + ":" + new String(this.getIPAddress().getAddress());
    }

}
