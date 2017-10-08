package ece428.mp1;

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

    public int getNextAvailableSpot() {
        return 0;
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
        System.out.println("membership list size before: " + this.listEntries.size());
        final Iterator it = other.listEntries.entrySet().iterator();
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry otherEntry = other.listEntries.get(otherKey);
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
            if (thisEntry != null) {
                thisEntry.updateEntry(otherEntry);
            } else if (otherEntry.getAlive()) {
                System.out.println("not in list");
                this.addNewNode(otherKey, otherEntry.getHeartBeatCounter());
            }
        }
        System.out.println("membership list size after : " + this.listEntries.size());
    }

    public void incrementHeartBeatCount(final NodeID nodeID) {
        final MembershipListEntry entry = this.listEntries.get(nodeID);
        entry.setHeartBeatCounter(entry.getHeartBeatCounter() + 1);
        this.listEntries.put(nodeID, entry);
    }
}
