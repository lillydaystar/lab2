package Graphical;

import DataTypes.Group;
import DataTypes.Store;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    private JTable table = new JTable();
    final Store store;

    public MainWindow(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.store = new Store();
//        init();
        this.add(createTopPanel(), "North");
        this.setVisible(true);
    }

  /*  private void init() {
        JPanel window = new JPanel(new GridLayout());
        this.setSize(555,555);
        this.add(window);
    }*/

    private JPanel createTopPanel() {
        String[] list = {"Store view", "first group", "second group"};
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton inputButton = new JButton("Input");
        JButton outputButton = new JButton("Output");
        JComboBox<String> viewCombo = new JComboBox<>(list);
        viewCombo.addActionListener(select -> {
            int a = viewCombo.getSelectedIndex();
            String s = viewCombo.getItemAt(a);
            if (s.equals(list[0])) {
                System.out.println("here");
                createStoreTable();
                this.revalidate();
                this.repaint();
            }
        });
        inputButton.addActionListener(press -> {
            /* action listener for Input button */
            addInfo();
        });
        outputButton.addActionListener(press -> {
            /* action listener for Output button */
        });
        topPanel.add(inputButton);
        topPanel.add(viewCombo);
        topPanel.add(outputButton);
        return topPanel;
    }

    private void createGroupTable(Group group) {

    }

    private void createStoreTable() {
        String column[] = {"Name", "Number of products", "Description"};
        String info[][] = new String[this.store.numberOfGroups()][3];
        for (int i = 0; i < this.store.numberOfGroups(); i++) {
            try {
                info[i] = new String[] {this.store.get(i).getName(),
                        "" + this.store.get(i).getNumberOfProducts(),
                        this.store.get(i).getDescription()};
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        this.table = new JTable(info, column);
//        this.table.setBounds(20, 80, 300, 400);
//        this.table.setLocation(50, 600);
        JScrollPane scroll = new JScrollPane(this.table);
        this.add(scroll);
    }

    private void addInfo(){
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
        label.setLocation(0,10);
        label.setFont(new Font("Arial", Font.PLAIN,20));
        label.setSize(500,30);
        groupButton.addActionListener(press -> {
            /* action listener for Group button */
            choiceWindow.dispose();
            groupAction();
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
        if(store.isEmpty()) throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
        JFrame action = new ProductActions(this);
        action.setVisible(true);
    }

    private void groupAction(){
        JFrame action = new GroupActions(this);
        action.setVisible(true);
    }
}