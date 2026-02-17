import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

class ChatClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton exitButton;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatClientGUI().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() throws IOException {
        // Set up the GUI
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Text area to display the chat messages
        textArea = new JTextArea();
        textArea.setEditable(false); // Don't allow editing
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for input field and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textField = new JTextField();
        panel.add(textField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(sendButton, BorderLayout.EAST);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitChat();
            }
        });
        panel.add(exitButton, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);

        // Connect to the server
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Start a thread to listen for incoming messages
        new Thread(new MessageListener(in)).start();
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.trim().isEmpty()) {
            out.println(message);  // Send the message to the server
            textField.setText("");  // Clear the input field
        }
    }

    private void exitChat() {
        out.println("exit");  // Send the exit message to the server
        
        // Close the socket and streams properly
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
        
        System.exit(0);
    }

    // Listener thread that reads messages from the server and updates the text area
    private class MessageListener implements Runnable {
        private BufferedReader in;

        public MessageListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append(message + "\n"); // Append the message to the text area
                    textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}