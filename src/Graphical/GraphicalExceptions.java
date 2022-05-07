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