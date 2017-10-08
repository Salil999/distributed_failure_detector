package ece428.mp1;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class MembershipList {

    ConcurrentHashMap<NodeID, MembershipListEntry> listEntries;

    public MembershipList() {
        this.listEntries = new ConcurrentHashMap<NodeID, MembershipListEntry>();
    }

    public MembershipList(final ConcurrentHashMap<NodeID, MembershipListEntry> listEntries) {
        this.listEntries = listEntries;
    }

    public static long getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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

    public void updateEntries(final MembershipList other) {
        final Iterator it = other.listEntries.entrySet().iterator();
        while (it.hasNext()) {
            final ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry) it.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry otherEntry = other.listEntries.get(otherKey);
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
//            System.out.println(otherKey.getIPAddress());
//            System.out.println(otherKey.getStartTime());
            if (thisEntry != null && thisEntry.getAlive()) {
                final int otherHeartBeatCount = otherEntry.getHeartBeatCounter();
                final int thisHeartBeatCount = thisEntry.getHeartBeatCounter();

                if (thisEntry.getHeartBeatCounter() < 0) {
                    if (otherEntry.getHeartBeatCounter() == 0) {
                        this.addNewNode(otherKey, 0);
                    }
                } else if (otherHeartBeatCount > thisHeartBeatCount) {
                    thisEntry.setHeartBeatCounter(otherHeartBeatCount);
                    thisEntry.updateLocalTime();
                }

            } else if (otherEntry.getAlive()) {
                this.addNewNode(otherKey, 0);
            }
        }

        final Iterator i = this.listEntries.entrySet().iterator();
        while (i.hasNext()) {
            final ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry) i.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
            if (getCurrentTime() - thisEntry.getLocalTime() > 3000) {
                thisEntry.setAlive(false);
                if (getCurrentTime() - thisEntry.getLocalTime() > 6000) {
                    thisEntry.setHeartBeatCounter(-1);
                }
            } else {
                thisEntry.setAlive(true);
            }
        }
    }

    public void incrementHeartBeatCount(final NodeID nodeID) {
        final MembershipListEntry entry = this.listEntries.get(nodeID);
        entry.setHeartBeatCounter(entry.getHeartBeatCounter() + 1);
        this.listEntries.put(nodeID, entry);
    }
}
