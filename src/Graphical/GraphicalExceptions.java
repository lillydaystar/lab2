package Graphical;

import javax.swing.*;

class EmptyGroupsException extends Exception{

    EmptyGroupsException(){}

    EmptyGroupsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодної групи","Error",  JOptionPane.ERROR_MESSAGE);
    }
}

class EmptyProductsException extends Exception{

    EmptyProductsException(){}

    EmptyProductsException(String msg){
        super(msg);
        JOptionPane.showMessageDialog(null, "Не створено/додано жодного товару","Error",  JOptionPane.ERROR_MESSAGE);
    }
}

class IllegalInputFormat extends Exception{
    public IllegalInputFormat() {
        super();
    }

    public IllegalInputFormat(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Некоректне введення даних","Error",  JOptionPane.WARNING_MESSAGE);
    }
}

class GroupExistException extends Exception{
    public GroupExistException(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Така група вже існує","Error",  JOptionPane.WARNING_MESSAGE);
    }
}