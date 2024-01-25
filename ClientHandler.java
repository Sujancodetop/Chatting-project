package Client.Pro1;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ServerSock server;
    private ServerData serverData;

    public ClientHandler(Socket socket, ServerSock server, ServerData serverData) {
        this.socket = socket;
        this.server = server;
        this.serverData = serverData;

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = (String) objectInputStream.readObject();
                System.out.println("Received message: " + message);

                // Extract sender from the message
                String sender = message.split(":")[0].trim();
                // Store the received message in the database
                serverData.storeMessage("server", message);

                // Broadcast the received message to all clients
                server.broadcastMessage(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
                server.removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
