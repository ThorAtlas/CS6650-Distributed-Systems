package ezpeasyanimator.view;

import ezpeasyanimator.model.IAnimationModel;
import ezpeasyanimator.model.shapes.IShapes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class VisualView extends JFrame implements IView {

  DrawingPanel drawingPanel;
  JButton play;
  JButton pause;

  /**
   * A constructor for the swing view class.
   */
  public VisualView() {
    drawingPanel = new DrawingPanel();
    drawingPanel.setBackground(Color.white);
    drawingPanel.setPreferredSize(new Dimension(800, 800));
    setSize(1000, 1000);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JScrollPane pane = new JScrollPane(drawingPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    setVisible(true);
  }

  @Override
  public void render(IAnimationModel model, Appendable out, int fps) {
    drawingPanel.drawShapes(model.getShapeList());

  }

//  @Override
//  public void render(List<IShapes> shapes) {
//
//  }

  @Override
  public void showWindow(boolean value) {

  }

  @Override
  public StringBuilder getTextView() throws IllegalStateException {
    return null;
  }
}


