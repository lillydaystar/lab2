package Graphical;

import java.awt.*;

class Tools {

    static Point getCenterLocation(int width, int height) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((screen.width - width)/2, (screen.height - height)/2);
    }
}