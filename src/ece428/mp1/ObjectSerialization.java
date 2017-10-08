package ece428.mp1;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

public class ObjectSerialization {

    private HashMap<NodeID, MembershipListEntry> listEntries;
    private String content;

    public ObjectSerialization(final MembershipList membershipList) {
        final StringBuilder builder = new StringBuilder();
        final Iterator it = membershipList.listEntries.entrySet().iterator();
        while (it.hasNext()) {
            final HashMap.Entry pair = (HashMap.Entry) it.next();
            final NodeID nodeID = (NodeID) pair.getKey();
            final MembershipListEntry entry = membershipList.listEntries.get(nodeID);

            builder
                    .append(nodeID.getStartTime()).append(",")
                    .append(nodeID.getIPAddress().getHostName()).append("|")
                    .append(entry.getHeartBeatCounter()).append(",")
                    .append(entry.getLocalTime()).append(",")
                    .append(entry.getAlive()).append("`");

            this.content = builder.toString();
        }
    }

    public ObjectSerialization(final String content) throws IOException {
        this.listEntries = new HashMap<NodeID, MembershipListEntry>();
        final String[] elements = content.split("`");

        for (final String str : elements) {
            System.out.println(str);
            System.out.println("test");
            final String[] pair = str.split("\\|");
            final String nodeID = pair[0];
            final String entry = pair[1];

            final String[] nodeSplit = nodeID.split("\\,");
            final String[] entrySplit = entry.split("\\,");

            final String nodeStartTime = nodeSplit[0];
            final String IPAddress = nodeSplit[1];

            final String heartBeatCount = entrySplit[0];
            final String entryLocalTime = entrySplit[1];
            final String isAlive = entrySplit[2];

            final NodeID nodeIDKey = new NodeID(
                    InetAddress.getByName(IPAddress),
                    Long.parseLong(nodeStartTime)
            );
            final MembershipListEntry membershipListEntry = new MembershipListEntry(
                    Integer.parseInt(heartBeatCount),
                    Long.parseLong(entryLocalTime),
                    Boolean.parseBoolean(isAlive)
            );

            this.listEntries.put(nodeIDKey, membershipListEntry);
        }

    }

    private void printStringArr(final String[] arr) {
        for (final String str : arr) {
            System.out.println(str);
        }
    }

    public MembershipList getMembershipList() {
        return new MembershipList(this.listEntries);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
