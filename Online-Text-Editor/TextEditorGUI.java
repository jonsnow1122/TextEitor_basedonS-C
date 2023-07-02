import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class TextEditorGUI extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private String currentFile;
    private boolean isTextChanged;
    private JLabel statusLabel;

    public TextEditorGUI() {
        setTitle("Text Editor");

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        fileChooser = new JFileChooser();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        fileMenu.add(saveMenuItem);
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(this);
        fileMenu.add(saveAsMenuItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem findReplaceMenuItem = new JMenuItem("Find/Replace");
        findReplaceMenuItem.addActionListener(this);
        editMenu.add(findReplaceMenuItem);
        menuBar.add(editMenu);

        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontMenuItem = new JMenuItem("Font");
        fontMenuItem.addActionListener(this);
        formatMenu.add(fontMenuItem);
        JMenuItem colorMenuItem = new JMenuItem("Color");
        colorMenuItem.addActionListener(this);
        formatMenu.add(colorMenuItem);
        JMenuItem sizeMenuItem = new JMenuItem("Size");
        sizeMenuItem.addActionListener(this);
        formatMenu.add(sizeMenuItem);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);

        statusLabel = new JLabel();
        add(statusLabel, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        currentFile = null;
        isTextChanged = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditorGUI());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Save")) {
            saveFile();
        } else if (command.equals("Save As")) {
            saveFileAs();
        } else if (command.equals("Find/Replace")) {
            findAndReplace();
        } else if (command.equals("Font")) {
            changeFont();
        } else if (command.equals("Color")) {
            changeColor();
        } else if (command.equals("Size")) {
            changeSize();
        }
    }



    
    
    private void saveFile() {
        if (currentFile != null) { // File has been saved before
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                fileWriter.write(textArea.getText());
                fileWriter.close();
                isTextChanged = false;
                setStatus("File saved.");
            } catch (IOException e) {
                setStatus("Error saving file.");
            }
        } else { // Save file as a new file
            saveFileAs();
        }
    }

    private void saveFileAs() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(textArea.getText());
                fileWriter.close();
                currentFile = file.getAbsolutePath();
                isTextChanged = false;
                setStatus("File saved as " + currentFile);
            } catch (IOException e) {
                setStatus("Error saving file.");
            }
        }
    }

    private void findAndReplace() {
        // Add find/replace functionality
        JDialog findReplaceDialog = new JDialog(this, "Find/Replace", true);
        findReplaceDialog.setSize(600, 300);
        findReplaceDialog.setLayout(new GridLayout(4, 2));

        JLabel findLabel = new JLabel("Find:");
        JTextField findTextField = new JTextField();
        JLabel replaceLabel = new JLabel("Replace:");
        JTextField replaceTextField = new JTextField();

        JButton findButton = new JButton("Find");
        JButton replaceButton = new JButton("Replace");
        JButton replaceAllButton = new JButton("Replace All");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(findButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findText = findTextField.getText();
                String text = textArea.getText();
                int index = text.indexOf(findText);

                if (index != -1) {
                    textArea.setCaretPosition(index);
                    textArea.select(index, index + findText.length());
                }
            }
        });

        replaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findText = findTextField.getText();
                String replaceText = replaceTextField.getText();
                String text = textArea.getText();
                int index = textArea.getCaretPosition();

                if (index != -1 && text.regionMatches(index, findText, 0, findText.length())) {
                    textArea.replaceRange(replaceText, index, index + findText.length());
                }
            }
        });

        replaceAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findText = findTextField.getText();
                String replaceText = replaceTextField.getText();
                String text = textArea.getText();

                text = text.replace(findText, replaceText);
                textArea.setText(text);
            }
        });

        findReplaceDialog.add(findLabel);
        findReplaceDialog.add(findTextField);
        findReplaceDialog.add(replaceLabel);
        findReplaceDialog.add(replaceTextField);
        findReplaceDialog.add(new JLabel()); // Empty label placeholder
        findReplaceDialog.add(buttonPanel);

        findReplaceDialog.setVisible(true);
    }

    private void changeFont() {
    // Change font type
    Font currentFont = textArea.getFont();
    FontChooserDialog fontChooser = new FontChooserDialog(this, currentFont);
    Font newFont = fontChooser.getSelectedFont();
    if (newFont != null) {
        textArea.setFont(newFont);
    }
}

    private void changeColor() {
        // Change text color
        Color currentColor = textArea.getForeground();
        Color newColor = JColorChooser.showDialog(this, "Choose Color", currentColor);
        if (newColor != null) {
            textArea.setForeground(newColor);
        }
    }

    private void changeSize() {        // Change text size
        String selectedSize = (String) JOptionPane.showInputDialog(
                this,
                "Select new font size:",
                "Font Size",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"12", "14", "16", "18", "20"},
                "12");

        if (selectedSize != null) {
            int newSize = Integer.parseInt(selectedSize);
            Font currentFont = textArea.getFont();
            Font newFont = currentFont.deriveFont((float) newSize);
            textArea.setFont(newFont);
        }
    }

    private void setStatus(String status) {
        statusLabel.setText(status);
    }

    private class NetworkThread extends Thread {
        private Socket socket;

        public NetworkThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                isr.close();
                br.close();
                socket.close();

                SwingUtilities.invokeLater(() -> textArea.setText(sb.toString()));
                setStatus("File loaded successfully.");
            } catch (IOException e) {
                setStatus("Error loading file.");
            }
        }
    }

    private void loadFileFromNetwork(String serverAddress, int port) {
        try {
            Socket socket = new Socket(serverAddress, port);
            NetworkThread networkThread = new NetworkThread(socket);
            networkThread.start();
        } catch (IOException e) {
            setStatus("Error connecting to server.");
        }
    }

    private void insertImage() {
        // Add image insertion functionality
    }

    private void deleteImage() {
        // Add image deletion functionality
    }

    private void adjustImageSize() {
        // Add image size adjustment functionality
    }
}
       