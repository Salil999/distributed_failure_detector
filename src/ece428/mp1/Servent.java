package ece428.mp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Servent {
    public static Integer SEND_PORT = 1234;
    public static Integer RECEIVE_PORT = 1235;
    protected final Integer MACHINE_NUMBER = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
    protected int membershipListSize;

    protected MembershipList membershipList;
    protected ArrayList<NodeID> heartBeatList;
    protected DatagramSocket socketClient;
    protected DatagramSocket serverSocket;
    protected InetAddress host;


    public Servent() throws IOException {
        this.membershipList = new MembershipList();
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName("fa17-cs425-g39-0" + this.MACHINE_NUMBER.toString() + ".cs.illinois.edu");
            if (this.MACHINE_NUMBER == 10) {
                inetAddress = InetAddress.getByName("fa17-cs425-g39-" + this.MACHINE_NUMBER.toString() + ".cs.illinois.edu");
            }
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        this.host = inetAddress;
        this.membershipList.addNewNode(new NodeID(this.host));
        this.membershipListSize = 1;
        this.heartBeatList = new ArrayList<NodeID>();

        this.serverSocket = new DatagramSocket(
                SEND_PORT,
                Servent.this.host
        );

        if (this.MACHINE_NUMBER == 1) {
            inetAddress = InetAddress.getByName("fa17-cs425-g39-02.cs.illinois.edu");
            this.heartBeatList.add(new NodeID(inetAddress));
        } else {
            inetAddress = InetAddress.getByName("fa17-cs425-g39-01.cs.illinois.edu");
            this.heartBeatList.add(new NodeID(inetAddress));
        }
    }

    private void getMembershipListFromIntroducer() {

    }


    public void startServent() {
        startServer();
        startClient();
    }


    private void startServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {

                        final byte[] incomingByteStream = new byte[(int) Math.pow(2, 20)];
                        final DatagramPacket incomingPacket = new DatagramPacket(
                                incomingByteStream, incomingByteStream.length
                        );

                        // THIS LINE IS BLOCKING
                        // It waits for this machine to receive some packet
                        Servent.this.serverSocket.receive(incomingPacket);
                        // incomingPacket now contains the contents of whatever the receiver sent

                        final String data = new String(incomingPacket.getData());
                        final MembershipList other = (new ObjectSerialization(data)).getMembershipList();

//                        System.out.println("Received from Client: " + other.toString());
                        System.out.println("Client IP: " + incomingPacket.getAddress());
                    }
                } catch (final IOException e) {
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void startClient() {
        new Thread() {
            @Override
            public void run() {
                final int port = RECEIVE_PORT;
                for (final NodeID nodeID : Servent.this.heartBeatList) {
                    try {
                        Servent.this.socketClient = new DatagramSocket(
                                port,
                                Servent.this.host
                        );

//                        final byte[] data = new Scanner(System.in).nextLine().getBytes();
                        final byte[] data = new ObjectSerialization(Servent.this.membershipList).toString().getBytes();
                        final DatagramPacket sendPacket = new DatagramPacket(
                                data, data.length,
                                nodeID.getIPAddress(),
                                SEND_PORT
                        );
                        Servent.this.socketClient.send(sendPacket);

                        Servent.this.socketClient.close();
                    } catch (final IOException e) {
                        System.err.println(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
