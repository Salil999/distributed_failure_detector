package ece428.mp1;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class GrepServer {
    private Connection connection;
    private ServerSocket serverSocket;

    public GrepServer() {
    }


    public GrepServer(final Integer port) {
        connection = new Connection();
        connection.setPort(port);
    }


    /**
     * Initializes the server and binds it to the port.
     *
     * @throws IOException
     */
    public void initialize() throws IOException {
        try {
            serverSocket = new ServerSocket(connection.getPort());
        } catch (final IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    /**
     * @throws IOException
     */
    public void startServer() throws InterruptedException, IOException {
        String line;
        final TerminalParser terminalParser = new TerminalParser();
        try {
            connection.setSocket(serverSocket.accept());
            final DataInputStream dataInputStream = new DataInputStream(connection.getSocket().getInputStream());
            while (true) {
                line = dataInputStream.readUTF();
                terminalParser.setCommand(line);
                terminalParser.runCommand(connection);
            }
        } catch (final IOException e) {
            System.out.println(e.getLocalizedMessage());
            closeServer();
        }
    }


    /**
     * Closes the server and assigns variables to null for object de-referencing.
     *
     * @throws IOException
     */
    public void closeServer() throws IOException {
        if (connection != null && connection.getSocket() != null) {
            connection.getSocket().close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
        connection = null;
        serverSocket = null;
    }


    /**
     * @return The connection configuration for the client.
     */
    public Connection getConnection() {
        return connection;
    }
}
