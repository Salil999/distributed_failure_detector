package ece428.mp1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    DataOutputStream dataOutputStream;
    Boolean isAvailable;
    private Connection connection;

    public Client() {
    }

    public Client(final String hostName, final Integer port) {
        this.connection = new Connection();
        this.connection.setHost(hostName);
        this.connection.setPort(port);
    }

    private void clearVars() {
        this.dataOutputStream = null;
        this.connection = null;
    }

    public void closeConnection() throws IOException {
        try {
            this.connection.getSocket().close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.isAvailable = false;
    }

    public void openConnection() throws IOException {
        try {
            this.connection.setSocket(new Socket(this.connection.getHost(), this.connection.getPort()));
        } catch (final IOException e) {
            e.printStackTrace();
            this.isAvailable = false;
        }
        this.dataOutputStream = new DataOutputStream(this.connection.getSocket().getOutputStream());
        this.isAvailable = true;
    }

    public Boolean getAvailable() {
        return this.isAvailable;
    }
}
