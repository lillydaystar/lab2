package Graphical;

import DataTypes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

class SearchWindow extends JFrame {

    private final static int SEARCH_WIDTH = 800;
    private final static int SEARCH_HEIGHT = 600;

    private final Store store;
    private final Group group;

    private boolean searchInDescription;
    private boolean searchForLiquid;
    private boolean searchForWeight;
    private boolean searchForPiece;
    private boolean useRegex;

    private JScrollPane scroll;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField countField;
    private JTextField makerField;

    SearchWindow(boolean searchInGroup, Group currentGroup, Store store) {
        super("Search");
        this.store = store;
        this.group = currentGroup;
        this.setSize(SEARCH_WIDTH, SEARCH_HEIGHT);
        this.setLocation(Tools.getCenterLocation(SEARCH_WIDTH, SEARCH_HEIGHT));
        addElements(searchInGroup);
        this.setVisible(true);
    }

    private void addElements(boolean searchInGroup) {
//        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel searchPanel = new JPanel(new GridLayout(4, 2));
        JPanel checkPanel = new JPanel(new GridLayout(5, 1));

        nameField = new JTextField();
        priceField = new JTextField();
        countField = new JTextField();
        makerField = new JTextField();

        JCheckBox checkBox = new JCheckBox("Search \"name\" in description");
        JCheckBox pieceCheck = new JCheckBox("Search for piece products");
        JCheckBox weightCheck = new JCheckBox("Search for weight products");
        JCheckBox liquidCheck = new JCheckBox("Search for liquid products");
        JCheckBox regex = new JCheckBox("Use simple regular expression");

        checkBox.addItemListener(checked -> {
            setSearchInDescription(checked.getStateChange() == ItemEvent.SELECTED);
            createTable(performSearch(searchInGroup));
        });
        pieceCheck.addItemListener(checked -> {
            setSearchForPiece(checked.getStateChange() == ItemEvent.SELECTED);
            createTable(performSearch(searchInGroup));
        });
        liquidCheck.addItemListener(checked -> {
            setSearchForLiquid(checked.getStateChange() == ItemEvent.SELECTED);
            createTable(performSearch(searchInGroup));
        });
        weightCheck.addItemListener(checked -> {
            setSearchForWeight(checked.getStateChange() == ItemEvent.SELECTED);
            createTable(performSearch(searchInGroup));
        });
        regex.addItemListener(checked -> {
            setUseRegex(checked.getStateChange() == ItemEvent.SELECTED);
            createTable(performSearch(searchInGroup));
        });

        Component text[] = {nameField, priceField, countField, makerField};
        for (Component component : text)
            component.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
//                    System.out.println(e.getKeyCode());
                }

                @Override
                public void keyPressed(KeyEvent e) {
//                    System.out.println(e.getKeyCode());
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    createTable(performSearch(searchInGroup));
                }
            });

        JLabel nameLabel = new JLabel("Name:", JLabel.CENTER);
        JLabel priceLabel = new JLabel("Price:", JLabel.CENTER);
        JLabel countLabel = new JLabel("Count:", JLabel.CENTER);
        JLabel makerLabel = new JLabel("Maker:", JLabel.CENTER);

        createTable(new LinkedList<>());
        setFont(new Component[] {nameLabel, priceLabel, countLabel, makerLabel,
                    checkBox, pieceCheck, liquidCheck, weightCheck, regex,
                    nameField, priceField, countField, makerField}, 16);

        searchPanel.add(nameLabel);
        searchPanel.add(nameField);
        searchPanel.add(priceLabel);
        searchPanel.add(priceField);
        searchPanel.add(countLabel);
        searchPanel.add(countField);
        searchPanel.add(makerLabel);
        searchPanel.add(makerField);

        checkPanel.add(checkBox);
        checkPanel.add(pieceCheck);
        checkPanel.add(weightCheck);
        checkPanel.add(liquidCheck);
        checkPanel.add(regex);

        mainPanel.add(searchPanel);
        mainPanel.add(checkPanel);
        this.add(mainPanel, "West");
        this.add(this.scroll, "Center");
    }

    private void createTable(List<Product> list) {
        if (this.scroll != null) this.remove(scroll);
        this.scroll = null;
        JTable table;
        this.revalidate();
        this.repaint();
        String[] column = {"Name", "Price", "Count", "Maker", "Description"};
        String[][] info = new String[list.size() + 1][5];
        for (int i = 0; i < list.size(); i++) {
            Product product = list.get(i);
            info[i] = new String[] {product.getName(), ""+product.getPrice(),
                    ""+product.getDCount()+product.getEnding(),
                    product.getMaker(), product.getDescription()};
        }
        info[info.length - 1] = new String[] {"Всього: "+list.size()+" товарів"
                , totalPrice(list)+"", "", "", ""};
        table = new JTable(info, column);
        DefaultTableModel tableModel = new DefaultTableModel(info, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        this.scroll = new JScrollPane(table);
        this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
        this.revalidate();
        this.repaint();
    }

    private List<Product> performSearch(boolean searchInGroup) {
        try {
            String name = nameField.getText();
            String priceStr = priceField.getText();
            String countStr = countField.getText();
            String maker = makerField.getText();

            List<Product> found;
            ProductPattern pattern;
            if (searchInGroup) {
                if (this.searchInDescription) {
                    pattern = new ProductPattern(name, name, maker, priceStr, countStr, useRegex);
                    pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                }
                else {
                    pattern = new ProductPattern(name, null, maker, priceStr, countStr, useRegex);
                    pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                }
                found = Searching.search(this.group, pattern);
            } else {
                if (searchInDescription) {
                    pattern = new ProductPattern(name, name, maker, priceStr, countStr, useRegex);
                    pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                }
                else {
                    pattern = new ProductPattern(name, null, maker, priceStr, countStr, useRegex);
                    pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                }
                found = Searching.search(this.store, pattern);
            }

            return found;
        } catch (DataExceptions e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            if (e.getMessage().equals("Incorrect format of \"count\" field"))
                this.countField.setText("");
            if (e.getMessage().equals("Incorrect format of \"price\" field"))
                this.priceField.setText("");
        }
        return new LinkedList<>();
    }

    private double totalPrice(List<Product> list) {
        double price = 0;
        for (Product product : list)
            price += product.getPrice()*product.getCount();
        return price;
    }

    private void setFont(Component array[], int size) {
        for (Component component : array)
            component.setFont(new Font(Font.MONOSPACED, Font.PLAIN, size));
    }

    private void setSearchInDescription(boolean searchInDescription) {
        this.searchInDescription = searchInDescription;
    }

    private void setSearchForLiquid(boolean searchForLiquid) {
        this.searchForLiquid = searchForLiquid;
    }

    private void setSearchForWeight(boolean searchForWeight) {
        this.searchForWeight = searchForWeight;
    }

    private void setSearchForPiece(boolean searchForPiece) {
        this.searchForPiece = searchForPiece;
    }

    private void setUseRegex(boolean useRegex) {
        this.useRegex = useRegex;
        System.out.println(this.useRegex);
    }
}