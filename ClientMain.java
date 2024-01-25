package Client.pro1;

import Client.Pro1.ClientGUI;
import Client.Pro1.ClientSock;

public class ClientMain {
    public static void main(String[] args) {

        ClientGUI clientGUI = new ClientGUI();
        ClientSock clientSock = new ClientSock(clientGUI);
    }
}
