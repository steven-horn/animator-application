package cs3500.animator.model.actions;

import cs3500.animator.model.shapes.IShape;
import cs3500.animator.model.misc.Posn;
import cs3500.animator.provider.model.AnimationOperations;
import cs3500.animator.provider.model.IScaleAnimation;
import cs3500.animator.provider.model.ShapeOperations;
import cs3500.animator.provider.view.visitors.AnimationVisitor;

/**
 * Represents a scaling applied to a shape in the animator.
 */
public class Scale implements IAction, IScaleAnimation {

  private IShape s;
  private Posn oldDimensions;
  private Posn newDimensions;
  private Posn duration;
  private final ActionType type;

  /**
   * Constructs a cs3500.animator.model.actions.Scale object.
   *
   * @param s             The shape to be scaled
   * @param oldDimensions The dimensions of the shape before the scaling
   * @param newDimensions The dimensions of the shape after the scaling
   * @param duration      The duration of this scaling
   */
  public Scale(IShape s, Posn oldDimensions, Posn newDimensions, Posn duration) {
    this.s = s;
    this.oldDimensions = oldDimensions;
    this.newDimensions = newDimensions;
    this.type = ActionType.SCALE;

    if (s.getAppear() <= duration.getX() && s.getDisappear() >= duration.getDoubleY()) {
      this.duration = duration;
    }
    else {
      throw new IllegalArgumentException("The duration of this move must be within the shape's "
              + "lifetime.");
    }
  }

  @Override
  public void execute(double t) {
    double ta = duration.getX();
    double tb = duration.getDoubleY();

    double a = oldDimensions.getX();
    double b = newDimensions.getX();
    double ftWidth = a * ((tb - t) / (tb - ta)) + b * ((t - ta) / (tb - ta));

    a = oldDimensions.getDoubleY();
    b = newDimensions.getDoubleY();
    int ftHeight = (int) (a * ((tb - t) / (tb - ta)) + b * ((t - ta) / (tb - ta)));

    s.setDimensions(new Posn(ftWidth, ftHeight));
  }

  @Override
  public Posn getDuration() {
    return this.duration;
  }

  @Override
  public ActionType getType() {
    return this.type;
  }

  @Override
  public String getShapeName() {
    return this.s.getName();
  }

  @Override
  public String getDescription() {
    String str = "";
    String[] wl = s.getWLTypes();
    str += "scales from " + wl[0] + ": " + oldDimensions.getX() + ", "
            + wl[1] + ": " + oldDimensions.getDoubleY() + " to ";
    str += wl[0] + ": " + newDimensions.getX() + ", "
            + wl[1] + ": " + newDimensions.getDoubleY() + " ";
    return str;
  }

  @Override
  public String getSVGDescription(double speed) {
    String str = "";
    if (this.oldDimensions.getIntX() != this.newDimensions.getIntX()) {
      str += "    <animate attributeType=\"xml\" begin=\""
              + (int) ((this.duration.getX() / speed) * 1000) + "ms\" dur=\""
              + (int) (((this.duration.getDoubleY() - this.duration.getX()) / speed) * 1000)
              + "ms\" attributeName=\"width\" from=\"" + this.oldDimensions.getIntX() + "\" to=\""
              + this.newDimensions.getIntX() + "\" fill=\"freeze\" />\n";
    }
    if (this.oldDimensions.getIntY() != this.newDimensions.getIntY()) {
      str += "    <animate attributeType=\"xml\" begin=\""
              + (int) ((this.duration.getX() / speed) * 1000) + "ms\" dur=\""
              + (int) (((this.duration.getDoubleY() - this.duration.getX()) / speed) * 1000)
              + "ms\" attributeName=\"height\" from=\"" + this.oldDimensions.getIntY() + "\" to=\""
              + this.newDimensions.getIntY() + "\" fill=\"freeze\" />\n";
    }

    return str;
  }

  //================================================================================================

  @Override
  public void animate(ShapeOperations shapeToBeChanged, float currentTime) {
    s = (IShape) shapeToBeChanged;
    if (currentTime >= getDuration().getDoubleX() && currentTime <= getDuration().getDoubleY()) {
      execute(currentTime);
    }
  }

  @Override
  public float getFromTime() {
    return (float) duration.getX();
  }

  @Override
  public float getToTime() {
    return (float) duration.getDoubleY();
  }

  @Override
  public <T> T accept(AnimationVisitor<T> v) {
    return v.visit(this);
  }

  @Override
  public Posn getFrom() {
    return oldDimensions;
  }

  @Override
  public Posn getTo() {
    return newDimensions;
  }

  @Override
  public boolean hasNoConflictsWith(ShapeOperations stateAtFromTime, ShapeOperations stateAtToTime,
                                    AnimationOperations a) {
    // unsupported
    return false;
  }

  @Override
  public boolean noConflictsWithHelper(ShapeOperations givenFrom, ShapeOperations givenTo,
                                       float givenFromTime, float givenToTime) {
    // unsupported
    return false;
  }

  @Override
  public void setShapeToBeAnimated(String shapeName) {
    s.setName(shapeName);
  }
}
