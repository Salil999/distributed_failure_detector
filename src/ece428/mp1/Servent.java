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
import java.util.Timer;
import java.util.TimerTask;

public class Servent {
    public static Integer SEND_PORT = 1234;
    public static Integer RECEIVE_PORT = 1235;
    protected final Integer MACHINE_NUMBER = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
    protected int membershipListSize;

    protected MembershipList membershipList;
    protected ArrayList<NodeID> heartBeatList;
    protected DatagramSocket socketClient;
    protected DatagramSocket serverSocket;
    protected NodeID self;


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

        this.self = new NodeID(inetAddress);
        this.membershipList.addNewNode(this.self);
        this.membershipListSize = 1;
        this.heartBeatList = getKNodes();

        this.serverSocket = new DatagramSocket(
                SEND_PORT,
                inetAddress
        );
    }


    public void startServent() {
        startServer();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Heartbeating");
                heartBeat();
            }
        }, 0, 500);
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
                        retrieveData(incomingPacket);
                    }
                } catch (final IOException e) {
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void retrieveData(final DatagramPacket incomingPacket) throws IOException {
        final String data = new String(incomingPacket.getData());
        final MembershipList other = new ObjectSerialization(data).getMembershipList();
        System.out.println("Received from Client: " + other.toString());
        this.membershipList.updateEntries(other);
    }


    private void heartBeat() {
        new Thread() {
            @Override
            public void run() {
                if (Servent.this.membershipList.listEntries.size() != Servent.this.membershipListSize) {
                    Servent.this.heartBeatList = getKNodes();
                    Servent.this.membershipListSize = Servent.this.membershipList.listEntries.size();
                }
                for (final NodeID nodeID : Servent.this.heartBeatList) {
                    heartBeat(nodeID);
                }
            }
        }.start();
    }


    protected ArrayList<NodeID> getKNodes() {
        final ArrayList<NodeID> allKeys = new ArrayList<NodeID>(this.membershipList.listEntries.keySet());
        allKeys.remove(this.self);
        if (allKeys.size() <= 5) {
            return allKeys;
        }
        final ArrayList<NodeID> returnList = new ArrayList<NodeID>();
        final Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            returnList.add(allKeys.remove(rand.nextInt(allKeys.size())));
        }
        return returnList;
    }


    private void heartBeat(final NodeID nodeID) {
        try {
            Servent.this.socketClient = new DatagramSocket(
                    RECEIVE_PORT,
                    this.self.getIPAddress()
            );

            this.membershipList.incrementHeartBeatCount(this.self);

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
