package script;

import images.ConcreteImageModel;
import images.ImageModel;

/**
 * This is the driver for Image Processing.
 */
public class ImageProcessDriver {
  
  /**
   * Runs image processing on a console.
   *
   * @param args input argument.
   */
  public static void main(String[] args) {
    // create a model we will use
    ImageModel model = new ConcreteImageModel();
    
    // create a view that uses that console for output
    ImageView view = new ImageViewConc(System.out);
    
    // create a controller that utilize both view and controller
    ImageController controller = new ImageControllerConc(args[0]);
    
    // pass control to the controller
    controller.processImage(view, model);
  }
}
