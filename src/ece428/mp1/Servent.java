package ece428.mp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

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
        startServer();

    }

    private void sendMembershipList(final MembershipList membershipList) {

    }

    public void startServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Servent.this.serverSocket = new DatagramSocket(
                            Servent.this.connection.getPort(),
                            Servent.this.connection.getHost()
                    );
                    while (true) {
                        final byte[] incomingByteStream = new byte[1048576];
                        final DatagramPacket incomingPacket = new DatagramPacket(
                                incomingByteStream, incomingByteStream.length
                        );

                        // THIS LINE IS BLOCKING
                        // It waits for this machine to receive some packet
                        Servent.this.serverSocket.receive(incomingPacket);
                        // incomingPacket now contains the contents of whatever the receiver sent

                        System.out.println("Received from Client: " + new String(incomingPacket.getData()));
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
                try {
                    final int port = Servent.this.connection.getPort() + 1;
                    Servent.this.socketClient = new DatagramSocket(
                            port,
                            Servent.this.connection.getHost()
                    );

                    final byte[] data = new Scanner(System.in).nextLine().getBytes();

                    final DatagramPacket sendPacket = new DatagramPacket(
                            data, data.length,
                            Servent.this.connection.getHost(), Servent.this.connection.getPort()
                    );
                    Servent.this.socketClient.send(sendPacket);

                    Servent.this.socketClient.close();
                } catch (final IOException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
