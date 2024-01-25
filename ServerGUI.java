// ServerGUI.java
package Client.Pro1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerGUI {
    private JFrame frame;
    private JTextArea logArea;
    private JTextArea serverInputArea;
    private ServerSock serverSock;

    public ServerGUI() {

        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setLayout(null);

        // Header
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        frame.add(p1);

        // Back icon
        ImageIcon backIcon = new ImageIcon(getClass().getResource("/Client/Pro1/arrow.png"));
        Image backImage = backIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon backImageIcon = new ImageIcon(backImage);
        JLabel back = new JLabel(backImageIcon);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.setVisible(false);
            }
        });

        // ... (unchanged code)
        // Profile picture icon
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Client/Pro1cuty.png"));
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
        JLabel name = new JLabel("Sunil Raut");
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
        logArea = new JTextArea();
        logArea.setEditable(false);
        a1.add(new JScrollPane(logArea));
        frame.add(a1);

        // Footer
        // Textfield
        serverInputArea = new JTextArea();
        serverInputArea.setBounds(5, 655, 310, 40);
        serverInputArea.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        frame.add(serverInputArea);

        //send button
        JButton send = new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(7,94,84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this::sendToClientsAction);
        send.setFont(new Font ("SAN_SERIF", Font.PLAIN, 16));
        frame.add(send);

        frame.setSize(450, 700);
        frame.setLocation(200, 50);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);

        // Initialize ServerSock and start the server
        serverSock = new ServerSock(new ServerData(), this);
        serverSock.startServer();

        frame.setVisible(true);
    }
    public static JPanel formatLabel(String sender,String message, boolean isServer){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html> <p style=\"width: 150 px\">"+ message + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN,16));
        output.setBackground(new Color(16, 236, 98));
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

        if (!isServer) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            JLabel time = new JLabel();
            time.setText(sdf.format(cal.getTime()));
            time.setForeground(Color.GRAY);
            panel.add(time);
        }
        return panel;
    }


    private void sendToClientsAction(ActionEvent e) {
        String serverMessage = serverInputArea.getText();
        serverSock.broadcastMessage(serverMessage, null);
        // Store messages sent by the server
//        serverData.storeMessage("Server", serverMessage);
//        serverGUI.appendLog("Server: " + serverMessage);

        // Append the message to the server's log area
        appendLog("Server: " + serverMessage);

        serverInputArea.setText("");
    }

    public void setServerSock(ServerSock serverSock) {
        this.serverSock = serverSock;
    }

    public void appendLog(String log) {
        logArea.append(log + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI serverGUI = new ServerGUI();
        });
    }
}
