package script;

import java.io.IOException;

/**
 * This is a concrete implementation of the ImageView interface.
 */
public class ImageViewConc implements ImageView {
  
  private final Appendable out;
  
  /**
   * Constructor to create an instance of the new view.
   *
   * @param out output for message or logs.
   */
  public ImageViewConc(Appendable out) {
    this.out = out;
  }
  
  /**
   * Show a message to the user.
   *
   * @param msg the message to show.
   */
  @Override
  public void showMessage(String msg) throws IllegalArgumentException {
    try {
      out.append(msg);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid Message.");
    }
  }
  
}
