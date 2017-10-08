package ece428.mp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Introducer extends Servent {
    public static final String INTRODUCER_IP = "fa17-cs425-g39-01.cs.illinois.edu";
    protected PriorityQueue<NodeID> priorityQueue;

    public Introducer() throws IOException {
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
        this.priorityQueue = new PriorityQueue<NodeID>(new Comparator<NodeID>() {
            @Override
            public int compare(final NodeID n1, final NodeID n2) {
                if (n1.getStartTime() < n2.getStartTime()) {
                    return -1;
                }
                return 1;
            }
        });
        this.heartBeatList = getKNodes();

        this.serverSocket = new DatagramSocket(
                SEND_PORT,
                inetAddress
        );

    }

    @Override
    protected ArrayList<NodeID> getKNodes() {
        final ArrayList<NodeID> returnList = new ArrayList<NodeID>();
        for (int i = 0; i < 5; i++) {
            if (this.priorityQueue.size() == 0) {
                break;
            }
            returnList.add(this.priorityQueue.poll());
        }
        return returnList;
    }

    @Override
    protected void retrieveData(final DatagramPacket incomingPacket) throws IOException {
        super.retrieveData(incomingPacket);
        this.priorityQueue.clear();

        final Iterator it = this.membershipList.listEntries.entrySet().iterator();
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID key = (NodeID) pair.getKey();
            this.priorityQueue.add(key);
        }
    }
}

