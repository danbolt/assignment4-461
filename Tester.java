public class Tester {
    public stati void main(String [] args) {
        ClientConnection c = new ClientConnection(InetAddress.getByName("224.0.0.1"), InetAddress.getLocalHost(), 8000, "Bob");
        Thread t = new Thread(c);

    }
}