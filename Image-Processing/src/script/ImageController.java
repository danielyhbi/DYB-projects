package script;

import images.ImageModel;

/**
 * Represents a Controller for ImageModel: handle user commands by executing them
 * using the model; For example, read/save file, apply filters, etc.
 */
public interface ImageController {
  
  /**
   * This method execute a list of action for image processing.
   *
   * @param view The view of the MVC system.
   * @param model The model of the MVC system.
   */
  void processImage(ImageView view, ImageModel model);
}
