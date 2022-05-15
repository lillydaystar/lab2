package Graphical;

import DataTypes.Product;

import javax.swing.*;
import java.util.List;

public class SearchResults extends JFrame {

    private final static int RESULT_WIDTH = 600;
    private final static int RESULT_HEIGHT = 300;

    SearchResults(List<Product> list) {
        super("Results");
        this.setSize(RESULT_WIDTH, RESULT_HEIGHT);
        this.setLocation(Tools.getCenterLocation(RESULT_WIDTH, RESULT_HEIGHT));
        for (Product product : list)
            System.out.println(product.toString());
        this.setVisible(true);
    }
}
