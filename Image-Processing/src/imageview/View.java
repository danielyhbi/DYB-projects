package imageview;

import java.awt.image.BufferedImage;

/**
 * This is the public interface for the view.
 */
public interface View {
  
  /**
   * Obtain a set of feature callbacks for view to use.
   *
   * @param f set of features in the Feature class.
   */
  void setFeatures(Features f);
  
  /**
   * Update the processed imaged to the view.
   *
   * @param newImg incoming bufferImage from the model
   */
  void updateImage(BufferedImage newImg);
  
  /**
   * Output messages from model or controller to the user.
   *
   * @param msg a string of message
   */
  void displayMessage(String msg);
}
