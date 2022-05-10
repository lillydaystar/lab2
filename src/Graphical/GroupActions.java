package Graphical;

import DataTypes.Group;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class GroupActions extends JFrame {
    JPanel panel;
    MainWindow window;

    GroupActions(MainWindow window){
        setTitle("Група");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.window = window;
        init(window);
    }

    private void init(MainWindow window) {
        setSize(300,300);
        setLocation(600,400);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(panel);
        JButton add = new JButton("Додати");
        JButton edit = new JButton("Редагувати");
        JButton delete = new JButton("Видалити");
        panel.add(add);
        panel.add(edit);
        panel.add(delete);
        add.addActionListener(press -> {
            remove(panel);
            addGroup();
        });

        edit.addActionListener(press -> {
            try {
                remove(panel);
                editGroup();
            } catch (EmptyGroupsException e) {
                e.printStackTrace();
            }
        });

        delete.addActionListener(press -> {
            try {
                remove(panel);
                deleteGroup();
            } catch (EmptyGroupsException e) {
                e.printStackTrace();
            }
        });
    }

    private void addGroup() {
        resetPanel();
        JButton fromFile = new JButton("Додати з файлу");
        JButton input = new JButton("Створити вручну");
        panel.add(fromFile);
        panel.add(input);
        fromFile.addActionListener(press -> {
            addFile();
            dispose();
            window.createTopPanel();
        });
        input.addActionListener(press -> {
            /*створення форми для введення групи вручну*/
        });
        pack();
    }

    private void deleteGroup() throws EmptyGroupsException{
    }

    private void editGroup() throws EmptyGroupsException{
    }

    public void resetPanel() {
        this.revalidate();
        this.repaint();
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
}
