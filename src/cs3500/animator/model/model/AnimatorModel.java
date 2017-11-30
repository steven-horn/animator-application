package cs3500.animator.model.model;

import java.util.ArrayList;

import cs3500.animator.model.actions.ColorChange;
import cs3500.animator.model.actions.Move;
import cs3500.animator.model.actions.Scale;
import cs3500.animator.model.misc.IMyColor;
import cs3500.animator.model.misc.IPosn;
import cs3500.animator.model.shapes.IShape;
import cs3500.animator.model.misc.MyColor;
import cs3500.animator.model.misc.Posn;
import cs3500.animator.model.shapes.MyOval;
import cs3500.animator.model.shapes.MyRectangle;
import cs3500.animator.model.shapes.ShapeType;
import cs3500.animator.model.actions.ActionType;
import cs3500.animator.model.actions.IAction;
import cs3500.animator.view.TweenModelBuilder;

/**
 * Maintains and manipulates the data in a Simple Animator.
 */
public class AnimatorModel implements IAnimatorOperations {

  private ArrayList<IShape> shapes;
  private ArrayList<IAction> actions;

  /**
   * Constructs an AnimatorModel.
   */
  public AnimatorModel() {
    shapes = new ArrayList<>();
    actions = new ArrayList<>();
  }

  @Override
  public void createShape(ShapeType st, String name, IPosn location, IPosn dimensions, int sides,
                          IMyColor color, IPosn lifetime) {
    IShape s = null;
    if (st == ShapeType.OVAL) {
      s = new MyOval(name, location, dimensions, color, lifetime);
    }
    else {
      s = new MyRectangle(name, location, dimensions, color, lifetime);
    }
    shapes.add(s);
  }

  @Override
  public void createAction(ActionType at, String shapeName, Object oldTrait, Object newTrait,
                           IPosn duration) {
    IShape s = getShape(shapeName);
    IAction a;
    switch (at) {
      case MOVE:
        a = new Move(s, (IPosn) oldTrait, (IPosn) newTrait, duration);
        break;
      case COLOR_CHANGE:
        a = new ColorChange(s, (IMyColor) oldTrait, (IMyColor) newTrait, duration);
        break;
      default:
        a = new Scale(s, (IPosn) oldTrait,(IPosn) newTrait, duration);
    }
    s.addAction(a);
    if (!checkDuration(at, shapeName, duration)) {
      actions.add(a);
    }
    else {
      throw new IllegalArgumentException("This new action would conflict with existing ones");
    }
  }

  @Override
  public void executeAction(int actionIndex, double time) {
    IAction a = actions.get(actionIndex);
    if (time >= a.getDuration().getX() && time <= a.getDuration().getY()) {
      a.execute(time);
    }
  }

  @Override
  public String getDescription() {
    String s = "";
    s += "Shapes:\n";
    for (int i = 0; i < shapes.size(); i++) {
      if (i != 0) {
        s += "\n";
      }
      IShape curr = shapes.get(i);
      s += "Name: " + curr.getName() + "\n";
      s += "Type: " + curr.getType().toString().toLowerCase() + "\n";
      s += curr.getPosLocation() + ": " + "(" + curr.getX()
              + "," + curr.getY() + ")" + ", ";
      String[] wl = curr.getWLTypes();
      s += wl[0] + ": " + curr.getWidth() + ", "
              + wl[1] + ": " + curr.getHeight() + ", ";
      s += "Color: " + curr.getColor() + "\n";
      s += "Appears at t=" + curr.getAppear() + "\n";
      s += "Disappears at t=" + curr.getDisappear() + "\n";
    }

    for (int i = 0; i < actions.size(); i++) {
      s += "\n";
      IAction curr = actions.get(i);
      s += "Shape " + curr.getShapeName() + " ";
      String str = curr.getDescription();
      s += str;
      s += "from t=" + curr.getDuration().getX() + " to t=" + curr.getDuration().getY();
    }
    return s;
  }

