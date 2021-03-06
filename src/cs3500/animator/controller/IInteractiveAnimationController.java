package cs3500.animator.controller;

import cs3500.animator.model.shapes.IShape;

/**
 * Outlines the methods for a Controller used with the Interactive view.
 */
public interface IInteractiveAnimationController extends IAnimationController {

  /**
   * A listener has notified the controller that the user has toggled pause:
   * Relay this information to the view.
   */
  void pauseToggled();

  /**
   * A listener has notified the controller that the user has toggled looping:
   * Relay this information to the view.
   */
  void loopToggled();

  /**
   * A listener has notified the controller that the export button has been pushed:
   * Relay this information to the view.
   * @param ofile   The file to send the svg output to.
   */
  void export(String ofile);

  /**
   * A listener has notified the controller that the user has changed the speed:
   * Relay this information to the view.
   * @param newSpeed  The new speed that the user requested.
   */
  void speedChanged(int newSpeed);

  /**
   * A listener has notified the controller that the user has selected a shape from the drop-down
   * menu that removes shapes from the animation:
   * Relay this information to the view.
   *
   * @param s The shape that was selected
   */
  void shapeToggled(IShape s);
}
