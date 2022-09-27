package imageview;

import images.ConcreteImageModel;
import images.ImageModel;
import script.ImageControllerGui;

/**
 * Public class for the view driver.
 */
public class MainViewDriver {
  
  /**
   * Main method for the view driver.
   * @param args  input arguments. not used.
   */
  public static void main(String[] args) {
    // create model
    ImageModel modelMain = new ConcreteImageModel();
    // create controller
    ImageControllerGui controller = new ImageControllerGui(modelMain);
    // create the view
    View view = new ViewMainFrame();
    // set th view in the controller
    controller.setView(view);
  }
}
