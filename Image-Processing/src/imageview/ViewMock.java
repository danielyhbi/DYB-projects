package imageview;

import java.awt.image.BufferedImage;

/**
 * This is a mock view for the view.
 */
public class ViewMock implements View {
  
  private StringBuilder log;
  
  /**
   * Constructor for this mock view.
   *
   * @param log the log to use
   */
  public ViewMock(StringBuilder log) {
    this.log = log;
  }
  
  /**
   * Obtain a set of feature callbacks for view to use.
   *
   * @param f set of features in the Feature class.
   */
  @Override
  public void setFeatures(Features f) {
    log.append("[View] Give feature callbacks to the view." + System.lineSeparator());
  }
  
  /**
   * Update the processed imaged to the view.
   *
   * @param newImg incoming bufferImage from the model
   */
  @Override
  public void updateImage(BufferedImage newImg) {
    log.append("[View] Image updated" + System.lineSeparator());
  }
  
  /**
   * Output messages from model or controller to the user.
   *
   * @param msg a string of message
   */
  @Override
  public void displayMessage(String msg) {
    log.append("[View] Display Message: " + msg);
  }
  
  @Override
  public String toString() {
    return log.toString();
  }
}
