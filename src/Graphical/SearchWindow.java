package Graphical;

import DataTypes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

class SearchWindow extends JFrame {

    private final static int SEARCH_WIDTH = 300;
    private final static int SEARCH_HEIGHT = 300;

    private final Store store;
    private final Group group;

    private boolean searchInDescription;
    private boolean searchForLiquid;
    private boolean searchForWeight;
    private boolean searchForPiece;

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
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JPanel check = new JPanel(new GridLayout(4, 1));
            panel.setPreferredSize(new Dimension(SEARCH_WIDTH, SEARCH_WIDTH *2/3));
//            Vector<String> list = new Vector<>(this.store.numberOfGroups() + 1);
//            for (int i = 0; i < this.store.numberOfGroups(); i++)
//                list.add(this.store.get(i).getName());
//            list.add("<all>");

            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField countField = new JTextField();
            JTextField makerField = new JTextField();
//            JComboBox<String> groupCombo = new JComboBox<>(list);
            JCheckBox checkBox = new JCheckBox("Search \"name\" in description");
            JCheckBox pieceCheck = new JCheckBox("Search for piece products");
            JCheckBox weightCheck = new JCheckBox("Search for weight products");
            JCheckBox liquidCheck = new JCheckBox("Search for liquid products");

            checkBox.addItemListener(checked ->
                    setSearchInDescription(checked.getStateChange() == ItemEvent.SELECTED));
            pieceCheck.addItemListener(checked ->
                    setSearchForPiece(checked.getStateChange() == ItemEvent.SELECTED));
            liquidCheck.addItemListener(checked ->
                    setSearchForLiquid(checked.getStateChange() == ItemEvent.SELECTED));
            weightCheck.addItemListener(checked ->
                    setSearchForWeight(checked.getStateChange() == ItemEvent.SELECTED));

            JLabel nameLabel = new JLabel("Name:", JLabel.CENTER);
            JLabel priceLabel = new JLabel("Price:", JLabel.CENTER);
//            JLabel groupLabel = new JLabel("Group:", JLabel.CENTER);
            JLabel countLabel = new JLabel("Count:", JLabel.CENTER);
            JLabel makerLabel = new JLabel("Maker:", JLabel.CENTER);
            JButton searchButton = new JButton("Search");

            searchButton.addActionListener(press -> {
                try {
                    String name = nameField.getText();
                    String priceStr = priceField.getText();
                    String countStr = countField.getText();
                    String maker = makerField.getText();

                    List<Product> found;
                    ProductPattern pattern;
                    if (searchInGroup) {
                        if (this.searchInDescription) {
                            pattern = new ProductPattern(name, name, maker, priceStr, countStr);
                            pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                        }
                        else {
                            pattern = new ProductPattern(name, null, maker, priceStr, countStr);
                            pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                        }
                        found = Searching.search(this.group, pattern);
                    } else {
                        if (searchInDescription) {
                            pattern = new ProductPattern(name, name, maker, priceStr, countStr);
                            pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                        }
                        else {
                            pattern = new ProductPattern(name, null, maker, priceStr, countStr);
                            pattern.addTypeFilter(this.searchForPiece, this.searchForLiquid, this.searchForWeight);
                        }
                        found = Searching.search(this.store, pattern);
                    }

                    this.setVisible(false);
                    new SearchResults(found);
                } catch (DataExceptions e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, e.getMessage(),
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
//            panel.add(groupLabel);
//            panel.add(groupCombo);

            check.add(checkBox);
            check.add(pieceCheck);
            check.add(weightCheck);
            check.add(liquidCheck);

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
}