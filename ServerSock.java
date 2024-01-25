// ServerSock.java
package Client.Pro1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSock {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private ServerData serverData;
    private ServerGUI serverGUI;

    public ServerSock(ServerData serverData, ServerGUI serverGUI) {
        this.serverData = serverData;;
        this.serverGUI = serverGUI;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(3306);
            serverGUI.appendLog("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                serverGUI.appendLog("New client connected: " + socket);

                // Create a new ClientHandler thread for each connected client
                ClientHandler clientHandler = new ClientHandler(socket, this, serverData);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler otherClient : clients) {
            if (otherClient != clients) {
                String formattedMessage = sender + ": " + message;
                otherClient.sendMessage(formattedMessage);
                serverGUI.appendLog(formattedMessage);
            }
        }
        // Store messages received from clients
        serverData.storeMessage("Server", message);
    }
//    public void sendMessage(String sender, String message) {
//        try {
//            ObjectOutput objectOutputStream = null;
//            objectOutputStream.writeObject(sender + ": " + message);
//            objectOutputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        ServerData serverData = new ServerData();
        ServerGUI serverGUI = new ServerGUI();
        ServerSock serverSock = new ServerSock(serverData, serverGUI);  // Provide both ServerData and ServerGUI
        serverGUI.setServerSock(serverSock);
        serverSock.startServer();
    }
}
