package model.builder;

import java.util.ArrayList;
import java.util.List;
import model.animations.IAnimation;
import model.animations.TypeOfAnimation;
import model.shapes.Ellipse;
import model.shapes.IShapes;
import model.shapes.Rectangle;
import model.shapes.TypeOfShape;

public class AnimationModel implements IAnimationModel {

  private List<IShapes> shapeList;
  protected int leastX;
  protected int maxY;
  protected int height;
  protected int width;

  public AnimationModel() {
    this.shapeList = new ArrayList<>();
  }


  @Override
  public void addShape(IShapes shape) throws IllegalArgumentException{
    if(findShape(shape.getName()) != null){
      throw new IllegalArgumentException("Shape with that name is already in the model");
    }
    if (shape == null) {
      throw new IllegalArgumentException("shape cannot be null");
    }
    shapeList.add(shape);
  }

  @Override
  public void addShape(int start, int end, String name, double x, double y, double param1,
      double param2,
      int r, int g, int b, List<IAnimation> animations, TypeOfAnimation shapeType) {
    if (findShape(name)==null) {
      if (shapeType.equals(TypeOfShape.ELLIPSE)) {
        shapeList.add(new Ellipse(name, x, y, param1, param2, r, g, b, start, end, animations));
      } else if (shapeType.equals(TypeOfShape.RECTANGLE)) {
        shapeList.add(new Rectangle(name, x, y, param1, param2, r, g, b, start, end, animations));
      } else {
        throw new IllegalArgumentException("Type of shape not valid");
      }
    }
    else {
      throw new IllegalArgumentException("Shape by that name is already in the model");
    }
  }

  @Override
  public void removeShape(String name) {
    IShapes shape = findShape(name);
    shapeList.remove(shape);

  }

  //todo edit for our modeL DO NOT LEAVE UNEDITED
  @Override
  public String getState(int tic) {

    String state = "";

    for (IShapes shape : shapeList) {
      state += "\nName: " + shape.getName() + "\nType: ";
      if (shape instanceof Rectangle) {
        state += "rectangle\n";
      } else {
        state += "oval\n";
      }

      // For 2 fps | can abstract this later
      state += "Appears at t=" + (shape.getTimeAppear() * 1000 / tic) + " ms";
      state += "\nDisappears at t=" + (shape.getTimeDisappear() * 1000 / tic) + " ms";
    }

    state += "\n";

    for (IShapes shape : shapeList) {
      for (IAnimation motion : shape.getAnimations()) {
        state += "\nShape " + shape.getName() + " " + motion.toString() + "\n";
      }
    }

    return state;
  }

  @Override
  public List<IShapes> getAllShapes() {
    //creates a copy of the shapeList and returns it
    List<IShapes> shapes = new ArrayList<>(shapeList);
    return shapes;
  }

  @Override
  public void addMotion(IShapes shape, IAnimation animation) {
    if (findShape(shape.getName()) == null) {
      throw new IllegalArgumentException("No shape by that name found");
    }
    shape.addAnimation(animation);
  }

  @Override
  public void addMotion(String name, IAnimation animation) {
    IShapes shape = findShape(name);
    if (shape == null) {
      throw new IllegalArgumentException("No shape by that name found");
    }
    shape.addAnimation(animation);
  }

  @Override
  public List<IShapes> produceFrame(int tic) throws IllegalArgumentException{
    if(tic < 0){
      throw new IllegalArgumentException("frame cannot be negative.");
    }
    List<IShapes> interpolated = new ArrayList<>();

    for (IShapes shape : shapeList) {
      //checks if the shape would appear at that time frame
      if (shape.getTimeAppear() <= tic && shape.getTimeDisappear() >= tic) {
        for (IAnimation animation : shape.getAnimations()) {
          //checks if that animation would occur at that tic point
          if (animation.getStartTime() <= tic && animation.getEndTime() >= tic) {
            //gets the list of paramaters for the shape
            List params = animation.getParams();
            if (animation.getTypeOfAnimation() == TypeOfAnimation.SETCOLOR) {
              //need to cast as a double first so tween treats params.get as a double and
              // not an object. We then cast that afterwards as an int so the color is properly set
              int r = (int)(animation.tweenValue((double) params.get(0),
                  (double) params.get(3), tic));
              int g = (int)(animation.tweenValue((double) params.get(1),
                  (double) params.get(4), tic));
              int b = (int)(animation.tweenValue((double) params.get(2),
                  (double) params.get(5), tic));
              shape.setColor(r, g, b);
              break;
            }
            else if (animation.getTypeOfAnimation() == TypeOfAnimation.SETCENTER) {
                shape.setX(animation.tweenValue((double) params.get(0), (double) params.get(2),
                    tic));
                shape.setY(animation.tweenValue((double) params.get(1), (double) params.get(3),
                    tic));
                break;
            }
            else if(animation.getTypeOfAnimation() == TypeOfAnimation.SETSIZE) {
                shape.setParam1(animation.tweenValue((double) params.get(0), (double) params.get(2),
                    tic));
                shape.setParam2(animation.tweenValue((double) params.get(1), (double) params.get(3),
                    tic));
                break;
            }
            else{
                throw new IllegalArgumentException("No animation of that type found");
            }
          }
        }
        interpolated.add(shape);
      }
    }
    return interpolated;
  }

  /**
   * Helper function to find a shape by its name.
   * @param name of the function wanted to be found
   * @return the shape with that name, or null if no shape by that name found
   * @throws IllegalArgumentException If the shape is not found in the list, or the list is null
   */
  protected IShapes findShape(String name) throws IllegalArgumentException {
    if(name == null){
      throw new IllegalArgumentException("Name cannot be null");
    }
    if(shapeList == null){
      throw new IllegalArgumentException("ShapeList is null");
    }
    if (shapeList != null) {
      for (IShapes shape : shapeList) {
        if (shape.getName().equals(name)) {
          return shape;
        }
      }
    }
    return null;
  }


  public void setBounds(int x, int y, int width, int height) {
    this.leastX = x;
    this.maxY = y;
    this.width = width;
    this.height = height;
  }

}


