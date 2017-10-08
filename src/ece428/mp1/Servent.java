package ece428.mp1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class Servent {
    public static Integer SEND_PORT = 1234;
    public static Integer RECEIVE_PORT = 1235;
    public final NodeID INTRODUCER_NODE;
    protected final Integer MACHINE_NUMBER = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
    protected int membershipListSize;
    protected MembershipList membershipList;
    protected ArrayList<NodeID> heartBeatList;
    protected DatagramSocket socketClient;
    protected DatagramSocket serverSocket;
    protected NodeID self;


    public Servent() throws IOException {
        this.membershipList = new MembershipList();
        this.INTRODUCER_NODE = new NodeID(InetAddress.getByName("fa17-cs425-g39-01.cs.illinois.edu"));

        InetAddress inetAddress = null;
        try {
            if (this.MACHINE_NUMBER == 10) {
                inetAddress = InetAddress.getByName("fa17-cs425-g39-" + this.MACHINE_NUMBER.toString() + ".cs.illinois.edu");
            } else {
                inetAddress = InetAddress.getByName("fa17-cs425-g39-0" + this.MACHINE_NUMBER.toString() + ".cs.illinois.edu");
            }
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }

        this.self = new NodeID(inetAddress);
        this.membershipList.addNewNode(this.self);
        this.membershipList.addNewNode(this.INTRODUCER_NODE);
        this.membershipListSize = 2;

        this.serverSocket = new DatagramSocket(
                SEND_PORT,
                inetAddress
        );
    }


    public void startServent() {
        this.heartBeatList = getKNodes();
        startServer();
        heartBeat();
    }


    private synchronized void startServer() {
        new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        final byte[] incomingByteStream = new byte[(int) Math.pow(2, 20)];
                        final DatagramPacket incomingPacket = new DatagramPacket(
                                incomingByteStream, incomingByteStream.length
                        );

                        // THIS LINE IS BLOCKING
                        // It waits for this machine to receive some packet
                        Servent.this.serverSocket.receive(incomingPacket);
                        retrieveData(incomingPacket);
                    }
                } catch (final IOException e) {
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected synchronized void retrieveData(final DatagramPacket incomingPacket) throws IOException {
        final String data = new String(incomingPacket.getData());
        final MembershipList other = new ObjectSerialization(data).getMembershipList();
        other.listEntries.remove(this.self);
        this.membershipList.updateEntries(other);

        System.out.println(Servent.this.membershipList.toString());
    }


    private synchronized void heartBeat() {
        new Thread() {
            @Override
            public synchronized void run() {
                try {
                    while (true) {
                        Servent.this.heartBeatList = getKNodes();
                        Servent.this.membershipList.incrementHeartBeatCount(Servent.this.self);
                        for (final NodeID nodeID : Servent.this.heartBeatList) {
                            heartBeat(nodeID);
                        }
                        Thread.sleep(3000);
                    }
                } catch (final InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }


    protected synchronized ArrayList<NodeID> getKNodes() {
        final ArrayList<NodeID> allKeys = new ArrayList<NodeID>(this.membershipList.listEntries.keySet());
        allKeys.remove(this.self);
        if (allKeys.size() <= 5) {
            return allKeys;
        }
        final ArrayList<NodeID> returnList = new ArrayList<NodeID>();
        final Random rand = new Random();
        allKeys.remove(this.INTRODUCER_NODE);
        for (int i = 0; i < 4; i++) {
            returnList.add(allKeys.remove(rand.nextInt(allKeys.size())));
        }
        returnList.add(this.INTRODUCER_NODE);
        return returnList;
    }


    private synchronized void heartBeat(final NodeID nodeID) {
        try {
            Servent.this.socketClient = new DatagramSocket(
                    RECEIVE_PORT,
                    this.self.getIPAddress()
            );

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
