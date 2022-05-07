package Graphical;

import DataTypes.Group;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MainWindow extends JFrame {

    private JTable table = new JTable();
    static ArrayList<Group> groups = new ArrayList<>();

    public MainWindow(){
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
            createFile();
        });
        topPanel.add(inputButton);
        topPanel.add(viewCombo);
        topPanel.add(outputButton);
        return topPanel;
    }

    private void createFile(){
        JFrame choiceWindow = new JFrame("Choose an action");
        JPanel choicePanel = new JPanel(null);
        choiceWindow.setSize(500,200);
        choiceWindow.setLocation(800,300);
        choiceWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        choiceWindow.add(choicePanel);
        choiceWindow.setVisible(true);
        JLabel label = new JLabel("Оберіть, з чим бажаєте працювати: ");
        JButton groupButton = new JButton("Група товарів");
        JButton productButton = new JButton("Товар");

        groupButton.setBounds(25,100,200,50);
        productButton.setBounds(262,100,200,50);
        label.setLocation(80,10);
        label.setFont(new Font("Arial", Font.PLAIN,20));
        label.setSize(500,30);
        groupButton.addActionListener(press -> {
            /* action listener for Group button */
            choiceWindow.dispose();
        });
        productButton.addActionListener(press -> {
            /* action listener for Product button */
            choiceWindow.dispose();
            try {
                productAction();
            } catch (EmptyGroupsException e) {
                e.printStackTrace();
            }
        });
        choicePanel.add(label);
        choicePanel.add(groupButton);
        choicePanel.add(productButton);
    }

    private void productAction() throws EmptyGroupsException{
        if(groups.isEmpty()) throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
        JFrame action = new ProductActions();
        action.setVisible(true);
    }

    /**Додавання файлу з групою до списку*/
    private static void addFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (file != null) {
                try {
                    Group group = new Group(file);
                    groups.add(group);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}

class EmptyGroupsException extends Exception{

    EmptyGroupsException(){}

    EmptyGroupsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодної групи","Error",  JOptionPane.ERROR_MESSAGE);
    }
}