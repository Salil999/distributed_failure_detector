package ece428.mp1;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Connection {
    private InetAddress host;
    private Integer port;
    private DatagramSocket datagramSocket;
    private byte[] byteStream;


    public Connection() {
    }

    public Connection(final DatagramSocket datagramSocket, final byte[] byteStream) {
        this.datagramSocket = datagramSocket;
        this.byteStream = byteStream;
    }

    public Connection(final InetAddress host, final Integer port) {
        this.host = host;
        this.port = port;
    }


    public Connection(final InetAddress host, final Integer port, final DatagramSocket datagramSocket, final byte[] byteStream) {
        this.host = host;
        this.port = port;
        this.datagramSocket = datagramSocket;
        this.byteStream = byteStream;
    }

    public DatagramSocket getDatagramSocket() {
        return this.datagramSocket;
    }

    public void setDatagramSocket(final DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public byte[] getByteStream() {
        return this.byteStream;
    }

    public void setByteStream(final byte[] byteStream) {
        this.byteStream = byteStream;
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
