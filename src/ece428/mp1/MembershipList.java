package ece428.mp1;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
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

    public synchronized static long getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public synchronized void addNewNode(final NodeID nodeID) {

        this.listEntries.put(
                nodeID,
                new MembershipListEntry()
        );
    }

    public synchronized void addNewNode(final NodeID nodeID, final int heartBeatCounter) {
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
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry otherEntry = other.listEntries.get(otherKey);
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
            if (thisEntry != null) {

                final int otherHeartBeatCount = otherEntry.getHeartBeatCounter();
                final int thisHeartBeatCount = thisEntry.getHeartBeatCounter();

                if (thisEntry.getAlive() && otherHeartBeatCount > thisHeartBeatCount) {
                    thisEntry.setHeartBeatCounter(otherHeartBeatCount);
                    thisEntry.updateLocalTime();
                }

//                System.out.println(otherKey.getIPAddress().getHostName() + " : " + this.listEntries.get(otherKey).getHeartBeatCounter());

                final long currentTime = getCurrentTime();
                if (currentTime - thisEntry.getLocalTime() >= 6000) {
//                    System.out.println(otherKey.getIPAddress().getHostName() + " failed");
                    thisEntry.setAlive(false);
                    if (currentTime - thisEntry.getLocalTime() >= 12000) {
//                        System.out.println(otherKey.getIPAddress().getHostName() + " removed");
                        it.remove();
                    }
                }
            } else if (otherEntry.getAlive()) {
                this.addNewNode(otherKey, otherEntry.getHeartBeatCounter());
            }
        }
    }


    public synchronized void incrementHeartBeatCount(final NodeID nodeID) {
        final MembershipListEntry entry = this.listEntries.get(nodeID);
        if (entry != null) {
            entry.setHeartBeatCounter(entry.getHeartBeatCounter() + 1);
            this.listEntries.put(nodeID, entry);
        }
    }
}
