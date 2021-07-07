package model.builder;

import model.animations.IAnimation;
import model.animations.SetCenter;
import model.animations.SetColor;
import model.animations.SetSize;
import model.animations.SetStationary;
import model.shapes.Ellipse;
import model.shapes.IShapes;
import model.shapes.Rectangle;

public class Builder implements AnimationBuilder{

  private IAnimationModel model;


  /**
   * Constructor for the builder that builds a new animation model.
   */
  public Builder() {

    model = new AnimationModel();
  }

  @Override
  public Object build() {
    return this.model;
  }

  @Override
  public AnimationBuilder setBounds(int x, int y, int width, int height) {
    this.model.setBounds(x,y,width,height);
    return this;
  }

  @Override
  public AnimationBuilder declareShape(String name, String type) {
    if (type.equalsIgnoreCase("rectangle")) {
      IShapes newShape = new Rectangle(name);
      model.addShape(newShape);
    }
    if (type.equalsIgnoreCase("ellipse")) {
      IShapes newShape = new Ellipse(name);
      model.addShape(newShape);
    }
    return this;

  }

  @Override
  public AnimationBuilder addMotion(String name, int t1, int x1, int y1, int w1, int h1, int r1,
      int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2) {
    if (x1 == x2 && y1 == y2
        && w1 == w2 && h1 == h2
        && r1 == r2 && g1 == g2 && b1 == b2) {
      this.model.addMotion(name, new SetStationary(t1, x1, y1,
              w1, h1,
              r1, g1, b1,
              t2, x2, y2,
              w2, h2,
              r2, g2, b2)
          );
    }

    if (x1  != x2 || y1 != y2) {
      this.model.addMotion(name, new SetCenter(t1,t2, x1, y1, x2,y2));
    }
    if (w1  != w2 || h1 != h2) {
      this.model.addMotion(name, new SetSize(t1,t2, w1, h1, w2,h2));
    }
    if (r1  != r2 || g1 != g2 || b1 != b2) {
      this.model.addMotion(name, new SetColor(t1,t2, r1,g1,b1, r2, g2, b2));
    }
    return this;
  }

  @Override
  public IAnimationModel getModel() {
    return model;
  }
}
