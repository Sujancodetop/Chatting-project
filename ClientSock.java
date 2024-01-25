// ClientSock.java
package Client.Pro1;

import java.io.*;
import java.net.Socket;
public class ClientSock {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private  ClientGUI clientGUI;


    public  ClientSock(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }


    public void startClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 3306);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Start a new thread to handle incoming messages
            new Thread(() -> {
                while (true) {
                    try {
                        String message = (String) objectInputStream.readObject();
                        clientGUI.appendMessage("Server: " +message);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(message);
//            // Append the sent message to the client's display area
//            clientGUI.appendMessage("Client: " + message);
            // Store the message in the database (client's own messages)
            clientGUI.storeMessage("Client", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void storeServerMessage(String message) {
        // Store messages received from the server
        clientGUI.storeMessage("Server", message);
        // Display the server message in the client interface
        clientGUI.appendMessage("Server"+message);
    }
}
