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
    Integer number = Integer.parseInt(this.bufferedReader.readLine());

    public ReceiveThread() throws IOException {
        System.out.println(this.number);
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.number + ".cs.illinois.edu"),
                9090,
                new DatagramSocket(9090),
                new byte[4096]
        );
    }

    public ReceiveThread(final byte[] byteStream) throws IOException {
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.number + ".cs.illinois.edu"),
                9090,
                new DatagramSocket(9090),
                byteStream
        );
    }

    @Override
    public Object call() throws IOException {
        DatagramPacket receivePacket;
        String membershipList;
        System.out.println("ReceiveThread: " + this.connection.getHost());
        while (true) {
            // change this to membership list
            receivePacket = new DatagramPacket(this.connection.getByteStream(), this.connection.getByteStream().length);

            this.connection.getDatagramSocket().receive(receivePacket);

            membershipList = new String(receivePacket.getData());

            System.out.println("FROM SERVER:" + membershipList);
        }
    }
}
