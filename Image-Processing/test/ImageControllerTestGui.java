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
 * This class is specifically for the GUI
 */
public class ImageControllerTestGui {
  
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
   * Testing the GUI part of the mosaic method.
   */
  @Test
  public void testMosaicTypical() {
    
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applyMosaic("1600");
    
    String expectedModel = "[Model] seed is larger than 0" + System.lineSeparator()
            + "[Model] apply mosaic with seed: 1600" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Processing will take a while. Click ok to continue."
            + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * Testing the GUI part of the mosaic method with invalid input.
   */
  @Test
  public void testMosaicBadSeeds() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applyMosaic("abcde12");
    
    String expectedModel = "";
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Processing will take a while. Click ok to continue."
            + System.lineSeparator()
            + "[View] Display Message: java.lang.NumberFormatException: "
            + "For input string: \"abcde12\"";
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the blur method.
   */
  @Test
  public void testBlur() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applyBlur();
    
    String expectedModel = "[Model] apply blur" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the shapren method.
   */
  @Test
  public void testSharpen() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applySharpen();
    
    String expectedModel = "[Model] apply sharpen" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the grayscale method.
   */
  @Test
  public void testGrayScale() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applyGrayScale();
    
    String expectedModel = "[Model] apply grayscale" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the sepia method.
   */
  @Test
  public void testSepia() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applySepia();
    
    String expectedModel = "[Model] apply sepia" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the dither method.
   */
  @Test
  public void testDither() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.applyDither();
    
    String expectedModel = "[Model] apply dither" + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the getImage method.
   */
  @Test
  public void testCropImage() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.cropImage(1111, 2222, 3333, 4444);
    
    String expectedModel = "[Model] Crop image: from (1111, 2222) to (3333, 4444)"
            + System.lineSeparator() + "[Model] send out the current image"
            + System.lineSeparator();
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view."
            + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
    
  }
  
  @Test
  public void testCropImageInvalid() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.cropImage(-1111, 2222, 3333, 4444);
    
    String expectedModel = "";
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Invalid Input, try cropping again."
            + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testContrastEnhance() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.applyContrastEnhancement();
    
    String expectedModel = "[Model] Apply Contrast Enhance." + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testRevertImage() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.revertImage();
    
    String expectedModel = "[Model] Restore the image to the original condition."
            + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testEdgeDetection() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.applyEdgeDetection();
    
    String expectedModel = "[Model] Apply edge detection." + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testRainbowFlag() {
    // NOTE: View will check the input validity before sending it to the controller
    
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    controller.generateRainbowFlag("exampleFileName.png", 200, 100, false);
    
    String expectedModel =
            "[Model] Generated a rainbow flag: Direction: vertical height: 100 width 200"
                    + System.lineSeparator()
                    + "[Model] File saved as exampleFileName.png" + System.lineSeparator();
    
    assertEquals(expectedModel, model.toString());
    
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Image saved as exampleFileName.png";
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testCheckBoard() {
    // NOTE: View will check the input validity before sending it to the controller
  
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
  
    controller.generateCheckerBoard("exampleFileName.png",
            new int[][] {{255, 255, 0}, {0, 0, 0}}, 10, 500);
  
    String expectedModel = "[Model] Generated check board pattern" + System.lineSeparator()
                    + "Color [255, 255, 0] and [0, 0, 0] checker count: 10 img size: 500"
                    + System.lineSeparator()
                    + "[Model] File saved as exampleFileName.png" + System.lineSeparator();
  
    assertEquals(expectedModel, model.toString());
  
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Image saved as exampleFileName.png";
    assertEquals(expectedView, view.toString());
  }
  
  @Test
  public void testCountryFlag() {
    // NOTE: View will check the input validity before sending it to the controller
  
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
  
    controller.generateCountryFlag("exampleFileName.png", "A COUNTRY", 500);
  
    String expectedModel = "[Model] Generated flag of A COUNTRY. img height: 500"
                    + System.lineSeparator()
                    + "[Model] File saved as exampleFileName.png" + System.lineSeparator();
  
    assertEquals(expectedModel, model.toString());
  
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: Image saved as exampleFileName.png";
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the open files method.
   */
  @Test
  public void testOpenFiles() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.openFiles("a file path that doesn't matter");
    
    String expectedModel = "[Model] load image from: a file path that doesn't matter"
            + System.lineSeparator()
            + "[Model] send out the current image" + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Image updated" + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the open files method but with invalid input.
   */
  @Test
  public void testOpenFilesInvalid() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.openFiles(null);
    
    String expectedModel = "";
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: There's no image to display." + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the save files method but with invalid input.
   */
  @Test
  public void testSaveFiles() {
    // create a controller that utilize both view and controller
    ImageControllerGui controller = new ImageControllerGui(model);
    controller.setView(view);
    
    // pass control to the controller
    controller.saveFiles("a file path that doesn't matter");
    
    String expectedModel = "[Model] save image from: a file path that doesn't matter"
            + System.lineSeparator();
    assertEquals(expectedModel, model.toString());
    // check the output of the view is as expected
    String expectedView = "[View] Give feature callbacks to the view." + System.lineSeparator()
            + "[View] Display Message: File saved. Path: a file path that doesn't matter"
            + System.lineSeparator();
    assertEquals(expectedView, view.toString());
  }
  
  /**
   * This Junit test out the save files method but with invalid input.
   */
  @Test
  public void testSaveFilesInvalid() {
    /*
    GUI with JFileChooser will always return a valid file path. Test dismissed.
     */
  }
  
  
}