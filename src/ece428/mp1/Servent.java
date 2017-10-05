package ece428.mp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Servent {
    private final Integer machineNumber = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
    private final MembershipList membershipList;
    private final Connection connection;

    private DatagramSocket socketClient;
    private DatagramSocket serverSocket;

    public Servent() throws IOException {
        this.membershipList = new MembershipList();
        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName("fa17-cs425-g39-0" + this.machineNumber.toString() + ".cs.illinois.edu");
            if (this.machineNumber == 10) {
                inetAddress = InetAddress.getByName("fa17-cs425-g39-" + this.machineNumber.toString() + ".cs.illinois.edu");
            }
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        this.connection = new Connection(inetAddress, 1234);
    }

    public void startServent() {
        System.out.println(this.connection.getHost().getHostName());
        System.out.println(this.connection.getHost().getHostAddress());
        startServer();
        startClient();
    }

    public void startServer() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Starting Server...");
                final byte[] incomingByteStream = new byte[4096];
                try {
                    while (true) {
                        final DatagramPacket incomingPacket = new DatagramPacket(
                                incomingByteStream, incomingByteStream.length
                        );
                        Servent.this.serverSocket = new DatagramSocket(
                                Servent.this.connection.getPort(),
                                Servent.this.connection.getHost()
                        );
                        Servent.this.serverSocket.receive(incomingPacket);
                        final String message = new String(incomingPacket.getData());
                        System.out.println("Received from Client: " + message);

                        final byte[] data = "Thanks for the message!".getBytes();
                        final DatagramPacket outgoingPacket = new DatagramPacket(
                                data, data.length, incomingPacket.getAddress(), incomingPacket.getPort()
                        );
                        Servent.this.serverSocket.send(outgoingPacket);
                    }
                } catch (final IOException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public void startClient() {
        new Thread() {
            @Override
            public void run() {
                System.out.println("Starting Client...");
                final byte[] incomingByteStream = new byte[4096];
                try {
                    final int port = Servent.this.connection.getPort() + 1;
                    Servent.this.socketClient = new DatagramSocket(
                            port,
                            Servent.this.connection.getHost()
                    );
                    final byte[] data = "This is a message from client".getBytes();

                    final DatagramPacket sendPacket = new DatagramPacket(
                            data, data.length,
                            Servent.this.connection.getHost(), port
                    );
                    Servent.this.socketClient.send(sendPacket);

                    System.out.println("Message from client: ");
                    final DatagramPacket incomingPacket = new DatagramPacket(
                            incomingByteStream, incomingByteStream.length);
                    final String response = new String(incomingPacket.getData());
                    System.out.println(response);

                } catch (final IOException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
