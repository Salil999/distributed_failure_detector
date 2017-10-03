package ece428.mp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Callable;

public class SendThread implements Callable {
    final BufferedReader bufferedReader = new BufferedReader(new FileReader("../number.txt"));
    private final Connection connection;

    public SendThread() throws IOException {
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.bufferedReader.readLine() + ".cs.illinois.edu"),
                9090,
                new DatagramSocket(9090),
                new byte[4096]
        );
    }

    public SendThread(final byte[] byteStream) throws IOException {
        this.connection = new Connection(
                InetAddress.getByName("fa17-cs425-g39-0" + this.bufferedReader.readLine() + ".cs.illinois.edu"),
                9090,
                new DatagramSocket(9090),
                new byte[4096]
        );
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
