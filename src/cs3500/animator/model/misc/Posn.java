package cs3500.animator.model.misc;

/**
 * Represents a position (x,y).
 * Can also be used to represent other pairs of numbers such as shape dimensions and time durations.
 */
public class Posn implements IPosn {
  private double x;
  private double y;

  /**
   * Constructs a cs3500.animator.model.misc.Posn.
   *
   * @param x The Posn's x-coordinate (or first number)
   * @param y The Posn's y-coordinate (or second number)
   */
  public Posn(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void move(IPosn changeInLocation) {
    this.x += changeInLocation.getX();
    this.y += changeInLocation.getDoubleY();
  }

  @Override
  public double getDoubleX() {
    return this.x;
  }

  @Override
  public double getDoubleY() {
    return this.y;
  }

  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  }

  @Override
  public int getIntX() {
    return (int) this.x;
  }

  @Override
  public int getIntY() {
    return (int) this.y;
  }

  @Override
  public float getX() {
    return (float) this.x;
  }

  @Override
  public float getY() {
    return (float) this.y;
  }
}
