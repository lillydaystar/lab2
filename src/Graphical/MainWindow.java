package Graphical;

import DataTypes.Group;
import DataTypes.Product;
import DataTypes.Store;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

public class MainWindow extends JFrame {

    private final static int STORE = 1;
    private final static int GROUP = 2;
    private final static int PRODUCTS = 3;

    private final static int WIDTH = 1000;
    private final static int HEIGHT = 800;

    private JTable table;
    private JPanel topPanel;
    private JPanel taskPanel;
    private JScrollPane scroll;
    private Vector<String> list;
    private List<Product> tableContext;
    private Group currentGroup;
    public static final Store store = new Store();

    public MainWindow(){
        super("Product manager");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(Tools.getCenterLocation(WIDTH, HEIGHT));
        createTopPanel();
        this.setVisible(true);
    }

    void createTopPanel() {
        try {
            if (this.scroll != null) this.remove(scroll);
            if (this.topPanel != null) this.remove(topPanel);
            if (this.table != null) this.table = null;
            this.list = new Vector<>(store.numberOfGroups() + 3);
            list.add("<none>");
            list.add("Store view");
            list.add("All products");
            for (int i = 0; i < store.numberOfGroups(); i++)
                list.add(store.get(i).getName());

            this.topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton helpButton = new JButton("Help");
            helpButton.addActionListener(press -> {
                /*help*/
                openInstruction();
            });
            JComboBox<String> viewCombo = new JComboBox<>(list);
            viewCombo.addActionListener(select -> {
                try {
                    int selectedIndex = viewCombo.getSelectedIndex();
                    switch (list.get(selectedIndex)) {
                        case "Store view":
                            if (this.scroll != null) this.remove(scroll);
                            if (this.taskPanel != null) this.remove(this.taskPanel);
                            createStoreTable();
                            createTaskPanel(STORE);
                            this.revalidate();
                            this.repaint();
                            break;
                        case "All products":
                            if (this.scroll != null) this.remove(scroll);
                            if (this.taskPanel != null) this.remove(this.taskPanel);
                            this.currentGroup = null;
                            createProductsTable();
                            createTaskPanel(PRODUCTS);
                            this.revalidate();
                            this.repaint();
                            break;
                        case "<none>":
                            if (this.table != null) {
                                if (this.scroll != null) this.remove(scroll);
                                if (this.taskPanel != null) this.remove(this.taskPanel);
                                this.currentGroup = null;
                                this.tableContext = null;
                                this.revalidate();
                                this.repaint();
                            }
                            break;
                        default:
                            Group selectedGroup = store.get(selectedIndex - 3);
                            if (this.scroll != null) this.remove(scroll);
                            if (this.taskPanel != null) this.remove(this.taskPanel);
                            createGroupTable(selectedGroup);
                            createTaskPanel(GROUP);
                            this.revalidate();
                            this.repaint();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            topPanel.add(viewCombo);
            topPanel.add(helpButton);
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
        if (choice != GROUP && choice != STORE && choice != PRODUCTS)
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
        JButton findButton = new JButton(choice == GROUP ? "Search" : "Find");
        JButton removeButton = new JButton(choice == GROUP ? "Remove" : "Delete");
        JButton editButton = new JButton(choice == GROUP ? "Edit" : "Change");
        JButton fillButton = new JButton("Bring/Sell");
        azSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByName(true);
                refreshGroup();
            } else if(choice == STORE){
                store.sortByName(true);
                refreshStore();
            }else{
                Store.sortByName(this.tableContext, true);
                try {
                    refreshProductsStore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        zaSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByName(false);
                refreshGroup();
            } else if(choice == STORE){
                store.sortByName(false);
                refreshStore();
            }else{
                Store.sortByName(this.tableContext, false);
                try {
                    refreshProductsStore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        incrSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByPrice(true);
                refreshGroup();
            } else if(choice == STORE){
                store.sortByNumberOfProducts(true);
                refreshStore();
            }else{
                Store.sortByPrice(this.tableContext, true);
                try {
                    refreshProductsStore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        decrSortButton.addActionListener(press -> {
            if (choice == GROUP) {
                this.currentGroup.sortByPrice(false);
                refreshGroup();
            } else if(choice == STORE){
                store.sortByNumberOfProducts(false);
                refreshStore();
            }else{
                Store.sortByPrice(this.tableContext, false);
                try {
                    refreshProductsStore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        findButton.addActionListener(press ->
                new SearchWindow(choice == GROUP, this.currentGroup, store));
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
        this.taskPanel.add(azSortButton);
        this.taskPanel.add(zaSortButton);
        if (choice == GROUP || choice == PRODUCTS) {
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
        this.add(taskPanel, "East");
    }


    void refreshGroup() {
        if (this.scroll != null) this.remove(scroll);
        if (this.taskPanel != null) this.remove(this.taskPanel);
        createGroupTable(this.currentGroup);
        createTaskPanel(GROUP);
        this.revalidate();
        this.repaint();
    }

    void refreshStore() {
        if (this.scroll != null) this.remove(scroll);
        if (this.taskPanel != null) this.remove(this.taskPanel);
        createStoreTable();
        createTaskPanel(STORE);
        this.revalidate();
        this.repaint();
    }

    private void refreshProductsStore() throws Exception {
        this.remove(scroll);
        this.currentGroup = null;
        String[] column = {"Назва", "Ціна (грн)", "Кількість", "Виробник", "Опис","Група"};
        String[][] info = new String[this.tableContext.size() + 1][5];
        for (int i = 0; i < this.tableContext.size(); i++) {
            Product product = this.tableContext.get(i);
            info[i] = new String[]{product.getName(), "" + product.getPrice(),
                    "" + product.getDCount() + product.getEnding(),
                    product.getMaker(), product.getDescription(), product.getGroup().getName()};
        }
        int totalPrice =0;
        for(int i=0; i<store.numberOfGroups(); i++){
            totalPrice += store.get(i).totalPrice();
        }
        info[info.length - 1] = new String[] {"Всього: "+this.tableContext.size()+" товарів"
                , totalPrice+"", "", "", ""};
        this.table = defaultTable(info,column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
        this.revalidate();
        this.repaint();
    }

    private void createGroupTable(Group group) {
        this.currentGroup = group;
        String[] column = {"Назва", "Ціна (грн)", "Кількість", "Виробник", "Опис"};
        String[][] info = new String[group.getNumberOfProducts() + 1][5];
        for (int i = 0; i < group.getNumberOfProducts(); i++) {
            Product product = group.getProduct(i);
            info[i] = new String[] {product.getName(), ""+product.getPrice(),
                                    ""+product.getDCount()+product.getEnding(),
                                    product.getMaker(), product.getDescription()};
        }
        info[info.length - 1] = new String[] {"Всього: "+group.getNumberOfProducts()+" товарів"
                , group.totalPrice()+"", "", "", ""};
        this.table = defaultTable(info,column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
    }

    private void createStoreTable() {
        this.currentGroup = null;
        String[] column = {"Назва", "Кількість продуктів", "Опис", "Загальна вартість"};
        String[][] info = new String[store.numberOfGroups()+1][4];
        for (int i = 0; i < store.numberOfGroups(); i++) {
            try {
                info[i] = new String[] {store.get(i).getName(),
                        "" + store.get(i).getNumberOfProducts(),
                        store.get(i).getDescription(), store.get(i).totalPrice()+""};

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        info[info.length - 1] = new String[] {"Всього: "+store.numberOfGroups()+" груп",
                store.totalProducts()+" продуктів", "",
                store.totalPrice()+" грн"};
        this.table = defaultTable(info,column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll);
    }

    private void createProductsTable() throws Exception {
        this.tableContext = null;
        this.currentGroup = null;
        this.tableContext = store.getProducts();
        String[] column = {"Назва", "Ціна (грн)", "Кількість", "Виробник", "Опис","Група"};

        String[][] info = new String[store.numberOfAllProducts() + 1][5];
        for (int i = 0; i < this.tableContext.size(); i++) {
            Product product = this.tableContext.get(i);
            info[i] = new String[]{product.getName(), "" + product.getPrice(),
                    "" + product.getDCount() + product.getEnding(),
                    product.getMaker(), product.getDescription(), product.getGroup().getName()};
        }
        int totalPrice =0;
        for(int i=0; i<store.numberOfGroups(); i++){
            totalPrice += store.get(i).totalPrice();
        }
        info[info.length - 1] = new String[] {"Всього: "+this.tableContext.size()+" товарів"
                , totalPrice+"", "", "", ""};
        this.table = defaultTable(info,column);
        this.scroll = new JScrollPane(this.table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scroll);
    }

    private JTable defaultTable(String[][] info, String[] column){
        JTable table = new JTable(info, column);
        DefaultTableModel tableModel = new DefaultTableModel(info, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column)
            {
                final Component c = super.
                        getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row == info.length-1 ? new Color(134,231,82) : Color.WHITE);
                return c;
            }
        });

        return table;
    }

    private void productAction(Action choice) throws EmptyGroupsException{
        if(store.isEmpty()) throw new
                EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
        new ProductActions(this, this.currentGroup, choice);
    }

    private void groupAction(Action choice){
        new GroupActions(this, choice);
    }

    private void openInstruction() {
        JFrame helpWindow = new JFrame();
        JPanel helpPanel = new JPanel(new GridLayout(1,1));
        helpWindow.setSize(500,500);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        helpWindow.setLocation((dimension.width - 500)/2, (dimension.height - 500)/2);
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setFont(new Font("Arial", Font.BOLD,20));
        JScrollPane scroll = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        helpPanel.add(scroll);
        helpWindow.add(helpPanel);
        helpWindow.setVisible(true);

        File file = new File("src//resources//Help.txt");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            text.read(br, null);
            br.close();
            text.requestFocus();
        } catch (Exception f) {
            JOptionPane.showMessageDialog(null, f);
        }
    }
}

enum Action{
    ADD, DELETE, EDIT, FILL
}