package Graphical;

import DataTypes.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductActions extends JFrame {

    JPanel panel;

    ProductActions(MainWindow window){
        setTitle("Товар");
        init(window);
    }

    void init(MainWindow window) {
        setBounds(800,300,400,300);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(panel);
        setVisible(true);

        JComboBox<Group> viewGroups = new JComboBox<>();
        for (Group group : window.store) viewGroups.addItem(group);
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

    private void resetPanel() {
        this.revalidate();
        this.repaint();
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(panel);
    }

    private void addProduct(Group gr){
        resetPanel();
        JLabel nameL = new JLabel("Назва: ");
        JTextArea name = new JTextArea(1, 10);
        panel.add(nameL);
        panel.add(name);
        JLabel priceL = new JLabel("Ціна: ");
        JTextArea price = new JTextArea(1, 10);
        panel.add(priceL);
        panel.add(price);
        JLabel decryL = new JLabel("Опис: ");
        JTextArea description = new JTextArea(1, 10);
        panel.add(decryL);
        panel.add(description);
        JLabel makerL = new JLabel("Виробник: ");
        JTextArea maker = new JTextArea(1, 10);
        panel.add(makerL);
        panel.add(maker);
        JComboBox<String> type = new JComboBox<>();
        type.addItem("Цілісний(шт)");
        type.addItem("Сипучий(кг)");
        type.addItem("Рідкий(л)");
        panel.add(type);
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            String n,d,m;
            double p;
            n = name.getText();
            p = Double.parseDouble(price.getText());       //add exception
            d = description.getText();
            m = maker.getText();
            try {
                Product newPr = null;
                String selected = (String)type.getSelectedItem();
                if (selected == null)
                    throw new NullPointerException("Selected item is null!");
                switch (selected) {
                    case "Цілісний(шт)":
                        newPr = new PieceProduct(n, d, m, p, 0);
                        break;
                    case "Сипучий(кг)":
                        newPr = new WeightProduct(n, d, m, p, 0);
                        break;
                    case "Рідкий(л)":
                        newPr = new LiquidProduct(n, d, m, p, 0);
                        break;
                }
                addToFile(gr,newPr);
                gr.add(newPr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();
        });
        panel.add(submit);
        pack();
    }

    private void addToFile(Group gr, Product pr) throws IOException{
        changeGroupSize(gr);
        try{
            FileWriter fStream = new FileWriter(gr.getFile(),true);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write(pr.toString());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void editProduct(Group gr) throws EmptyProductsException {
        if(gr.getNumberOfProducts() == 0) throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б одну.");
        resetPanel();
        JComboBox<Product> viewProducts = new JComboBox<>();
        for (Product product : gr.getProducts()) viewProducts.addItem(product);
        JCheckBox name = new JCheckBox("Назва");
        JCheckBox price = new JCheckBox("Ціна");
        JCheckBox description = new JCheckBox("Опис");
        JCheckBox maker = new JCheckBox("Виробник");

        JButton b = new JButton("Submit");
        b.addActionListener(press -> {
            Product pr = (Product) viewProducts.getSelectedItem();
            remove(panel);
            if (pr != null) {
                editProductParams(gr, pr, name.isSelected(), price.isSelected(), description.isSelected(), maker.isSelected());
            } else {
                throw new NullPointerException("Product argument in editProduct(Group gr) function is null!");
            }
        });
        panel.add(viewProducts);
        panel.add(name);
        panel.add(price);
        panel.add(description);
        panel.add(maker);
        panel.add(b);
        pack();
    }

    private void editProductParams(Group gr, Product pr, boolean nameSelected,
                                   boolean priceSelected, boolean descrySelected,
                                   boolean makerSelected) {
        resetPanel();
        JLabel product = new JLabel(pr.toString());
        panel.add(product);
        JTextArea name = null;
        JTextArea price = null;
        JTextArea description = null;
        JTextArea maker = null;
        if(nameSelected){
            JLabel nameL = new JLabel("Редагуйте назву: ");
            name = new JTextArea(1, 10);
            panel.add(nameL);
            panel.add(name);
        }
        if(priceSelected){
            JLabel priceL = new JLabel("Редагуйте ціну: ");
            price = new JTextArea(1, 10);
            panel.add(priceL);
            panel.add(price);
        }
        if(descrySelected){
            JLabel decryL = new JLabel("Редагуйте опис: ");
            description = new JTextArea(1, 10);
            panel.add(decryL);
            panel.add(description);
        }
        if(makerSelected){
            JLabel makerL = new JLabel("Редагуйте виробника: ");
            maker = new JTextArea(1, 10);
            panel.add(makerL);
            panel.add(maker);
        }
        JButton submit = new JButton("Submit");
        JTextArea finalName = name;
        JTextArea finalPrice = price;
        JTextArea finalDescription = description;
        JTextArea finalMaker = maker;
        submit.addActionListener(press -> {
            String n,d,m;
            double p;
            if(finalName == null) n = pr.getName();
            else {
                n = finalName.getText();
            }
            if(finalPrice == null) p = pr.getPrice();
            else {
                p = Double.parseDouble(finalPrice.getText());       //add exception
            }
            if(finalDescription == null) d = pr.getDescription();
            else {
                d = finalDescription.getText();
            }
            if(finalMaker == null) m = pr.getMaker();
            else {
                m = finalMaker.getText();
            }

            try {
                editFile(gr,pr,n,p,d,m);
                pr.setName(n);
                pr.setPrice(p);
                pr.setDescription(d);
                pr.setMaker(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();
        });
        panel.add(submit);
        pack();
    }

    private void editFile(Group gr, Product pr, String n, double p, String d, String m) throws IOException {
        File tempFile = new File("Temp.txt");
        File inputFile = gr.getFile();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String lineToEdit = pr.toString();
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToEdit)){
                String newPrice = String.valueOf(p);
                if(newPrice.endsWith(".0")) newPrice = newPrice.substring(0,newPrice.length()-2);
                currentLine = n + " / " + d + " / " + m + " / " + newPrice + " грн / " + pr.getCount() + pr.getEnding();
            }
            writer.write(currentLine + "\n");
        }
        reader.close();
        writer.close();
        rewriteFiles(tempFile,inputFile);
    }

    private void deleteProduct(Group gr) throws EmptyProductsException{
        if(gr.getNumberOfProducts() == 0)
            throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б одну.");
        resetPanel();
        JComboBox<Product> viewProducts = new JComboBox<>();
        for (Product product : gr.getProducts()) viewProducts.addItem(product);

        JButton b = new JButton("Submit");
        b.addActionListener(press -> {
            Product pr = (Product) viewProducts.getSelectedItem();
            if (pr == null)
                throw new NullPointerException("Parameter pr is null in function deleteProduct(Group gr)!");
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
        changeGroupSize(gr);
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
        reader.close();
        writer.close();
        rewriteFiles(tempFile,inputFile);
    }

    private void rewriteFiles(File tempFile, File fileToEdit) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToEdit));
        String s;
        while((s = reader.readLine()) != null) {
            writer.write(s + "\n");
        }
        reader.close();
        writer.close();
        boolean deletedSuccessfully = tempFile.delete();
        if (!deletedSuccessfully)
            throw new IOException("Temp file was not deleted!");
    }

    private void changeGroupSize(Group gr) throws IOException {
        File inputFile = gr.getFile();
        File tempFile = new File("Temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String groupLine = reader.readLine();
        Pattern headerPattern = Pattern.compile("Group\\s+'([A-Za-z ]*)'\\s+size\\s+(\\d+)\\s+description\\s+'([A-Za-z\\s]*)'");
        Matcher matcher = headerPattern.matcher(groupLine);
        if(matcher.matches())
            groupLine = "Group '" + matcher.group(1) + "' size " + gr.getNumberOfProducts() + " description '" + matcher.group(3) + "'";
        writer.write(groupLine + "\n");
        writer.close();
        reader.close();
        rewriteFiles(tempFile,inputFile);
    }
}