package script;

import images.ImageModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is the concrete implementation of the controller class.
 */
public class ImageControllerConc implements ImageController {
  
  private File file;
  
  /**
   * Constructor of the image processing controller.
   *
   * @param argument input of things from the driver.
   */
  public ImageControllerConc(String argument) {
    
    this.file = new File(argument);
    
  }
  
  /**
   * This method execute a list of action for image processing.
   *
   * @param view  The view of the MVC system.
   * @param model The model of the MVC system.
   */
  @Override
  public void processImage(ImageView view, ImageModel model) {
    
    // do a loop to get all of the input into a string array
    // Process the input file
    // pre-assign a fileReader
    Readable fileReader = null;
    try {
      fileReader = new FileReader(this.file);
    } catch (FileNotFoundException e) {
      // tell view to display an error message
      view.showMessage("File not found.");
      // due to the scope of this assignment. Exit program so user can input something else.
      System.exit(0);
    }
    
    // Process the script. Convert them into an array of strings
    Scanner script = new Scanner(fileReader);
    List<String> steps = processScript(script);
    
    // Process Image
    boolean fileSelected = false;
    boolean isError = false;
    
    for (int i = 0; i < steps.size(); i++) {
      
      // break up the input per line into tokens
      String[] tokens = steps.get(i).split(" ");
      
      switch (tokens[0].toUpperCase()) {
        case "LOAD":
          try {
            String test = tokens[1];
          } catch (ArrayIndexOutOfBoundsException e) {
            view.showMessage("Error on line " + (i + 1) + ". "
                    + e.getMessage() + System.lineSeparator());
            isError = true;
            break;
          }
          
          try {
            // ask the model to load the image
            model.loadImage(tokens[1]);
            // turn on file selected
            fileSelected = true;
          } catch (IllegalArgumentException e) {
            view.showMessage("Error on line " + (i + 1) + ". "
                    + e.getMessage() + System.lineSeparator());
            isError = true;
          }
          break;
        case "SAVE":
          if (fileSelected && !isError) {
            // ask the model to save the file
            try {
              model.saveImage(tokens[1]);
            } catch (IllegalArgumentException e) {
              view.showMessage("Error on line " + (i + 1)
                      + e.getMessage() + System.lineSeparator());
              isError = true;
            }
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "BLUR":
          if (fileSelected) {
            // ask the model to apply blur filter
            model.applyBlur();
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "SHARPEN":
          if (fileSelected) {
            // ask the model to apply a sharpen filter
            model.applySharpen();
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "GRAYSCALE":
        case "GREYSCALE":
          if (fileSelected) {
            // ask the model to apply a grayscale filter
            model.applyGrayscale();
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "SEPIA":
          if (fileSelected) {
            // ask the model to apply a grayscale filter
            model.applySepia();
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "DITHER":
          if (fileSelected) {
            // ask the model to apply a grayscale filter
            model.applyDither();
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "MOSAIC":
          if (fileSelected) {
            
            int seedCount = 0;
            // make sure the input is an integer
            try {
              seedCount = checkSeed(tokens[1]);
            } catch (NumberFormatException e) {
              view.showMessage("Error on line " + (i + 1)
                      + ". Seed count must be a positive integer"
                      + System.lineSeparator());
              isError = true;
            }
            
            // make sure to catch any exceptions from the model
            try {
              // ask the model to apply a grayscale filter
              model.applyMosaic(seedCount);
            } catch (IllegalArgumentException e) {
              view.showMessage("Error on line " + (i + 1)
                      + e.getMessage() + System.lineSeparator());
              isError = true;
            }
          } else {
            view.showMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "":
          break;
        default:
          view.showMessage("Invalid command on line " + (i + 1)
                  + ". Input: " + tokens[0] + System.lineSeparator());
          isError = true;
      }
    }
  }
  
  // Private method to process the scanner input. Conver them into a list of string
  private List<String> processScript(Scanner script) {
    
    List<String> output = new ArrayList<>();
    
    while (script.hasNext()) {
      output.add(script.nextLine());
    }
    
    return output;
  }
  
  // this provides a quick check
  private int checkSeed(String seed) {
    return Integer.parseInt(seed);
  }
  
  
  // 1. the scripe has to begin with "load" to read file
  // 2. then you can run through filters, note each of the filter are additive and can not undo
  // 3. before moving on to the next image, you have to save the current image
  // would be nice to notify user which line was messed up
  
  // honestly just output a bunch of message while processing an image
}
