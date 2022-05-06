package Graphical;

import DataTypes.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ProductActions extends JFrame {

    JPanel panel;

    ProductActions(){
        setTitle("Товар");
        init();
    }

    void init(){
        setBounds(800,300,400,300);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(panel);
        setVisible(true);

        JComboBox<Group> viewGroups = new JComboBox<Group>();
        for (Group group : MainWindow.groups) viewGroups.addItem(group);
        panel.add(viewGroups);
        JButton add = new JButton("Додати");
        JButton edit = new JButton("Редагувати");
        JButton delete = new JButton("Видалити");

        panel.add(add);
        panel.add(edit);
        panel.add(delete);
        add.addActionListener(press -> {
            remove(panel);
            addProduct((Group) viewGroups.getSelectedItem());
        });

        edit.addActionListener(press -> {
            try {
                remove(panel);
                editProduct((Group) viewGroups.getSelectedItem());
            } catch (EmptyProductsException e) {
                e.printStackTrace();
            }
        });

        delete.addActionListener(press -> {
            try {
                remove(panel);
                deleteProduct((Group) viewGroups.getSelectedItem());
            } catch (EmptyProductsException e) {
                e.printStackTrace();
            }
        });
    }

    private void addProduct(Group gr){

    }

    private void editProduct(Group gr) throws EmptyProductsException {
        if(gr.getNumberOfProducts() == 0) throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б одну.");
        Product pr;

    }

    private void deleteProduct(Group gr) throws EmptyProductsException{
        if(gr.getNumberOfProducts() == 0) throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б одну.");

        this.revalidate();
        this.repaint();

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(panel);
        JComboBox<Product> viewProducts = new JComboBox<Product>();
        for (Product product : gr.getProducts()) viewProducts.addItem(product);

        JButton b = new JButton("Submit");
        b.addActionListener(press -> {
            Product pr = (Product) viewProducts.getSelectedItem();
            gr.deleteProduct(pr);
            try {
                deleteFromFile(gr,pr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.dispose();
        });
        panel.add(b);
        panel.add(viewProducts);
        b.setVisible(true);
        viewProducts.setVisible(true);
        this.pack();
    }

    private void deleteFromFile(Group gr, Product pr) throws IOException {
        File inputFile = gr.getFile();
        File tempFile = new File("Temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = pr.toString();
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + "\n");
        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile);         //не працює
        boolean del2 = tempFile.delete();
    }

}

class EmptyProductsException extends Exception{

    EmptyProductsException(){}

    EmptyProductsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодного товару","Error",  JOptionPane.ERROR_MESSAGE);
    }
}
