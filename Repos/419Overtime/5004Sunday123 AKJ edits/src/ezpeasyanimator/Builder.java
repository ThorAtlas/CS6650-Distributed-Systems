package ezpeasyanimator;

import ezpeasyanimator.model.AnimationModel;
import ezpeasyanimator.model.IAnimationModel;
import ezpeasyanimator.model.animations.IAnimation;
import ezpeasyanimator.model.builder.AnimationBuilder;
import ezpeasyanimator.model.shapes.Ellipse;
import ezpeasyanimator.model.shapes.IShapes;
import ezpeasyanimator.model.shapes.Rectangle;

/**
 * A public class for the builder that implements the animation builder interface.
 */
public class Builder implements AnimationBuilder {
  //todo change to IAnimationModel and create interface for it and change it here
  private AnimationModel model;

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
  public AnimationBuilder<IAnimationModel> setBounds(int x, int y, int width, int height) {
    this.model.setBounds(x,y,width,height);
    return this;
  }

  //adds an empty shell of a shape to the list based on the text input
  @Override
  public AnimationBuilder<IAnimationModel> declareShape(String name, String type) {
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

  //first call of addMotion will
  @Override
  public AnimationBuilder<IAnimationModel> addMotion(String name, int t1, int x1, int y1, int w1, int h1,
      int r1, int g1, int b1, int t2, int x2, int y2,
      int w2, int h2, int r2, int g2, int b2)
      throws IllegalArgumentException {

//    IShapes object = null;
//    for (IShapes shape : this.model.getShapeList()) {
//      if (shape.getName() == name) {
//        object = shape;
//        break;
//      }
//    }
//    if (object == null) {
//      throw new IllegalArgumentException("Name of shape not found");
//    }
    if (x1 == x2 && y1 == y2
        && w1 == w2 && h1 == h2
        && r1 == r2 && g1 == g2 && b1 == b2) {
      this.model.setAttributes(name,
          t1, x1, y1,
          w1, h1,
          r1, g1, b1,
          t2, x2, y2,
          w2, h2,
          r2, g2, b2);
    }

    if (x1  != x2 || y1 != y2) {
      this.model.animationSetCenter(name, t1, t2, x2, y2);
    }
    if (w1  != w2 || h1 != h2) {
      this.model.animationSetSize(name, t1, t2, w2, h2);
    }
    if (r1  != r2 || g1 != g2 || b1 != b2) {
      this.model.animationSetColor(name, t1, t2, r2,g2,b2);
    }
    return this;
  }

  public AnimationModel getModel() {
    return model;
  }
}
