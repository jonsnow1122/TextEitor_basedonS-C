import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class SimpleEditor1 {
    private JFrame frame;
    private JTextPane textPane;
    private ImageIcon selectedImage;

    public SimpleEditor1() {
        // 创建主窗口
        frame = new JFrame("Simple Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem insertImageItem = new JMenuItem("Insert Image");
        JMenuItem deleteImageItem = new JMenuItem("Delete Image");
        fileMenu.add(insertImageItem);
        fileMenu.add(deleteImageItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // 创建文本编辑区域
        textPane = new JTextPane();
        textPane.setEditable(true);

        // 添加插入图像菜单项的动作监听器
        insertImageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int choice = fileChooser.showOpenDialog(frame);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    insertImage(file);
                }
            }
        });

        // 添加删除图像菜单项的动作监听器
        deleteImageItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedImage();
            }
        });

        // 添加鼠标事件监听器，用于调整图像大小
        textPane.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter new size (width, height in pixels):");
                if (input != null) {
                    String[] sizeTokens = input.split(" ");
                    if (sizeTokens.length == 2) {
                        try {
                            int width = Integer.parseInt(sizeTokens[0]);
                            int height = Integer.parseInt(sizeTokens[1]);
                            if (width > 0 && height > 0) {

                                Image image = selectedImage.getImage();

                                image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                                selectedImage = new ImageIcon(image);

                                MutableAttributeSet attrs = new SimpleAttributeSet();
                                attrs.addAttribute(StyleConstants.IconAttribute, selectedImage);
                                textPane.getStyledDocument().setCharacterAttributes(textPane.getSelectionStart(),
                                        textPane.getSelectionEnd() - textPane.getSelectionStart(), attrs, false);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Invalid size input!");
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid size input!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid size input!");
                    }
                }
            }
        });

        frame.add(new JScrollPane(textPane), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void insertImage(File file) {
        ImageIcon imageIcon = new ImageIcon(file.getPath());
        Image image = imageIcon.getImage();

        // 调整图像大小
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width > 600 || height > 400) {
            double scale = Math.min(600.0 / width, 400.0 / height);
            width = (int) (width * scale);
            height = (int) (height * scale);
            image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }

        // 创建图像样式
        Style style = textPane.addStyle("imageStyle", null);
        StyleConstants.setIcon(style, new ImageIcon(image));

        // 插入带有图像样式的空白字符
        try {
            // textPane.setCaretPosition(caretPosition);
            textPane.getStyledDocument().insertString(textPane.getCaretPosition(), " ", style);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteSelectedImage() {
        int caretPosition = textPane.getCaretPosition();
        StyledDocument doc = textPane.getStyledDocument();
        Element element = doc.getCharacterElement(caretPosition);
        AttributeSet attr = element.getAttributes();

        if (attr != null && attr.getAttribute(StyleConstants.IconAttribute) != null) {
            int start = element.getStartOffset();
            int end = element.getEndOffset();
            try {
                doc.remove(start, end - start);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editSizeItem = new JMenuItem("Edit Size");
        editSizeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter new size (in pixels):");
                if (input != null) {
                    int size = Integer.parseInt(input);
                    Image image = selectedImage.getImage();
                    int width = image.getWidth(null);
                    int height = image.getHeight(null);
                    System.out.println(width);
                    System.out.println(height);
                    double scale = (double) size / Math.max(width, height);
                    int newWidth = (int) (width * scale);
                    int newHeight = (int) (height * scale);
                    image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    selectedImage = new ImageIcon(image);
                    MutableAttributeSet attrs = new SimpleAttributeSet();
                    attrs.addAttribute(StyleConstants.IconAttribute, selectedImage);
                    textPane.getStyledDocument().setCharacterAttributes(textPane.getSelectionStart(),
                            textPane.getSelectionEnd() - textPane.getSelectionStart(), attrs, false);
                }
            }
        });
        popupMenu.add(editSizeItem);
        return popupMenu;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimpleEditor();
            }
        });
    }
}
