package ece428.mp1;


import java.io.IOException;

public class Servent {
    MembershipList membershipList;
    SendThread sendThread;
    ReceiveThread receiveThread;

    public Servent() {
        this.membershipList = new MembershipList();
    }

    public Servent(final String hostName, final Integer port) {
        this.membershipList = new MembershipList();
    }

    public void startServent() throws IOException, InterruptedException {
        this.sendThread = new SendThread();
        this.receiveThread = new ReceiveThread();
        this.sendThread.call();
//        this.receiveThread.call();
    }

    public void closeConnection() throws IOException {
    }

}
