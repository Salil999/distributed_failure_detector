package ece428.mp1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GrepClient {
    Connection connection;
    private DataOutputStream dataOutputStream;
    private boolean isAvailable = false;


    public GrepClient() {
    }


    /**
     * @return If the connection is available or not.
     */
    public boolean isAvailable() {
        return isAvailable;
    }


    /**
     * @param hostName The hostname that the client will connect to.
     * @param port     The port number that the client will connect to.
     * @throws Exception
     */
    public GrepClient(final String hostName, final Integer port) throws Exception {
        connection = new Connection();
        connection.setHost(hostName);
        connection.setPort(port);
    }


    /**
     * Clears the internal variables and sets them to null.
     */
    private void clearVars() {
        connection.setSocket(null);
        dataOutputStream = null;
        isAvailable = false;
    }


    /**
     * Opens a connection to the hostname and the port.
     * It is up to the programmer to open the connection
     * and close the connection with the methods provided in the class.
     *
     * @return Returns if the connection was successfully opened or not.
     * @throws Exception
     */
    public boolean openConnection() throws Exception {
        try {
            connection.setSocket(new Socket(connection.getHost(), connection.getPort()));
            dataOutputStream = new DataOutputStream(connection.getSocket().getOutputStream());
        } catch (final UnknownHostException e) {
            clearVars();
            return false;
        } catch (final IOException e) {
            clearVars();
            return false;
        } catch (final Exception e) {
            clearVars();
            return false;
        }
        isAvailable = connection.getSocket() != null &&
                      dataOutputStream != null;
        return isAvailable;
    }


    /**
     * Closes the connection.
     *
     * @throws Exception if the connection was never opened.
     */
    public void closeConnection() throws Exception {
        try {
            connection.getSocket().close();
            dataOutputStream.close();
        } catch (final Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        clearVars();
        isAvailable = false;
    }


    /**
     * This method sends data as a string.
     *
     * @throws Exception if the connection was not properly set.
     */
    public void writeData(final String cmd) throws Exception {
        if (!isAvailable) {
            throw new SocketException("Opening of the connection was not successful.");
        }
        dataOutputStream.writeUTF(cmd);
    }


    /**
     * Reads the data from all the servers and outputs it to the client's screen.
     *
     * @throws IOException
     */
    public int readData() throws IOException {
        int i = 0;
        String line;
        final DataInputStream dataInputStream = new DataInputStream(connection.getSocket().getInputStream());
        try {
            while (!(line = dataInputStream.readUTF()).equals("DONE")) {
                i++;
//                System.out.println(line);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println(Integer.valueOf(i) + " lines");
        return i;
    }


    /**
     * Getter for the connection object.
     *
     * @return The connection configuration for the client.
     */
    public Connection getConnection() {
        return connection;
    }
}
