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
                    if(window.store.isEmpty())
                        throw new EmptyGroupsException("Жодної групи не існує, будь ласка, створіть або додайте хоча б одну.");
                    deleteGroup();
                } catch (EmptyGroupsException e) {
                    this.dispose();
                    e.printStackTrace();
                }
                break;
            case EDIT:
                try {
                    if(window.store.isEmpty())
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
        setSize(600,300);
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
        setObjectsFont(new JComponent[]{fromFile,input});
        fromFile.addActionListener(press -> {
            addFile();
            dispose();
            window.createTopPanel();
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
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            try {
                if (name.getText().isEmpty() || description.getText().isEmpty())
                    throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                if(window.store.fileExist(new File("Store//" + name.getText()+".txt")))
                    throw new GroupExistException("Група з такою назвою уже існує!");
                try {
                    createGroupFile(name.getText(), description.getText());
                } catch (IOException | DataExceptions e) {
                    e.printStackTrace();
                }
            }
            catch (IllegalInputFormat | GroupExistException e) {
                e.printStackTrace();
            }
            dispose();
        });
        JComponent[] array = {nameLabel,name,descriptionL,description,submit};
        setObjectsFont(array);
        revalidate();
        repaint();
    }

    private void createGroupFile(String name, String description) throws IOException, DataExceptions {
        File f = new File("Store//"+name + ".txt");
        FileWriter fw = new FileWriter(f);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write("Group '" + name + "' size 0 description '" + description + "'\n");
        writer.close();
        fw.close();
        Group gr = new Group(f);
        window.store.add(gr);
    }

    private void deleteGroup(){
        JComboBox<Group> viewGroups = new JComboBox<>();
        for (Group group : window.store) viewGroups.addItem(group);
        JButton submit = new JButton("Submit");
        submit.addActionListener(press -> {
            try {
                window.store.remove(viewGroups.getSelectedIndex());
                File fileToDelete = new File("Store//" + viewGroups.getSelectedItem() + ".txt");
                boolean delSuccessful = fileToDelete.delete();
                if(!delSuccessful)
                    throw new Exception("Файл не видалено");
            } catch (Exception e) {
                e.printStackTrace();
            }
            dispose();
        });
        setObjectsFont(new JComponent[]{viewGroups,submit});
    }

    private void editGroup(){
        JCheckBox name = new JCheckBox("Назва");
        JCheckBox description = new JCheckBox("Опис");
        JComboBox<Group> viewGroups = new JComboBox<>();
        for (Group group : window.store) viewGroups.addItem(group);
        JButton b = new JButton("Submit");
        b.addActionListener(press -> {
            Group gr;
            try {
                gr = window.store.get(viewGroups.getSelectedIndex());
                editGroupParams(gr, name.isSelected(), description.isSelected());
            } catch (DataExceptions e) {
                e.printStackTrace();
            }
        });
        JComponent[] array = {viewGroups,name,description,b};
        setObjectsFont(array);
    }

    private void editGroupParams(Group gr, boolean nameSelected, boolean descriptionSelected) {
        resetPanel();

        JTextArea name = null;
        JTextArea description = null;
        if(nameSelected){
            JLabel nameL = new JLabel("Редагуйте назву: ");
            name = new JTextArea(1, 8);
            setObjectsFont(new JComponent[]{nameL, name});
        }
        if(descriptionSelected){
            JLabel descriptionL = new JLabel("Редагуйте опис: ");
            description = new JTextArea(1, 10);
            setObjectsFont(new JComponent[]{descriptionL, description});
        }
        JButton submit = new JButton("Submit");
        JTextArea finalName = name;
        JTextArea finalDescription = description;
        submit.addActionListener(press -> {
            try{
                String n, d;
                if(nameSelected){
                    if(finalName.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    if(window.store.fileExist(new File("Store//" + finalName.getText()+".txt")))
                        throw new GroupExistException("Група з такою назвою уже існує!");
                    n = finalName.getText();
                } else n = gr.getName();
                if(descriptionSelected){
                    if(finalDescription.getText().isEmpty())
                        throw new IllegalInputFormat("Не залишайте порожні поля!!!");
                    d = finalDescription.getText();
                } else d = gr.getDescription();

                editFile(gr, n, d);
                gr.setName(n);
                gr.setDescription(d);
            }catch(IllegalInputFormat | IOException | GroupExistException e){
                e.printStackTrace();
            }
            dispose();
        });
        submit.setFont(new Font("Arial", Font.PLAIN, 20));
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
                    if(window.store.fileExist(file)){
                        throw new GroupExistException("Така група вже існує!");
                    }
                    Group group = new Group(file);
                    window.store.add(group);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void setObjectsFont(JComponent[] array) {
        for (JComponent jComponent : array) {
            jComponent.setFont(new Font("Arial", Font.PLAIN, 20));
            panel.add(jComponent);
        }
    }
}
