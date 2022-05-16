package Graphical;

import javax.swing.*;

public class GraphicalExceptions extends Exception{
    public GraphicalExceptions() {
        super();
    }

    public GraphicalExceptions(String message) {
        super(message);
    }
}

class EmptyGroupsException extends GraphicalExceptions{

    EmptyGroupsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодної групи","Error",  JOptionPane.ERROR_MESSAGE);
    }
}

class EmptyProductsException extends GraphicalExceptions{

    EmptyProductsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодного товару","Error",  JOptionPane.ERROR_MESSAGE);
    }
}

class IllegalInputFormat extends GraphicalExceptions{

    public IllegalInputFormat(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Некоректне введення даних","Error",  JOptionPane.WARNING_MESSAGE);
    }
}

class GroupExistException extends GraphicalExceptions{
    public GroupExistException(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Така група вже існує","Error",  JOptionPane.WARNING_MESSAGE);
    }
}

class ProductExistException extends GraphicalExceptions{
    public ProductExistException(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Такий товар вже існує","Error",  JOptionPane.WARNING_MESSAGE);
    }
}