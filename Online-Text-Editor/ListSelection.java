import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;

public class ListSelection extends JFrame {
    private JList<String> list;
    int index;
    // File[] files;
    String name;

    public int getindex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public ListSelection(String[] files) {
        setTitle("Choose your file");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.err.println(files);
        int filenum = files.length;
        // 创建一个字符串格式的List
        ArrayList<String> dataList = new ArrayList<String>();
        for (int i = 0; i < filenum; i++) {
            dataList.add(files[i]);
        }

        // 将List添加到JList中
        list = new JList<String>(dataList.toArray(new String[dataList.size()]));

        // 添加选择事件监听器
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    index = list.getSelectedIndex();
                    JOptionPane.showMessageDialog(ListSelection.this, "You selected item " + index);
                    name = files[index];
                }
            }
        });

        // 添加JList到窗口中
        JScrollPane scrollPane = new JScrollPane(list);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 显示窗口
        pack();
        setVisible(true);
    }
}
