package ece428.mp1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Introducer extends Servent {
    public static final String INTRODUCER_IP = "fa17-cs425-g39-01.cs.illinois.edu";

    public Introducer() throws IOException {
        super();
        Introducer.this.serverSocket = new DatagramSocket(
                SEND_PORT,
                Introducer.this.host
        );
    }

    @Override
    public void startServent() {
        startIntroducing();

    }

    public void assignPosition() {
        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        }.start();
    }

    public void startIntroducing() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        final byte[] incomingByteStream = new byte[(int) Math.pow(2, 20)];
                        final DatagramPacket incomingPacket = new DatagramPacket(
                                incomingByteStream, incomingByteStream.length
                        );

                        // THIS LINE IS BLOCKING
                        // It waits for this machine to receive some packet
                        Introducer.this.serverSocket.receive(incomingPacket);
                        // incomingPacket now contains the contents of whatever the receiver sent

//                        System.out.println("Received from Client: " + new String(incomingPacket.getData()));
                        System.out.println("Received from Client: " + incomingPacket.getAddress());
                    }
                } catch (final IOException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