  @Override
  public ArrayList<IShape> getShapes() {
    return this.shapes;
  }

  @Override
  public ArrayList<IAction> getActions() {
    return this.actions;
  }

  @Override
  public double getEndTime() {
    double result = 0;
    for (IShape s : shapes) {
      double t = s.getDisappear();
      if (t > result) {
        result = t;
      }
    }
    return result;
  }

  @Override
  public Builder builder() {
    return new Builder();
  }

  public static final class Builder implements TweenModelBuilder<IAnimatorOperations> {
    AnimatorModel am = new AnimatorModel();

    @Override
    public TweenModelBuilder<IAnimatorOperations> addOval(String name, float cx, float cy,
                                                          float xRadius, float yRadius, float red,
                                                          float green, float blue, int startOfLife,
                                                          int endOfLife) {
      am.createShape(ShapeType.OVAL, name, new Posn(cx, cy), new Posn(xRadius, yRadius), 4,
              new MyColor(red, green, blue),
              new Posn(startOfLife, endOfLife));
      return this;
    }

    @Override
    public TweenModelBuilder<IAnimatorOperations> addRectangle(String name, float lx, float ly,
                                                               float width, float height, float red,
                                                               float green, float blue,
                                                               int startOfLife, int endOfLife) {
      am.createShape(ShapeType.RECTANGLE, name, new Posn(lx, ly), new Posn(width, height), 4,
              new MyColor(red, green, blue),
              new Posn(startOfLife, endOfLife));
      return this;
    }

    @Override
    public TweenModelBuilder<IAnimatorOperations> addMove(String name, float moveFromX,
                                                          float moveFromY, float moveToX,
                                                          float moveToY, int startTime,
                                                          int endTime) {
      am.createAction(ActionType.MOVE, name, new Posn(moveFromX, moveFromY),
              new Posn(moveToX, moveToY), new Posn(startTime, endTime));
      return this;
    }

    @Override
    public TweenModelBuilder<IAnimatorOperations> addColorChange(String name, float oldR,
                                                                 float oldG, float oldB, float newR,
                                                                 float newG, float newB,
                                                                 int startTime, int endTime) {
      am.createAction(ActionType.COLOR_CHANGE, name,
              new MyColor(oldR, oldG, oldB),
              new MyColor(newR, newG, newB),
              new Posn(startTime, endTime));
      return this;
    }

    @Override
    public TweenModelBuilder<IAnimatorOperations> addScaleToChange(String name, float fromSx,
                                                                   float fromSy, float toSx,
                                                                   float toSy, int startTime,
                                                                   int endTime) {
      am.createAction(ActionType.SCALE, name, new Posn(fromSx, fromSy), new Posn(toSx, toSy),
              new Posn(startTime, endTime));
      return this;
    }

    @Override
    public IAnimatorOperations build() {
      return am;
    }
  }

  /**
   * Gets the shape with the given name.
   *
   * @param name  The name of the desired shape
   * @return      The desired shape
   */
  private IShape getShape(String name) {
    for (IShape s : shapes) {
      if (s.getName().equals(name)) {
        return s;
      }
    }
    throw new IllegalArgumentException("No shape found with that name.");
  }

  /**
   * Does the given duration conflict with any actions for the specified shape in the model?.
   *
   * @param at        The type of the actions to check
   * @param shapeName The name of the shape that could have conflicting actions.
   * @param duration  The duration that could potentially cause a conflict
   * @return          True if there is a conflict, false otherwise
   */
  private boolean checkDuration(ActionType at, String shapeName, IPosn duration) {
    for (IAction a : actions) {
      if (a.getType() == at && a.getShapeName().equals(shapeName)) {
        // If the start or end of the given duration is within the duration of the action in the
        // model
        if (duration.getX() > a.getDuration().getX() && duration.getX() < a.getDuration().getY()
                || duration.getX() > a.getDuration().getX() && duration.getX() < a.getDuration()
                .getY()) {
          return true;
        }
      }
    }
    return false;
  }
}