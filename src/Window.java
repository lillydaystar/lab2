

import DataTypes.Group;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Window extends JFrame {

    private JTable table = new JTable();

    Window(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        init();
        this.add(createTopPanel(), "North");
        this.setVisible(true);
    }

    private void init() {
        JPanel window = new JPanel(new GridLayout());
        this.setSize(555,555);
        this.add(window);
    }

    private JPanel createTopPanel() {
        String[] list = {"Store view", "first group", "second group"};
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton inputButton = new JButton("Input");
        JButton outputButton = new JButton("Output");
        JComboBox<String> viewCombo = new JComboBox<>(list);
        viewCombo.addActionListener(select -> {
            int a = viewCombo.getSelectedIndex();
            String s = viewCombo.getItemAt(a);
            System.out.println("Selected item number "+a+" which is "+s);
        });
        inputButton.addActionListener(press -> {
            /* action listener for Input button */
            addFile();
        });
        outputButton.addActionListener(press -> {
            /* action listener for Output button */
        });
        topPanel.add(inputButton);
        topPanel.add(viewCombo);
        topPanel.add(outputButton);
        return topPanel;
    }

    private static void addFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (file != null) {
                try {
                    Group group = new Group(file);
                    Tester.groups.add(group);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
