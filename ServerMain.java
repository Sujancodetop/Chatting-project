package Client.Pro1;

public class ServerMain {
    public static void main(String[] args) {
        ServerData serverData = new ServerData();
        ServerGUI serverGUI = new ServerGUI();
        ServerSock serverSock = new ServerSock(serverData, serverGUI);
        // Start the server
        serverSock.startServer();
    }
}


