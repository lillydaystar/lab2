package Graphical;

import DataTypes.Group;
import DataTypes.Product;
import DataTypes.Store;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MainWindow extends JFrame {

    private final static int STORE = 1;
    private final static int GROUP = 2;

    final static int WIDTH = 800;
    final static int HEIGHT = 600;

    private JTable table;
    private JPanel topPanel;
    private JPanel taskPanel;
    private JScrollPane scroll;
    private Vector<String> list;
    private Group currentGroup;
    final Store store;

    public MainWindow(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(Tools.getCenterLocation(WIDTH, HEIGHT));
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
            list.add("<none>");
            list.add("Store view");
            for (int i = 0; i < this.store.numberOfGroups(); i++)
                list.add(this.store.get(i).getName());

            this.topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton inputButton = new JButton("Input");
            JButton outputButton = new JButton("Output");
            JComboBox<String> viewCombo = new JComboBox<>(list);
            viewCombo.addActionListener(select -> {
                try {
                    int selectedIndex = viewCombo.getSelectedIndex();
                    if (list.get(selectedIndex).equals("Store view")) {
                        if (this.scroll != null) this.remove(scroll);
                        if (this.taskPanel != null) this.remove(this.taskPanel);
                        createStoreTable();
                        createTaskPanel(STORE);
                        this.revalidate();
                        this.repaint();
                    } else if (list.get(selectedIndex).equals("<none>")) {
                        if (this.table != null) {
                            if (this.scroll != null) this.remove(scroll);
                            if (this.taskPanel != null) this.remove(this.taskPanel);
                            this.currentGroup = null;
                            this.revalidate();
                            this.repaint();
                        }
                    } else {
                        Group selectedGroup = this.store.get(selectedIndex - 2);
                        if (this.scroll != null) this.remove(scroll);
                        if (this.taskPanel != null) this.remove(this.taskPanel);
                        createGroupTable(selectedGroup);
                        createTaskPanel(GROUP);
                        this.revalidate();
                        this.repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            inputButton.addActionListener(press -> addInfo());
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

    private void createTaskPanel(int choise) {
        if (choise != GROUP && choise != STORE)
            return;
        if (this.taskPanel != null) this.remove(taskPanel);
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(15);
        this.taskPanel = new JPanel(layout);
        this.taskPanel.setPreferredSize(new Dimension(90, 200));
        JButton azSortButton = new JButton("a->z");
        JButton zaSortButton = new JButton("z->a");
        JButton incrSortButton = new JButton("Incr");
        JButton decrSortButton = new JButton("Decr");
        JButton addButton = new JButton(choise == GROUP ? "Add" : "New");
        JButton findButton = new JButton(choise == GROUP ? "Select" : "Find");
        JButton removeButton = new JButton(choise == GROUP ? "Remove" : "Delete");
        JButton editButton = new JButton(choise == GROUP ? "Edit" : "Change");
        azSortButton.addActionListener(press -> {
            if (choise == GROUP) {
                this.currentGroup.sortByName(true);
                refreshGroup();
            } else {
                this.store.sortByName(true);
                refreshStore();
            }
        });
        zaSortButton.addActionListener(press -> {
            if (choise == GROUP) {
                this.currentGroup.sortByName(false);
                refreshGroup();
            } else {
                this.store.sortByName(false);
                refreshStore();
            }
        });
        incrSortButton.addActionListener(press -> {
            if (choise == GROUP) {
                this.currentGroup.sortByPrice(true);
                refreshGroup();
            } else {
                this.store.sortByNumberOfProducts(true);
                refreshStore();
            }
        });
        decrSortButton.addActionListener(press -> {
            if (choise == GROUP) {
                this.currentGroup.sortByPrice(false);
                refreshGroup();
            } else {
                this.store.sortByNumberOfProducts(false);
                refreshStore();
            }
        });
        addButton.addActionListener(press -> {

        });
        findButton.addActionListener(press -> {
            new SearchWindow(choise == GROUP, this.currentGroup, this.store);
        });
        removeButton.addActionListener(press -> {

        });
        editButton.addActionListener(press -> {

        });
        this.taskPanel.add(azSortButton);
        this.taskPanel.add(zaSortButton);
        if (choise == GROUP) {
            this.taskPanel.add(incrSortButton);
            this.taskPanel.add(decrSortButton);
        }
        this.taskPanel.add(findButton);
        this.taskPanel.add(addButton);
        this.taskPanel.add(removeButton);
        this.taskPanel.add(editButton);
        this.add(taskPanel, "East");
    }

    private void refreshGroup() {
        if (this.scroll != null) this.remove(scroll);
        if (this.taskPanel != null) this.remove(this.taskPanel);
        createGroupTable(this.currentGroup);
        createTaskPanel(GROUP);
        this.revalidate();
        this.repaint();
    }

    private void refreshStore() {
        if (this.scroll != null) this.remove(scroll);
        if (this.taskPanel != null) this.remove(this.taskPanel);
        createStoreTable();
        createTaskPanel(STORE);
        this.revalidate();
        this.repaint();
    }

    private void createGroupTable(Group group) {
        this.currentGroup = group;
        String column[] = {"Name", "Price", "Count", "Maker", "Description"};
        String info[][] = new String[group.getNumberOfProducts()][5];
        for (int i = 0; i < group.getNumberOfProducts(); i++) {
            Product product = group.getProduct(i);
            info[i] = new String[] {product.getName(), ""+product.getPrice(),
                                    ""+product.getCount()+" "+product.getEnding(),
                                    product.getMaker(), product.getDescription()};
        }
        this.table = new JTable(info, column);
        DefaultTableModel tableModel = new DefaultTableModel(info, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
    }

    private void createStoreTable() {
        this.currentGroup = null;
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
        DefaultTableModel tableModel = new DefaultTableModel(info, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
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