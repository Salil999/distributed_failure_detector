package ece428.mp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Callable;

public class ReceiveThread implements Callable {
    final BufferedReader bufferedReader = new BufferedReader(new FileReader("../number.txt"));
    private final Connection connection;

    public ReceiveThread() throws IOException {
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.bufferedReader.readLine() + ".cs.illinois.edu"),
                1234,
                new DatagramSocket(1234),
                new byte[4096]
        );
    }

    public ReceiveThread(final byte[] byteStream) throws IOException {
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.bufferedReader.readLine() + ".cs.illinois.edu"),
                1234,
                new DatagramSocket(1234),
                byteStream
        );
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
