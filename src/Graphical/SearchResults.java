package Graphical;

import DataTypes.Group;
import DataTypes.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SearchResults extends JFrame {

    private final static int RESULT_WIDTH = 600;
    private final static int RESULT_HEIGHT = 300;

    private JTable table;
    private JPanel topPanel;
    private JScrollPane scroll;

    SearchResults(List<Product> list) {
        super("Results");
        this.setSize(RESULT_WIDTH, RESULT_HEIGHT);
        this.setLocation(Tools.getCenterLocation(RESULT_WIDTH, RESULT_HEIGHT));
        createTopPanel(list);
        this.setVisible(true);
    }

    void createTopPanel(List<Product> list) {
        if (this.scroll != null) this.remove(scroll);
        if (this.topPanel != null) this.remove(topPanel);
        if (this.table != null) this.table = null;

        this.topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton outputButton = new JButton("Output");

        outputButton.addActionListener(press -> {
            /* action listener for Output button */
        });
        topPanel.add(outputButton);
        createTable(list);
        this.add(this.topPanel, "North");
        this.revalidate();
        this.repaint();
    }

    private void createTable(List<Product> list) {
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

    private double totalPrice(List<Product> list) {
        double price = 0;
        for (Product product : list)
            price += product.getPrice();
        return price;
    }

    public static void main(String[] args) {
        try {
            Group group = new Group(new File("/home/kirill/MyGroup.txt"));
            LinkedList<Product> list = new LinkedList<>();
            for (Product product : group)
                list.add(product);
            new SearchResults(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}