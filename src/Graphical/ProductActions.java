package Graphical;

import DataTypes.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductActions extends JFrame {

    JPanel panel;
    MainWindow window;

    ProductActions(MainWindow window, Group gr, Action action){
        setTitle("Товар");
        this.window = window;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        init();
        switch (action) {
            case ADD:
                addProduct(gr);
                break;
            case DELETE:
                try {
                    if(gr.getNumberOfProducts() == 0)
                        throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б один.");
                    deleteProduct(gr);
                } catch (EmptyProductsException e) {
                    dispose();
                    e.printStackTrace();
                }
                break;
            case EDIT:
                try {
                    if(gr.getNumberOfProducts() == 0)
                        throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б один.");
                    editProduct(gr);
                } catch (EmptyProductsException e) {
                    dispose();
                    e.printStackTrace();
                }
                break;
            case FILL:
                try{
                    if(gr.getNumberOfProducts() == 0)
                        throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б один.");
                    chooseProduct(gr);
                }catch (EmptyProductsException e) {
                    dispose();
                    e.printStackTrace();
                }

        }
    }

    void init() {
        setSize(600,300);
        toCentre();
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(panel);
        this.setVisible(true);
    }

    public void resetPanel() {
        remove(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(panel);
    }

    public void toCentre(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x,y);
    }

    private void addProduct(Group gr){
        resetPanel();
        JLabel nameL = new JLabel("Назва: ");
        JTextArea name = new JTextArea(1, 8);
        JLabel priceL = new JLabel("Ціна: ");
        JTextArea price = new JTextArea(1, 5);
        JLabel descriptionL = new JLabel("Опис: ");
        JTextArea description = new JTextArea(1, 10);
        JLabel makerL = new JLabel("Виробник: ");
        JTextArea maker = new JTextArea(1, 8);
        JComboBox<String> type = new JComboBox<>();
        type.addItem("Цілісний(шт)");
        type.addItem("Сипучий(кг)");
        type.addItem("Рідкий(л)");
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            try {
                if (name.getText().isEmpty() || price.getText().isEmpty() || description.getText().isEmpty() || maker.getText().isEmpty())
                    throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                if(MainWindow.store.ProductExist(name.getText()))
                    throw new ProductExistException("Такий товар уже існує!");
                String n, d, m;
                double p;
                n = name.getText();
                if(!price.getText().matches("\\d+(.\\d+)?"))
                    throw new IllegalInputFormat("Неправильний формат числа!!!");
                p = Double.parseDouble(price.getText());
                d = description.getText();
                m = maker.getText();
                try {
                    Product newPr = null;
                    String selected = (String) type.getSelectedItem();
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
                    gr.add(newPr);
                    addToFile(gr, newPr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (IllegalInputFormat | ProductExistException e) {
                e.printStackTrace();
            }
            dispose();
        });
        JComponent[] array = {nameL,name,priceL,price,descriptionL,description,makerL,maker,type,submit};
        setObjectsFont(array,panel);
        revalidate();
        repaint();
    }

    private void setObjectsFont(JComponent[] array, JPanel panel) {
        for (JComponent jComponent : array) {
            jComponent.setFont(new Font("Arial", Font.PLAIN, 20));
            panel.add(jComponent);
        }
    }

    private void addToFile(Group gr, Product pr) throws IOException{
        changeGroupSize(gr);
        try {
            FileWriter fStream = new FileWriter(gr.getFile(),true);
            BufferedWriter out = new BufferedWriter(fStream);
            out.write(pr.toString());
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void editProduct(Group gr) throws EmptyProductsException {
        if(gr.getNumberOfProducts() == 0) throw new EmptyProductsException("Жодного товару не існує, будь ласка, створіть або додайте хоча б один.");
        resetPanel();
        JComboBox<Product> viewProducts = new JComboBox<>();
        for (Product product : gr.getProducts()) viewProducts.addItem(product);
        JCheckBox name = new JCheckBox("Назва");
        JCheckBox price = new JCheckBox("Ціна");
        JCheckBox description = new JCheckBox("Опис");
        JCheckBox maker = new JCheckBox("Виробник");

        JButton b = new JButton("Submit");
        JPanel checkPanel = new JPanel(new GridLayout(5,1));
        b.addActionListener(press -> {
            Product pr = (Product) viewProducts.getSelectedItem();
            if (pr != null) {
                editProductParams(gr, pr, name.isSelected(), price.isSelected(), description.isSelected(), maker.isSelected());
            } else {
                throw new NullPointerException("Product argument in editProduct(Group gr) function is null!");
            }
        });
        panel.add(viewProducts);
        JComponent[] array = {/*viewProducts,*/name,price,description,maker,b};
        for (JComponent jComponent : array) {
            jComponent.setFont(new Font("Arial", Font.PLAIN, 20));
            checkPanel.add(jComponent);
        }
        //setObjectsFont(array);
        panel.add(checkPanel);
        revalidate();
        repaint();
    }

    private void editProductParams(Group gr, Product pr, boolean nameSelected,
                                   boolean priceSelected, boolean descrySelected,
                                   boolean makerSelected) {
        resetPanel();
        JLabel product = new JLabel(pr.toString());
        product.setFont(new Font("Arial", Font.PLAIN, 20));
        this.setSize(product.getPreferredSize().width+30, 300);
        panel.add(product);
        JTextArea name = null;
        JTextArea price = null;
        JTextArea description = null;
        JTextArea maker = null;
        JPanel checkPanel = new JPanel(new GridLayout(4,2));
        if(nameSelected){
            JLabel nameL = new JLabel("Редагуйте назву: ");
            name = new JTextArea(1, 8);
            //setObjectsFont(new JComponent[]{nameL, name});
            setObjectsFont(new JComponent[]{nameL,name}, checkPanel);
        }
        if(priceSelected){
            JLabel priceL = new JLabel("Редагуйте ціну: ");
            price = new JTextArea(1, 5);
            //setObjectsFont(new JComponent[]{priceL, price});
            setObjectsFont(new JComponent[]{priceL, price},checkPanel);
        }
        if(descrySelected){
            JLabel descriptionL = new JLabel("Редагуйте опис: ");
            description = new JTextArea(1, 10);
            //setObjectsFont(new JComponent[]{descriptionL, description});
            setObjectsFont(new JComponent[]{descriptionL, description},checkPanel);
        }
        if(makerSelected){
            JLabel makerL = new JLabel("Редагуйте виробника: ");
            maker = new JTextArea(1, 8);
            //setObjectsFont(new JComponent[]{makerL, maker});
            setObjectsFont(new JComponent[]{makerL, maker},checkPanel);
        }
        JButton submit = new JButton("Submit");
        JTextArea finalName = name;
        JTextArea finalPrice = price;
        JTextArea finalDescription = description;
        JTextArea finalMaker = maker;
        submit.addActionListener(press -> {
            try {
                String n, d, m;
                double p;
                if (finalName == null) n = pr.getName();
                else {
                    if(finalName.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    if(MainWindow.store.ProductExist(finalName.getText()))
                        throw new ProductExistException("Такий товар уже існує!");
                    n = finalName.getText();
                }
                if (finalPrice == null) p = pr.getPrice();
                else {
                    if(finalPrice.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    if(!finalPrice.getText().matches("\\d+(.\\d+)?"))
                        throw new IllegalInputFormat("Неправильний формат числа!!!");
                    p = Double.parseDouble(finalPrice.getText());
                }
                if (finalDescription == null) d = pr.getDescription();
                else {
                    if(finalDescription.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    d = finalDescription.getText();
                }
                if (finalMaker == null) m = pr.getMaker();
                else {
                    if(finalMaker.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    m = finalMaker.getText();
                }

                try {
                    String lineToEdit = pr.toString();
                    pr.setName(n);
                    pr.setPrice(p);
                    pr.setDescription(d);
                    pr.setMaker(m);
                    editFile(gr, pr, lineToEdit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IllegalInputFormat | ProductExistException e) {
                e.printStackTrace();
            }
            dispose();
        });
        submit.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(checkPanel);
        panel.add(submit);
        revalidate();
        repaint();
    }

    private void editFile(Group gr, Product pr, String lineToEdit) throws IOException {
        File tempFile = new File("Store//Temp.txt");
        File inputFile = gr.getFile();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToEdit)){
                String newPrice = String.valueOf(pr.getPrice());
                if(newPrice.endsWith(".0")) newPrice = newPrice.substring(0,newPrice.length()-2);
                String newCount = String.valueOf(pr.getDCount());
                if(newCount.endsWith(".0")) newCount = newCount.substring(0,newCount.length()-2);
                currentLine = pr.getName() + " / " + pr.getDescription() + " / " + pr.getMaker() + " / " + newPrice + " грн / " + newCount + pr.getEnding();
            }
            writer.write(currentLine + "\n");
        }
        reader.close();
        writer.close();
        rewriteFiles(tempFile,inputFile);
    }

    private void deleteProduct(Group gr) throws EmptyProductsException{
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
        JComponent[] array = {viewProducts,b};
        setObjectsFont(array,panel);
        b.setVisible(true);
        viewProducts.setVisible(true);
        revalidate();
        repaint();
    }

    private void deleteFromFile(Group gr, Product pr) throws IOException {
        changeGroupSize(gr);
        File inputFile = gr.getFile();
        File tempFile = new File("Store//Temp.txt");
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

    private void rewriteFiles(File tempFile, File inputFile) throws IOException {
        String fileName = inputFile.getName();
        boolean successfulDel = inputFile.delete();
        boolean renameTemp = tempFile.renameTo(new File("Store//"+fileName));
        if(!successfulDel)
            throw new IOException("Temp file was not deleted!");
        if(!renameTemp)
            throw new IOException("Editing file was not successful!");
    }

    private void changeGroupSize(Group gr) throws IOException {
        File inputFile = gr.getFile();
        File tempFile = new File("Store//Temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String groupLine = reader.readLine();
        Pattern headerPattern = Pattern.compile("Group\\s+'([A-Za-zА-ЯІЇЄа-іїє ]*)'\\s+size\\s+(\\d+)\\s+description\\s+'([A-Za-zА-ЯІЇЄа-іїє\\s]*)'");
        Matcher matcher = headerPattern.matcher(groupLine);
        if(matcher.matches())
            groupLine = "Group '" + matcher.group(1) + "' size " + gr.getNumberOfProducts() + " description '" + matcher.group(3) + "'";
        writer.write(groupLine + "\n");
        String s;
        while((s = reader.readLine()) != null) {
            writer.write(s + "\n");
        }
        writer.close();
        reader.close();
        rewriteFiles(tempFile,inputFile);
    }

    private void chooseProduct(Group gr) {
        JComboBox<Product> viewProducts = new JComboBox<>();
        for (Product product : gr.getProducts()) viewProducts.addItem(product);
        try {
            viewProducts.addActionListener(selectPr -> {
                resetPanel();
                JButton add = new JButton("Додати");
                JButton remove = new JButton("Списати");
                Product pr = (Product) viewProducts.getSelectedItem();
                JButton submit = new JButton("Submit");
                JSpinner spinner = new JSpinner();
                final boolean[] choice = {false};
                submit.addActionListener(sub -> {
                    try {
                        productSetCount(gr, pr, spinner.getValue(), choice[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
                add.addActionListener(press -> {
                    resetPanel();
                    spinner.setModel(model(pr,true));
                    choice[0] = true;
                    setObjectsFont(new JComponent[]{spinner, submit},panel);
                    revalidate();
                    repaint();
                });
                remove.addActionListener(press -> {
                    resetPanel();
                    if(pr != null) {
                        spinner.setModel(model(pr,false));
                        choice[0] = false;
                        setObjectsFont(new JComponent[]{spinner, submit},panel);
                    }
                    revalidate();
                    repaint();
                });
                setObjectsFont(new JButton[]{add, remove},panel);
                revalidate();
                repaint();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        setObjectsFont(new JComboBox[]{viewProducts},panel);
    }

    private void productSetCount(Group gr, Product pr, Object count, boolean choice) throws IOException {
        if((double) count == 0.0) return;
        dispose();
        String lineToEdit = pr.toString();
        String message;
        int valueI;
        double valueD;
        if(pr instanceof PieceProduct){
            valueI = (int) count;
            if(choice) message = "Ви закупили товар на суму " + valueI*pr.getPrice() + " грн";
            else{
                message = "Ви продали товар на суму " + valueI*pr.getPrice() + " грн";
                valueI*=(-1);
            }
            valueI = pr.getCount() + valueI;
            pr.setCount(valueI);
        }
        else{
            valueD = (double) count;
            if(choice) message = "Ви закупили товар на суму " + valueD*pr.getPrice() + " грн";
            else{
                message = "Ви продали товар на суму " + valueD*pr.getPrice() + " грн";
                valueD*=(-1);
            }
            valueD = pr.getDCount() + valueD;
            pr.setCount(valueD);
        }
        JOptionPane.showMessageDialog(null, message, "Успішна операція", JOptionPane.INFORMATION_MESSAGE);
        editFile(gr, pr, lineToEdit);
    }

    private SpinnerModel model(Product pr, boolean choice){

        SpinnerModel model;
        if(pr instanceof LiquidProduct || pr instanceof WeightProduct){
            double max;
            if(choice) max = 1000;
            else max = pr.getDCount();
            model = new SpinnerNumberModel(
                    0.0,
                    0.0,
                    max,
                    0.5
            );
        }
        else{
            int max;
            if(choice) max = 1000;
            else max = pr.getCount();
            model = new SpinnerNumberModel(
                    0,
                    0,
                    max,
                    1
            );
        }
        return model;
    }
}