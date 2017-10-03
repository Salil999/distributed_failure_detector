package ece428.mp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StartClient {
    public static void main(final String[] args) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader("../number.txt"));
        final Integer exclude = Integer.parseInt(bufferedReader.readLine());
        final ArrayList<Client> clientList = new ArrayList<Client>();
        for (int i = 1; i <= 10; i++) {
            if (i != exclude) {
                String host = "fa17-cs425-g39-0" + Integer.toString(i) + ".cs.illinois.edu";
                if (i == 10) {
                    host = "fa17-cs425-g39-" + Integer.toString(i) + ".cs.illinois.edu";
                }
                final Client client = new Client(host, 9090);
                clientList.add(client);
            }
        }

    }
}
