import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FontChooserDialog extends JDialog {
    private JComboBox<String> fontFamilyComboBox;
    private JComboBox<String> fontStyleComboBox;
    private JComboBox<Integer> fontSizeComboBox;
    private Font selectedFont;

    public FontChooserDialog(JFrame parent, Font initialFont) {
        super(parent, "Font Chooser", true);

        selectedFont = initialFont;

        JLabel fontFamilyLabel = new JLabel("Font Family:");
        String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontFamilyComboBox = new JComboBox<>(fontFamilies);
        fontFamilyComboBox.setSelectedItem(initialFont.getFamily());

        JLabel fontStyleLabel = new JLabel("Font Style:");
        String[] fontStyles = {"Plain", "Bold", "Italic"};
        fontStyleComboBox = new JComboBox<>(fontStyles);
        fontStyleComboBox.setSelectedItem(getFontStyleName(initialFont.getStyle()));

        JLabel fontSizeLabel = new JLabel("Font Size:");
        Integer[] fontSizes = {12, 14, 16, 18, 20};
        fontSizeComboBox = new JComboBox<>(fontSizes);
        fontSizeComboBox.setSelectedItem(initialFont.getSize());

        JButton selectButton = new JButton("Select");
        JButton cancelButton = new JButton("Cancel");

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fontFamily = (String) fontFamilyComboBox.getSelectedItem();
                int fontStyle = getFontStyleValue((String) fontStyleComboBox.getSelectedItem());
                int fontSize = (Integer) fontSizeComboBox.getSelectedItem();

                selectedFont = new Font(fontFamily, fontStyle, fontSize);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(fontFamilyLabel);
        panel.add(fontFamilyComboBox);
        panel.add(fontStyleLabel);
        panel.add(fontStyleComboBox);
        panel.add(fontSizeLabel);
        panel.add(fontSizeComboBox);
        panel.add(selectButton);
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    private String getFontStyleName(int fontStyle) {
        if (fontStyle == Font.BOLD) {
            return "Bold";
        } else if (fontStyle == Font.ITALIC) {
            return "Italic";
        } else {
            return "Plain";
        }
    }

    private int getFontStyleValue(String fontStyleName) {
        if (fontStyleName.equals("Bold")) {
            return Font.BOLD;
        } else if (fontStyleName.equals("Italic")) {
            return Font.ITALIC;
        } else {
            return Font.PLAIN;
        }
    }

    public Font getSelectedFont() {
        return selectedFont;
    }
}