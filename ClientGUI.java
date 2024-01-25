// ClientGUI.java
package Client.Pro1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientGUI {
    private JFrame f;
    private JTextField text;
    private JTextArea chatArea;
    private ClientSock clientSock;
    private ServerData serverData;


    public ClientGUI() {
        f = new JFrame();
        f.setLayout(null);

        // ... (unchanged code)

        // Header
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back icon
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/Client/Pro1/arrow.png"));
        Image backImage = backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon backImageIcon = new ImageIcon(backImage);
        JLabel back = new JLabel(backImageIcon);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);


        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f.setVisible(false);
            }
        });

        // Profile picture icon
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Client/Pro1/sujan.png"));
        Image profileImage = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING);
        ImageIcon profileImageIcon = new ImageIcon(profileImage);
        JLabel profile = new JLabel(profileImageIcon);
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        // Video call icon
        ImageIcon videoIcon = new ImageIcon(getClass().getResource("/Client/Pro1/video-call.png"));
        Image videoImage = videoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_AREA_AVERAGING);
        ImageIcon videoImageIcon = new ImageIcon(videoImage);

        JLabel video = new JLabel(videoImageIcon);
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        // Phone icon
        ImageIcon phoneIcon = new ImageIcon(getClass().getResource("/Client/Pro1/phone.png"));
        Image phoneImage = phoneIcon.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon phoneImageIcon = new ImageIcon(phoneImage);
        JLabel phone = new JLabel(phoneImageIcon);
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        // More option icon
        ImageIcon moreIcon = new ImageIcon(getClass().getResource("/Client/Pro1/dots-3.png"));
        Image moreImage = moreIcon.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon moreImageIcon = new ImageIcon(moreImage);
        JLabel option = new JLabel(moreImageIcon);
        option.setBounds(420, 20, 10, 25);
        p1.add(option);

        // Display name
        JLabel name = new JLabel("Bibek Raut");
        name.setBounds(110, 20, 120, 15);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        // Status online or offline
        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 40, 120, 15);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
        p1.add(status);

        // Message display area
        JPanel a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setBackground(Color.WHITE);
        a1.setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        a1.add(new JScrollPane(chatArea));
        f.add(a1);
        // writing boundry
        a1.setBounds(5,75, 440, 570);
        f.add(a1);

        // Footer
        // Textfield
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        // Send button
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this::sendMessageAction);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);

        // Initialize ClientSock and start the client
        clientSock = new ClientSock(this);
        clientSock.startClient();
        // Initialize ServerData
        serverData = new ServerData();

        // Display stored messages in the chat area
        displayStoredMessages();

        f.setVisible(true);

    }

    public static JPanel formatLabel(String message,String sender,boolean isServer){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html> <p style=\"width: 150 px\">"+ message + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN,16));
        output.setBackground(new Color(37,211,102));
        output.setOpaque(true);
//        output.setBorder(new EmptyBorder(15,15,15,50));

        output.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (isServer) {
            output.setBackground(new Color(37, 211, 102)); // Server message color
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            output.setBackground(new Color(7, 94, 84)); // Client message color
            panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        }

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }


    void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
    private void sendMessageAction(ActionEvent e) {
        if (e.getActionCommand().equals("Send")) {
            String message = text.getText().trim();
            if (!message.isEmpty()) {
                // Append the sent message to the display area
//                appendMessage("Client:"+ message);
                // Send the message to the server
                clientSock.sendMessage(message);
                // Store the message in the database
                storeMessage("Client", message);
                // Clear the textfield
                text.setText("");
            }
        }
    }

    void storeMessage(String sender, String message) {
        serverData.storeMessage(sender, message);
    }

    void displayStoredMessages() {
        for (String message : serverData.getMessages()) {
            appendMessage(message);
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ClientGUI());
    }

    public void setClientSock(ClientSock clientSock) {
    }
}
