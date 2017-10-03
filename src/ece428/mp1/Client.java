package ece428.mp1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.TimerTask;

public class Client implements Runnable {
    Thread t1;
    DataOutputStream dataOutputStream;
    Boolean isAvailable;
    MembershipList membershipList;
    DatagramSocket datagramSocket;
    private Connection connection;

    public Client() {
        this.membershipList = new MembershipList();
    }

    public Client(final String hostName, final Integer port) {
        this.connection = new Connection();
        this.connection.setHost(hostName);
        this.connection.setPort(port);
        this.membershipList = new MembershipList();
    }

    @Override
    public void run() {
    }

    private void clearVars() {
        this.dataOutputStream = null;
        this.connection = null;
    }

    private void heartbeatDetection() throws IOException {
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Test");
            }
        };
    }

    public void closeConnection() throws IOException {
    }

    public void openConnection() throws IOException {

        this.isAvailable = true;
    }

    public Boolean getAvailable() {
        return this.isAvailable;
    }
}
