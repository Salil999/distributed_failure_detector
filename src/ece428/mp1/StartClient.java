package ece428.mp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class StartClient {
    public static void main(String[] args) throws Exception {
        BufferedReader temp = new BufferedReader(new FileReader("../number.txt"));
        int exclude = Integer.parseInt(temp.readLine());
        ArrayList<GrepClient> grepClientArrayList = new ArrayList<GrepClient>();
        for (int i = 1; i <= 10; i + n +) {
            if (i != exclude) {
                String host = "fa17-cs425-g39-0" + Integer.toString(i) + ".cs.illinois.edu";
                if (i == 10) {
                    host = "fa17-cs425-g39-" + Integer.toString(i) + ".cs.illinois.edu";
                }
                GrepClient grepClient = new GrepClient(host, 9090);
                grepClientArrayList.add(grepClient);
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String cmd = scanner.nextLine();
            int count = 0;
            long startTime = System.nanoTime();
            for (int i = 0; i < grepClientArrayList.size(); ++i) {
                GrepClient current = grepClientArrayList.get(i);
                if (current.isAvailable() || current.openConnection()) {
                    current.writeData(cmd);
                    System.out.println("Server: " + Integer.valueOf(i + 1));
                    count += current.readData();
                    System.out.println();
                }
            }
            long endTime = System.nanoTime();
            System.out.println("Total lines: " + Integer.valueOf(count));
//            System.out.println((endTime - startTime) / 1000000 + " milliseconds");
        }
    }
}
