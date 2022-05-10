package Graphical;

import DataTypes.Group;
import DataTypes.Product;
import DataTypes.Store;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class MainWindow extends JFrame {

    private JTable table;
    private JPanel topPanel;
    private JScrollPane scroll;
    private Vector<String> list;
    final Store store;

    public MainWindow(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.store = new Store();
        createTopPanel();
        this.setVisible(true);
    }

    void createTopPanel() {
        try {
            if (this.scroll != null) this.remove(scroll);
            if (this.topPanel != null) this.remove(topPanel);
            if (this.table != null) this.table = null;
            this.list = new Vector<>(this.store.numberOfGroups() + 2);
            System.out.println(list.size());
            System.out.println(list.capacity());
            list.add("Store view");
            for (int i = 0; i < this.store.numberOfGroups(); i++)
                list.add(this.store.get(i).getName());
            list.add("<none>");

            this.topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton inputButton = new JButton("Input");
            JButton outputButton = new JButton("Output");
            JComboBox<String> viewCombo = new JComboBox<>(list);
            viewCombo.addActionListener(select -> {
                try {
                    int selectedIndex = viewCombo.getSelectedIndex();
                    if (selectedIndex == 0) {
                        if (this.scroll != null) this.remove(scroll);
                        createStoreTable();
                        this.revalidate();
                        this.repaint();
                    } else if (selectedIndex == list.size() - 1) {
                        if (this.table != null) {
                            System.out.println("hereee");
                            this.remove(scroll);
                            this.revalidate();
                            this.repaint();
                        }
                    } else {
                        Group selectedGroup = this.store.get(selectedIndex - 1);
                        if (this.scroll != null) this.remove(scroll);
                        createGroupTable(selectedGroup);
                        this.revalidate();
                        this.repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            inputButton.addActionListener(press -> {
                addInfo();
            });
            outputButton.addActionListener(press -> {
                /* action listener for Output button */
            });
            topPanel.add(inputButton);
            topPanel.add(viewCombo);
            topPanel.add(outputButton);
            this.add(this.topPanel, "North");
            this.revalidate();
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error while loading groups",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createGroupTable(Group group) {
        String column[] = {"Name", "Price", "Count", "Maker", "Description"};
        String info[][] = new String[group.getNumberOfProducts()][5];
        for (int i = 0; i < group.getNumberOfProducts(); i++) {
            Product product = group.getProduct(i);
            info[i] = new String[] {product.getName(), ""+product.getPrice(),
                                    ""+product.getCount()+" "+product.getEnding(),
                                    product.getMaker(), product.getDescription()};
        }
        this.table = new JTable(info, column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
    }

    private void createStoreTable() {
        String column[] = {"Name", "Number of products", "Description"};
        String info[][] = new String[this.store.numberOfGroups()][3];
        for (int i = 0; i < this.store.numberOfGroups(); i++) {
            try {
                System.out.println("Querying "+i+"th group");
                info[i] = new String[] {this.store.get(i).getName(),
                        "" + this.store.get(i).getNumberOfProducts(),
                        this.store.get(i).getDescription()};
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        this.table = new JTable(info, column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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