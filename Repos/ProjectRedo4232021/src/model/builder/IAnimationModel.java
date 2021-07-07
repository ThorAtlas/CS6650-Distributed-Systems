package model.builder;

import java.util.List;
import model.animations.IAnimation;
import model.animations.TypeOfAnimation;
import model.shapes.IShapes;

public interface IAnimationModel {
  /**
   * Adds a new shape with the given parameters to the model.
   *
   * @param name      string name for the shape and animation
   * @param shapeType   the type of shape
   * @param x       x parameter of the center
   * @param y       y parameter of the center position
   * @param param1  the first parameter(width/xRaidus)
   * @param param2  the second parameter(length/yRaidus)
   * @param r       red component of the shape's color
   * @param g       green component of the shape's color
   * @param b       blue component of the shape's color
   * @param start   start of life for the shape
   * @param end     end of life for the shape
   * @param animations list of all the animations to be applied for this shape
   */
  void addShape(int start, int end, String name, double x, double y, double param1, double param2,
      int r, int g, int b,  List<IAnimation> animations, TypeOfAnimation shapeType);

  /**
   * Adds a shape based on an already created shape.
   * @param shape
   */
  void addShape(IShapes shape);

  /**
   * Removes from the model the shape and animation linked to the given string name.
   *
   * @param name unique string identifier
   */
  void removeShape(String name);

  /**
   * Returns a string representation of the shapes contained in the model paired with their
   * associated animations at a certain tic.
   *
   * @return string listing of shapes and animations
   */
  String getState(int tic);

  /**
   * Returns a list containing all the shapes in the model.
   *
   * @return list of all shapes
   */
  List<IShapes> getAllShapes();


  /**
   * Adds a given motion to the list of motions contained by the shape associated with the given
   * name.
   *
   * @param name     identifier for the shape
   * @param animations motion to be added
   */
  void addMotion(String name, IAnimation animations);

  //todo javadoc for this
  void addMotion(IShapes shape, IAnimation animation);

  /**
   * Produces a list of shapes which should be drawn on the given frame.
   *
   * @param frame given frame of animation
   * @return list of shapes
   */
  List<IShapes> produceFrame(int frame);


  void setBounds(int x, int y, int width, int height);
}
