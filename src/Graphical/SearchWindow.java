package Graphical;

import DataTypes.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

class SearchWindow extends JFrame {

    private final static int SEARCH_WIDTH = 300;
    private final static int SEARCH_HEIGHT = 300;

    private final Store store;
    private final Group group;

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
        try {
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JPanel check = new JPanel(new GridLayout(4, 1));
            panel.setPreferredSize(new Dimension(SEARCH_WIDTH, SEARCH_WIDTH *2/3));
            Vector<String> list = new Vector<>(this.store.numberOfGroups() + 1);
            for (int i = 0; i < this.store.numberOfGroups(); i++)
                list.add(this.store.get(i).getName());
            list.add("<all>");

            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField countField = new JTextField();
            JTextField makerField = new JTextField();
            JComboBox<String> groupCombo = new JComboBox<>(list);
            JCheckBox checkBox = new JCheckBox("Search \"name\" in description");
            JCheckBox piece = new JCheckBox("Search for piece products");
            JCheckBox weight = new JCheckBox("Search for weight products");
            JCheckBox liquid = new JCheckBox("Search for liquid products");

            JLabel nameLabel = new JLabel("Name:", JLabel.CENTER);
            JLabel priceLabel = new JLabel("Price:", JLabel.CENTER);
            JLabel groupLabel = new JLabel("Group:", JLabel.CENTER);
            JLabel countLabel = new JLabel("Count:", JLabel.CENTER);
            JLabel makerLabel = new JLabel("Maker:", JLabel.CENTER);
            JButton searchButton = new JButton("Search");

            searchButton.addActionListener(press -> {
                try {
                    String name = nameField.getText();
                    String priceStr = priceField.getText();
                    String countStr = countField.getText();
                    String maker = makerField.getText();
                    double price, count;
                    boolean searchInDescription = checkBox.isBorderPaintedFlat();

                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "Incorrect number format\nof field \"price\"", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        count = Double.parseDouble(countStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                                "Incorrect number format\nof field \"count\"", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    List<Product> found;
                    ProductPattern pattern;
                    if (searchInGroup) {
                        if (searchInDescription) {
                            pattern = new ProductPattern(name, name, maker, price, count);
                            pattern.addTypeFilter(piece.isEnabled(), liquid.isEnabled(), weight.isEnabled());
                        }
                        else {
                            pattern = new ProductPattern(name, null, maker, price, count);
                            pattern.addTypeFilter(piece.isEnabled(), liquid.isEnabled(), weight.isEnabled());
                        }
                        found = Searching.search(this.group, pattern);
                    } else {
                        if (searchInDescription) {
                            pattern = new ProductPattern(name, name, maker, price, count);
                            pattern.addTypeFilter(piece.isEnabled(), liquid.isEnabled(), weight.isEnabled());
                        }
                        else {
                            pattern = new ProductPattern(name, null, maker, price, count);
                            pattern.addTypeFilter(piece.isEnabled(), liquid.isEnabled(), weight.isEnabled());
                        }
                        found = Searching.search(this.store, pattern);
                    }

                    this.setVisible(false);
                    new SearchResults(found);
                } catch (DataExceptions e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Incorrect format of numeric values!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(priceLabel);
            panel.add(priceField);
            panel.add(countLabel);
            panel.add(countField);
            panel.add(makerLabel);
            panel.add(makerField);
            panel.add(groupLabel);
            panel.add(groupCombo);

            check.add(checkBox);
            check.add(piece);
            check.add(weight);
            check.add(liquid);

            this.add(panel, "North");
            this.add(check, "Center");
            this.add(searchButton, "South");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error while loading groups",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}