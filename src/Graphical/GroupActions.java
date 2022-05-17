package Graphical;

import DataTypes.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class GroupActions extends JFrame {
    JPanel panel;
    MainWindow window;

    GroupActions(MainWindow window, Action action){
        setTitle("Група");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.window = window;
        init();
        switch (action) {
            case ADD:
                addGroup();
                break;
            case DELETE:
                try {
                    if(MainWindow.store.isEmpty())
                        throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
                    deleteGroup();
                } catch (EmptyGroupsException e) {
                    this.dispose();
                    e.printStackTrace();
                }
                break;
            case EDIT:
                try {
                    if(MainWindow.store.isEmpty())
                        throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
                    editGroup();
                } catch (EmptyGroupsException e) {
                    e.printStackTrace();
                    this.dispose();
                }
                break;
        }

    }

    private void init() {
        setSize(300,200);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x,y);
        panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(90, 200));
        add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private void addGroup() {
        JButton fromFile = new JButton("Додати з файлу");
        JButton input = new JButton("Створити вручну");
        setObjectsFont(new JComponent[]{fromFile,input},panel);
        fromFile.addActionListener(press -> {
            dispose();
            addFile();
        });
        input.addActionListener(press -> {
            /*створення форми для введення групи вручну*/
            addGroupInput();
        });
    }

    private void addGroupInput() {
        resetPanel();
        JLabel nameLabel = new JLabel("Назва: ");
        JTextArea name = new JTextArea(1, 10);
        JLabel descriptionL = new JLabel("Опис: ");
        JTextArea description = new JTextArea(1, 10);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        GridLayout layout = new GridLayout(2,2);
        layout.setVgap(10);
        JPanel paramsPanel = new JPanel(layout);
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            boolean correct;
            try {
                if (name.getText().isEmpty() || description.getText().isEmpty())
                    throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                if (MainWindow.store.fileExist(new File("Store//" + name.getText() + ".txt")))
                    throw new GroupExistException("Група з такою назвою уже існує!");
                correct = true;
                try {
                    createGroupFile(name.getText(), description.getText());
                } catch (IOException | DataExceptions e) {
                    e.printStackTrace();
                }
            } catch (IllegalInputFormat | GroupExistException e) {
                name.setText("");
                description.setText("");
                correct = false;
                e.printStackTrace();
            }
            if (correct){
                dispose();
                this.window.refreshStore();
            }
        });
        JComponent[] array = {nameLabel,name,descriptionL,description};
        setObjectsFont(array,paramsPanel);
        panel.add(paramsPanel);
        setObjectsFont(new JComponent[]{submit},panel);
        revalidate();
        repaint();
    }

    private void createGroupFile(String name, String description) throws IOException, DataExceptions {
        File f = new File("Store/"+name + ".txt");
        FileWriter fw = new FileWriter(f);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write("Group '" + name + "' size 0 description '" + description + "'\n");
        writer.close();
        fw.close();
        Group gr = new Group(f);
        MainWindow.store.add(gr);
        this.window.createTopPanel();
    }

    private void deleteGroup(){
        JLabel label = new JLabel("Оберіть групу, яку хочете видалити: ");
        JComboBox<Group> viewGroups = new JComboBox<>();
        for (Group group : MainWindow.store) viewGroups.addItem(group);
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            try {
                MainWindow.store.remove(viewGroups.getSelectedIndex());
                File fileToDelete = new File("Store//" + viewGroups.getSelectedItem() + ".txt");
                boolean delSuccessful = fileToDelete.delete();
                if(!delSuccessful)
                    throw new Exception("Файл не видалено");
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose();
            this.window.createTopPanel();
            this.window.refreshStore();
        });
        setObjectsFont(new JComponent[]{label,viewGroups,submit},panel);
        this.setSize(label.getPreferredSize().width+30, 200);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dimension.getWidth() - this.getWidth()) / 2,(int)(dimension.getHeight() - this.getHeight()) / 2);
    }

    private void editGroup(){
        JLabel label1 = new JLabel("Оберіть групу, яку хочете редагувати: ");
        JLabel label2 = new JLabel("Оберіть параметри для редагування: ");
        JCheckBox name = new JCheckBox("Назва");
        JCheckBox description = new JCheckBox("Опис");
        JComboBox<Group> viewGroups = new JComboBox<>();
        for (Group group : MainWindow.store) viewGroups.addItem(group);
        JButton b = new JButton("Submit");
        b.addActionListener(press -> {
            Group gr;
            try {
                gr = MainWindow.store.get(viewGroups.getSelectedIndex());
                editGroupParams(gr, name.isSelected(), description.isSelected());
            } catch (DataExceptions e) {
                e.printStackTrace();
            }
        });
        JComponent[] array = {label1,viewGroups,label2,name,description,b};
        setObjectsFont(array,panel);
        this.setSize(label1.getPreferredSize().width+30, 200);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dimension.getWidth() - this.getWidth()) / 2,(int)(dimension.getHeight() - this.getHeight()) / 2);
    }

    private void editGroupParams(Group gr, boolean nameSelected, boolean descriptionSelected) {
        if(!nameSelected && !descriptionSelected){
            dispose();
            return;
        }
        resetPanel();
        JLabel label = new JLabel("Група: " + gr.getName());
        JTextArea name = null;
        JTextArea description = null;
        GridLayout layout = new GridLayout(nameSelected && descriptionSelected ? 2 : 1, 2);
        layout.setVgap(10);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(label);
        JPanel paramsPanel = new JPanel(layout);
        if(nameSelected){
            JLabel nameL = new JLabel("Редагуйте назву: ");
            name = new JTextArea(1, 8);
            setObjectsFont(new JComponent[]{nameL, name},paramsPanel);
        }
        if(descriptionSelected){
            JLabel descriptionL = new JLabel("Редагуйте опис: ");
            description = new JTextArea(1, 15);
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            setObjectsFont(new JComponent[]{descriptionL, description},paramsPanel);
        }
        JButton submit = new JButton("Submit");
        JTextArea finalName = name;
        JTextArea finalDescription = description;
        submit.addActionListener(press -> {
            boolean correct;
            try {
                String n, d;
                if (nameSelected) {
                    if (finalName.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    if (MainWindow.store.fileExist(new File("Store//" + finalName.getText() + ".txt")))
                        throw new GroupExistException("Група з такою назвою уже існує!");
                    n = finalName.getText();
                } else n = gr.getName();
                if (descriptionSelected) {
                    if (finalDescription.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    d = finalDescription.getText();
                } else d = gr.getDescription();
                correct = true;
                editFile(gr, n, d);
                gr.setName(n);
                gr.setFile(new File("Store//" + n + ".txt"));
                gr.setDescription(d);
            } catch (IllegalInputFormat | IOException | GroupExistException e) {
                if(nameSelected) finalName.setText("");
                if(descriptionSelected) finalDescription.setText("");
                correct = false;
                e.printStackTrace();
            }
            if(correct) {
                dispose();
                this.window.createTopPanel();
                this.window.refreshStore();
            }
        });
        submit.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(paramsPanel);
        panel.add(submit);
        revalidate();
        repaint();
    }

    private void editFile(Group gr, String n, String d) throws IOException {
        File groupFile = gr.getFile();
        File tempFile = new File("Store//Temp.txt");
        BufferedReader reader = new BufferedReader(new FileReader(groupFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToEdit = "Group '" + gr.getName() + "' size " + gr.getNumberOfProducts() + " description '" + gr.getDescription() + "'";
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToEdit)){
                currentLine = "Group '" + n + "' size " + gr.getNumberOfProducts() + " description '" + d + "'";
            }
            writer.write(currentLine + "\n");
        }
        reader.close();
        writer.close();

        boolean successfulDel = groupFile.delete();
        boolean renameTemp = tempFile.renameTo(new File("Store//" + n + ".txt"));
        if(!successfulDel)
            throw new IOException("Temp file was not deleted!");
        if(!renameTemp)
            throw new IOException("Editing file was not successful!");
    }

    public void resetPanel() {
        remove(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.setSize(500,300);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x,y);
        this.add(panel);
    }

    /**Додавання файлу з групою до списку*/
    private void addFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.addChoosableFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (file != null) {
                try {
                    if(MainWindow.store.fileExist(file)){
                        throw new GroupExistException("Така група вже існує!");
                    }
                    Group group = new Group(file);
                    MainWindow.store.add(group);
                    this.window.createTopPanel();
                    this.window.refreshStore();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void setObjectsFont(JComponent[] array, JPanel panel) {
        for (JComponent jComponent : array) {
            jComponent.setFont(new Font("Arial", Font.PLAIN, 20));
            panel.add(jComponent);
        }
    }
}
