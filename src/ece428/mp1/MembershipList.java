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


    public void updateEntries(final MembershipList other) {
        final Iterator it = other.listEntries.entrySet().iterator();
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID otherKey = (NodeID) pair.getKey();
            final MembershipListEntry otherEntry = other.listEntries.get(otherKey);
            final MembershipListEntry thisEntry = this.listEntries.get(otherKey);
            if (thisEntry != null) {
                thisEntry.updateEntry(otherEntry);
            } else if (otherEntry.getAlive()) {
                this.addNewNode(otherKey, otherEntry.getHeartBeatCounter());
            }
        }
    }


}
