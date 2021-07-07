import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.awt.*;

public class shapes {

  public static void main(String[] args) {
    JFrame frame = new JFrame("test");
    frame.setVisible(true);
    frame.setSize(400,200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    draw object = new draw();
    frame.add(object);

    object.drawing();
  }

}
