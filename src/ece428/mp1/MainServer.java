package ece428.mp1;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
    private Connection connection;
    private ServerSocket serverSocket;

    public MainServer() {
    }

    public MainServer(final Integer port) {
        this.connection = new Connection();
        this.connection.setPort(port);
    }

    public void initialize() throws IOException {
        try {
            this.serverSocket = new ServerSocket(this.connection.getPort());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        this.connection.setSocket(this.serverSocket.accept());
    }

    public Connection getConnection() {
        return this.connection;
    }
}
