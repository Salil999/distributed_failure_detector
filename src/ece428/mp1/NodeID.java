package ece428.mp1;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NodeID {
    private final long startTime;
    private final InetAddress IPAddress;


    public NodeID(final InetAddress IPAddress) {
        this.startTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.IPAddress = IPAddress;
    }

    public NodeID(final InetAddress IPAddress, final long startTime) {
        this.startTime = startTime;
        this.IPAddress = IPAddress;
    }

    @Override
    public synchronized boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NodeID)) {
            return false;
        }
        final NodeID other = (NodeID) obj;
        return this.IPAddress.getHostName().equals(other.IPAddress.getHostName());
    }

    @Override
    public synchronized int hashCode() {
//        System.out.println(this.IPAddress.getHostName().hashCode() + "\n\n");
        return this.IPAddress.getHostName().hashCode();
    }

    public long getStartTime() {
        return this.startTime;
    }


    public InetAddress getIPAddress() {
        return this.IPAddress;
    }


    @Override
    public String toString() {
        return this.startTime + ":" + new String(this.getIPAddress().getAddress());
    }

}
