package ece428.mp1;

import java.time.LocalDateTime;

public class MembershipListEntry {
    NodeID nodeID;
    LocalDateTime startTime;

    public MembershipListEntry(final String remoteEntry) {

    }
    // long epoch = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
}
