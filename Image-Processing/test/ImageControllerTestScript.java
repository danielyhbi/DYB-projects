import static org.junit.Assert.assertEquals;

import images.ImageModel;
import images.ImageModelMock;
import imageview.View;
import imageview.ViewMock;
import org.junit.Before;
import org.junit.Test;
import script.ImageControllerGui;

/**
 * Test class for the Image Process Controller.
 * This class is specifically used for script testing, namely the
 * "applyFilterScript" method in the controller. Very similar to homework 8.
 */
public class ImageControllerTestScript {
  
  StringBuilder viewLog;
  StringBuilder processLog;
  String expectedView;
  String expectedModel;
  ImageModel model;
  View view;
  
  /**
   * This is to set up a few variables.
   */
  @Before
  public void setup() {
    // set up all of the logs
    StringBuilder viewLog = new StringBuilder();
    StringBuilder processLog = new StringBuilder();
    
    model = new ImageModelMock(processLog);
    view = new ViewMock(viewLog);
  }
  
  /**
   * This is a test for a typical script run.
   * I copied and pasted the code from homework 8, since that code has been
   * well tested in homework 8. I will not test here further other than demonstrating
   * it works here.
   * Note this script run has been tested well in homework 8.
   */
  @Test
  public void testTypical() {
    
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load ny-manhattan-bridge.png", "blur", "blur",
      "save ny-manhattan-bridge-blurred-2.png", "sepia",
      "save ny-manhattan-bridge-blurred-sepia.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "[Model] load image from: ny-manhattan-bridge.png"
            + System.lineSeparator()
            + "[Model] apply blur" + System.lineSeparator()
            + "[Model] apply blur" + System.lineSeparator()
            + "[Model] save image from: ny-manhattan-bridge-blurred-2.png" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator()
            + "[Model] apply sepia" + System.lineSeparator()
            + "[Model] save image from: ny-manhattan-bridge-blurred-sepia.png"
            + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testNoFileLoaded() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"blur", "blur",
      "save ny-manhattan-bridge-blurred-2.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "";
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Error on line 1. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Error on line 2. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Error on line 3. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out any invalid command and typos.
   */
  @Test
  public void testInvalidCommand() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load ny-manhattan-bridge.png", "sharrpen",
      "save ny-manhattan-bridge-blurred-2.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "[Model] load image from: ny-manhattan-bridge.png"
            + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Invalid command on line 2. Input: sharrpen"
            + System.lineSeparator()
            + "[View] Display Message: Error on line 3. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This jUnit tests out blank file name. Note the validity of the file name is handled in the
   * model, which the controller would catch an exception. Since it's not within the scope of the
   * controller, invalid file name was not tested.
   */
  @Test
  public void testBadFileName() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load", "blur", "save"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "";
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Error on line 1. Index 1 out of bounds for length 1"
            + System.lineSeparator()
            + "[View] Display Message: Error on line 2. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Error on line 3. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out if the controller will handle if the script saves a file before lading.
   */
  @Test
  public void testSaveFirst() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"save ny-manhattan-bridge-blurred-2.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "";
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Error on line 1. No file loaded."
            + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out if the controller will handle if no input of seed is given.
   */
  @Test
  public void testNoSeeds() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load ny-manhattan-bridge.png", "mosaic",
      "save ny-manhattan-bridge-blurred-2.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "[Model] load image from: ny-manhattan-bridge.png"
            + System.lineSeparator()
            + "[Model] seed is not a positive integer" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView
            = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Error on line 2. Seed count must be a positive integer"
            + System.lineSeparator()
            + "[View] Display Message: Error on line 3. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out if the controller will handle if bad seed is given. Validity of the seeds
   * that comes with integer is further tested in the model. Out of scope of this controller test.
   */
  @Test
  public void testBadSeeds() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load ny-manhattan-bridge.png", "mosaic abc",
      "save ny-manhattan-bridge-blurred-2.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "[Model] load image from: ny-manhattan-bridge.png"
            + System.lineSeparator()
            + "[Model] seed is not a positive integer" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView
            = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Error on line 2. Seed count must be a positive integer"
            + System.lineSeparator()
            + "[View] Display Message: Error on line 3. No file loaded." + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * Test if the controller will skip an empty line.
   */
  @Test
  public void testSpaceInMiddle() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    String[] instruction = new String[]{"load ny-manhattan-bridge.png", "blur", "", "blur",
      "save ny-manhattan-bridge-blurred-2.png", "sepia",
      "save ny-manhattan-bridge-blurred-sepia.png"};
    
    // pass control to the controller
    controller.applyFilterScript(instruction);
    
    String expectedModel = "[Model] load image from: ny-manhattan-bridge.png"
            + System.lineSeparator()
            + "[Model] apply blur" + System.lineSeparator()
            + "[Model] apply blur" + System.lineSeparator()
            + "[Model] save image from: ny-manhattan-bridge-blurred-2.png" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator()
            + "[Model] apply sepia" + System.lineSeparator()
            + "[Model] save image from: ny-manhattan-bridge-blurred-sepia.png"
            + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator()
            + "[View] Display Message: Script running complete!" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
}
