package ece428.mp1;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Callable;

public class SendThread implements Callable {
    private final Connection connection;

    public SendThread() throws SocketException, UnknownHostException {
        this.connection = new Connection(new DatagramSocket(1234), new byte[4096]);
        this.connection.setPort(1234);
        this.connection.setHost(InetAddress.getLocalHost());
    }

    public SendThread(final byte[] byteStream) throws SocketException, UnknownHostException {
        this.connection = new Connection(new DatagramSocket(1234), byteStream);
        this.connection.setPort(1234);
        this.connection.setHost(InetAddress.getLocalHost());
    }

    @Override
    public Object call() throws IOException, InterruptedException {
        while (true) {
            Thread.sleep(500);
            System.out.println("Sending packet...");
            // change this to membership list
            final String capitalizedSentence = "send_packet_test";
            this.connection.setByteStream(capitalizedSentence.getBytes());

            // for each of the monitors monitoring heartbeats, get ip and port
            final DatagramPacket sendPacket = new DatagramPacket(
                    this.connection.getByteStream(),
                    this.connection.getByteStream().length,
                    this.connection.getHost(),
                    this.connection.getPort());

            this.connection.getDatagramSocket().send(sendPacket);
        }
    }
}
