package cs3500.animator.provider.view;

import cs3500.animator.provider.model.AnimationOperations;
import cs3500.animator.provider.model.ShapeOperations;
import cs3500.animator.provider.view.model.AnimationViewOperations;
import cs3500.animator.provider.view.model.AnimatorViewOperations;
import cs3500.animator.provider.view.model.ShapeViewOperations;
import cs3500.animator.provider.view.visitors.AnimationVisitor;
import cs3500.animator.provider.view.visitors.ShapeVisitor;

import java.util.ArrayList;

public abstract class AbstractView implements AnimatorViewOperations {

  /**
   * int tempo - Represents the rate this view is playing an animation at.
   * state - Represents the animation as an array list.
   */
  protected int tempo;
  protected ArrayList<ShapeViewOperations> state;
  protected final ShapeVisitor<ShapeViewOperations> sv;
  protected final AnimationVisitor<AnimationViewOperations> av;

  protected AbstractView(ShapeVisitor<ShapeViewOperations> sv,
                         AnimationVisitor<AnimationViewOperations> av) {
    this.sv = sv;
    this.av = av;
  }

  /**
   * Used to initalize the given state of ShapeOperations into a state of ShapeViewOperations that
   * we can view.
   * @param modelState - the state we are given to view, presumably from the model.
   */
  protected void initializeState(ArrayList<ShapeOperations> modelState) {
    this.state = new ArrayList<>();
    for (ShapeOperations s : modelState) {
      this.state.add(s.accept(sv));
    }

    for (int i = 0; i < modelState.size() ; i++) {
      for (AnimationOperations a : modelState.get(i).getAnimations()) {
        this.state.get(i).addAnimation(a.accept(av));
      }
    }

  }

}
