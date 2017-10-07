package ece428.mp1;

public class Main {


    public static void main(final String[] args) throws Exception {
//        final Integer machineNumber = Integer.parseInt(new BufferedReader(new FileReader("../number.txt")).readLine());
//        if (machineNumber == 1) {
//            final Introducer introducer = new Introducer();
//            introducer.startServent();
//        } else {
        final Servent servent = new Servent();
        servent.startServent();
//        }
    }
}
