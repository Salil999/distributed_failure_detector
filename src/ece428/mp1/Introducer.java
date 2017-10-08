package ece428.mp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.*;

public class Introducer extends Servent {
    protected PriorityQueue<NodeID> priorityQueue;

    public Introducer() throws IOException {
        super();
        this.priorityQueue = new PriorityQueue<NodeID>(new Comparator<NodeID>() {
            @Override
            public int compare(final NodeID n1, final NodeID n2) {
                if (n1.getStartTime() < n2.getStartTime()) {
                    return -1;
                }
                return 1;
            }
        });
    }

    @Override
    protected ArrayList<NodeID> getKNodes() {
        final ArrayList<NodeID> returnList = new ArrayList<NodeID>();
        System.out.println(this.priorityQueue == null);
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

