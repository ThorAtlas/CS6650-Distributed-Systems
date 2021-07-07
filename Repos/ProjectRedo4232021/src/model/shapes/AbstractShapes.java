package model.shapes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import model.animations.IAnimation;

public abstract class AbstractShapes implements IShapes{
  protected String name;
  protected double x;
  protected double y;
  protected double param1;
  protected double param2;
  protected int r;
  protected int g;
  protected int b;
  protected TypeOfShape shapeType;
  protected int timeAppear;
  protected int timeDisappear;
  protected List<IAnimation> animationList;

  public AbstractShapes(String name, TypeOfShape shapeType){
    this.name = name;
    this.shapeType = shapeType;
    this.animationList = new ArrayList<>();
  }

  /**
   * Constructor for the Abstract shapes class.
   *
   * @param x for the x coordinate
   * @param y for the y coordinate
   * @param r for the red color
   * @param g for the green color
   * @param b for the blue color
  //todo parameters for this
  //todo exceptions for creation of shapes
   */
  public AbstractShapes(String name, double x, double y, int r, int g,
      int b, double param1, double param2) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("Name of an animated object cannot be null");
    }
//    if (timeAppear < 0 || timeDisappear < 0) {
//      throw new IllegalArgumentException("Time appear/disappear cannot be negative");
//    }
    if (r < 0 || g < 0 || b < 0) {
      throw new IllegalArgumentException("Color values cannot be negative");
    }
    if (param1 <= 0 || param2 <= 0) {
      throw new IllegalArgumentException("Parameters cannot be zero or negative");
    }

    this.animationList = new ArrayList<>();

    //x,y of center of shape
    this.x = x;
    this.y = y;

    //rgb colors of shape
    this.r = r;
    this.g = g;
    this.b = b;

    this.param1 = param1;
    this.param2 = param2;

    this.name = name;
  }



  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public double getParam1() {
    return param1;
  }

  @Override
  public double getParam2() {
    return param2;
  }

  @Override
  public TypeOfShape getShapeType() {
    return this.shapeType;
  }
}
