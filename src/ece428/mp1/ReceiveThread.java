package ece428.mp1;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Callable;

public class ReceiveThread implements Callable {
    private Connection connection;

    public ReceiveThread() throws SocketException, UnknownHostException {
        this.connection.setByteStream(new byte[4096]);
        this.connection.setPort(1234);
        this.connection.setDatagramSocket(new DatagramSocket(this.connection.getPort()));
        this.connection.setHost(InetAddress.getByName("fa17-cs425-g39-01.cs.illinois.edu"));
    }

    public ReceiveThread(final byte[] byteStream) throws SocketException, UnknownHostException {
        this.connection.setByteStream(byteStream);
        this.connection.setPort(1234);
        this.connection.setDatagramSocket(new DatagramSocket(this.connection.getPort()));
        this.connection.setHost(InetAddress.getLocalHost());
    }

    @Override
    public Object call() throws IOException {
        DatagramPacket receivePacket;
        String membershipList;
        while (true) {
            // change this to membership list
            receivePacket = new DatagramPacket(this.connection.getByteStream(), this.connection.getByteStream().length);

            this.connection.getDatagramSocket().receive(receivePacket);

            membershipList = new String(receivePacket.getData());

            System.out.println("FROM SERVER:" + membershipList);
        }
    }
}
