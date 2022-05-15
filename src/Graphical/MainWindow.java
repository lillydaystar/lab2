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
    public static final Store store = new Store();

    public MainWindow(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(Tools.getCenterLocation(WIDTH, HEIGHT));
        //store = new Store();
        createTopPanel();
        this.setVisible(true);
    }

    void createTopPanel() {
        try {
            if (this.scroll != null) this.remove(scroll);
            if (this.topPanel != null) this.remove(topPanel);
            if (this.table != null) this.table = null;
            this.list = new Vector<>(store.numberOfGroups() + 2);
            list.add("<none>");
            list.add("Store view");
            for (int i = 0; i < store.numberOfGroups(); i++)
                list.add(store.get(i).getName());

            this.topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton storeButton = new JButton("Store price");
            JButton groupButton = new JButton("Group price");
            storeButton.addActionListener(press -> {
                try {
                    showStorePrice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            groupButton.addActionListener(press -> {
                if(currentGroup == null){
                    JOptionPane.showMessageDialog(null,"Для підрахунку ціни всіх товарів у групі, виберіть групу у списку", "ERROR", JOptionPane.ERROR_MESSAGE);
                }else
                    showGroupPrice();

            });
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
                        Group selectedGroup = store.get(selectedIndex - 2);
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
            topPanel.add(storeButton);
            topPanel.add(viewCombo);
            topPanel.add(groupButton);
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

    private void createTaskPanel(int choice) {
        if (choice != GROUP && choice != STORE)
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
        JButton addButton = new JButton(choice == GROUP ? "Add" : "New");
        JButton findButton = new JButton(choice == GROUP ? "Select" : "Find");
        JButton removeButton = new JButton(choice == GROUP ? "Remove" : "Delete");
        JButton editButton = new JButton(choice == GROUP ? "Edit" : "Change");
        JButton resetTable = new JButton("Reset");
        JButton fillButton = new JButton("Bring/Sell");
        azSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByName(true);
                refreshGroup();
            } else {
                store.sortByName(true);
                refreshStore();
            }
        });
        zaSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByName(false);
                refreshGroup();
            } else {
                store.sortByName(false);
                refreshStore();
            }
        });
        incrSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByPrice(true);
                refreshGroup();
            } else {
                store.sortByNumberOfProducts(true);
                refreshStore();
            }
        });
        decrSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByPrice(false);
                refreshGroup();
            } else {
                store.sortByNumberOfProducts(false);
                refreshStore();
            }
        });
        addButton.addActionListener(press -> {
            if (choice == GROUP) {
                try {
                    productAction(Action.ADD);
                } catch (EmptyGroupsException e) {
                    e.printStackTrace();
                }
            }
            else{
                groupAction(Action.ADD);
            }
        });
        findButton.addActionListener(press -> new SearchWindow(choice == GROUP, this.currentGroup, store));
        removeButton.addActionListener(press -> {
            if (choice == GROUP) {
                try {
                    productAction(Action.DELETE);
                } catch (EmptyGroupsException e) {
                    e.printStackTrace();
                }
            }
            else{
                groupAction(Action.DELETE);
            }
        });
        editButton.addActionListener(press -> {
            if (choice == GROUP) {
                try {
                    productAction(Action.EDIT);
                } catch (EmptyGroupsException e) {
                    e.printStackTrace();
                }
            }
            else{
                groupAction(Action.EDIT);
            }
        });
        fillButton.addActionListener(press -> {
            try {
                productAction(Action.FILL);
            } catch (EmptyGroupsException e) {
                e.printStackTrace();
            }
        });
        resetTable.addActionListener(press -> {        //тимчасове!
            if(choice == STORE) {
                refreshStore();
            }else refreshGroup();
        });
        this.taskPanel.add(azSortButton);
        this.taskPanel.add(zaSortButton);
        if (choice == GROUP) {
            this.taskPanel.add(incrSortButton);
            this.taskPanel.add(decrSortButton);
        }
        this.taskPanel.add(findButton);
        this.taskPanel.add(addButton);
        this.taskPanel.add(removeButton);
        this.taskPanel.add(editButton);
        if(choice == GROUP){
            this.taskPanel.add(fillButton);
        }
        this.taskPanel.add(resetTable);
        this.add(taskPanel, "East");
    }

    private void showStorePrice() throws Exception {
        int price = 0;
        if(!store.isEmpty()){
            for(int i=0; i<store.numberOfGroups(); i++){
                for(int j=0; j<store.get(i).getNumberOfProducts(); j++){
                    double p = store.get(i).getProducts().get(j).getPrice();
                    double c = store.get(i).getProducts().get(j).getDCount();
                    price+= p*c;
                }
            }
        }
        JOptionPane.showMessageDialog(null,"Загальна ціна товарів на складі - " + price + " грн", "Store price", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showGroupPrice(){
        int price = 0;
        if(currentGroup.getNumberOfProducts() != 0){
            for(int j=0; j<this.currentGroup.getNumberOfProducts(); j++){
                double p = currentGroup.getProducts().get(j).getPrice();
                double c = currentGroup.getProducts().get(j).getDCount();
                price+= p*c;
            }
        }
        JOptionPane.showMessageDialog(null,"Загальна ціна товарів у групі '"+currentGroup.toString()+"' - " + price + " грн", "Group price", JOptionPane.INFORMATION_MESSAGE);

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
        String[] column = {"Name", "Price", "Count", "Maker", "Description"};
        String[][] info = new String[group.getNumberOfProducts()][5];
        for (int i = 0; i < group.getNumberOfProducts(); i++) {
            Product product = group.getProduct(i);
            info[i] = new String[] {product.getName(), ""+product.getPrice(),
                                    ""+product.getDCount()+product.getEnding(),
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
        String[] column = {"Name", "Number of products", "Description"};
        String[][] info = new String[store.numberOfGroups()][3];
        for (int i = 0; i < store.numberOfGroups(); i++) {
            try {
                info[i] = new String[] {store.get(i).getName(),
                        "" + store.get(i).getNumberOfProducts(),
                        store.get(i).getDescription()};
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

    private void productAction(Action choice) throws EmptyGroupsException{
        if(store.isEmpty()) throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
        new ProductActions(this, this.currentGroup, choice);
    }

    private void groupAction(Action choice){
        new GroupActions(this, choice);
    }
}

enum Action{
    ADD, DELETE, EDIT, FILL
}