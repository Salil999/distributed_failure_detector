package ece428.mp1;


import java.io.IOException;
import java.net.SocketException;

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

    public void startServent() throws SocketException, IOException, InterruptedException {
        final SendThread sendThread = new SendThread();
        final ReceiveThread receiveThread = new ReceiveThread();
        sendThread.call();
        receiveThread.call();
    }

    public void closeConnection() throws IOException {
    }

}
