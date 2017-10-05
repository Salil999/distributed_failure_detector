package ece428.mp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Servent {
    public static final int MIN_PORT_NUMBER = 1234;
    public static final int MAX_PORT_NUMBER = 1244;
    private final Integer machineNumber = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
    //    private final MembershipList membershipList;
    private final Connection connection;
    private DatagramSocket socketClient;
    private DatagramSocket serverSocket;

    public Servent() throws IOException {
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
        final int port = getAvailablePort();
        if (port == -1) {
            throw new BindException("All available ports are in use!");
        }
        this.connection = new Connection(inetAddress, port);
    }

    public static boolean isAvailablePort(final int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            return false;
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;

        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (final IOException e) {
                }
            }
        }
        return false;
    }

    public static Integer getAvailablePort() {
        for (int i = MIN_PORT_NUMBER; i <= MAX_PORT_NUMBER; ++i) {
            if (isAvailablePort(i)) {
                return i;
            }
        }
        return -1;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public MembershipList getMembershipList() {
//        return this.membershipList;
    }

    public void startServent() {
        startServer();
        startClient();
        System.out.println("Server running on port: " + this.connection.getPort());
    }

    private void sendMembershipList(final MembershipList membershipList) {

    }

    private void startServer() {
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
                    while (true) {
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
                    }
                } catch (final IOException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
