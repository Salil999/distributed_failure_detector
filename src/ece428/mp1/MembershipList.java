package ece428.mp1;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;

public class MembershipList {

    HashMap<NodeID, MembershipListEntry> listEntries;

    public MembershipList() {
        this.listEntries = new HashMap<NodeID, MembershipListEntry>();
    }

    public MembershipList(final HashMap<NodeID, MembershipListEntry> listEntries) {
        this.listEntries = listEntries;
    }


    public void addNewNode(final NodeID nodeID) {

        this.listEntries.put(
                nodeID,
                new MembershipListEntry()
        );
    }

    public void addNewNode(final NodeID nodeID, final int heartBeatCounter) {
        this.listEntries.put(
                nodeID,
                new MembershipListEntry(heartBeatCounter)
        );
    }

    @Override
    public String toString() {
        String s = new ObjectSerialization(this).toString();
        s = "Node:\n\t" + s
                .replace("|", "\nEntry:\n\t")
                .replace(",", "\n\t")
                .replace("`", "\n\nNode:\n\t");
        return s;
    }

    public synchronized void updateEntries(final MembershipList other) {
        final Iterator it = other.listEntries.entrySet().iterator();
        final HashMap<NodeID, MembershipListEntry> entries = new HashMap<NodeID, MembershipListEntry>();
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry otherEntry = other.listEntries.get(otherKey);
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
            if (thisEntry != null) {
//                System.out.println("Difference: " + (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//                        - thisEntry.getLocalTime()));
//                if (otherEntry.getHeartBeatCounter() > thisEntry.getHeartBeatCounter()) {
//                    entries.put(otherKey, new MembershipListEntry(
//                            otherEntry.getHeartBeatCounter(),
//                            LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
//                            true,
//                            -1
//                    ));
//                }
                thisEntry.updateEntry(otherEntry, otherKey);
                if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        - thisEntry.getLocalTime() >= 3000) {
                    it.remove();
                }
            }
//            else {
//                if (otherEntry.getAlive() && otherEntry.getFailedTime() < 0) {
//                    this.addNewNode(otherKey, otherEntry.getHeartBeatCounter());
//                }
//            }


        }
//        final Iterator it2 = entries.entrySet().iterator();
//        while (it2.hasNext()) {
//            final HashMap.Entry pair = (HashMap.Entry) it.next();
//            final NodeID otherKey = (NodeID) pair.getKey();
//
//        }


    }

    public synchronized void incrementHeartBeatCount(final NodeID nodeID) {
        final MembershipListEntry entry = this.listEntries.get(nodeID);
        entry.setHeartBeatCounter(entry.getHeartBeatCounter() + 1);
        this.listEntries.put(nodeID, entry);
    }
}
