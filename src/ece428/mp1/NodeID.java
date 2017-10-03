package ece428.mp1;

public class NodeID {
    Integer id;
    String IPAddress;

    public NodeID() {
    }

    public NodeID(final int id, final String IPAddress) {
        this.id = id;
        this.IPAddress = IPAddress;
    }

    public NodeID(final Integer id, final String IPAddress) {
        this.id = id;
        this.IPAddress = IPAddress;
    }

    public NodeID(final String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public NodeID(final Integer id) {
        this.id = id;
    }

    public NodeID(final int id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getIPAddress() {
        return this.IPAddress;
    }
}
