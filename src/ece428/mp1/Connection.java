package ece428.mp1;

import java.net.InetAddress;

public class Connection {
    private InetAddress host;
    private Integer port;

    public Connection(final InetAddress host, final Integer port) {
        this.host = host;
        this.port = port;
    }

    public Connection(final Integer port) {
        this.port = port;
    }

    public Connection(final InetAddress host) {
        this.host = host;
    }

    /**
     * @return The host name that the connection holds.
     */
    public InetAddress getHost() {
        return this.host;
    }

    public void setHost(final InetAddress host) {
        this.host = host;
    }

    /**
     * @return The port that the connection holds.
     */
    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }
}
