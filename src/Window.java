import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    Window(){
        super("Product manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel window = new JPanel(new GridLayout());
        this.setSize(555,555);
        this.add(window);
    }

}
