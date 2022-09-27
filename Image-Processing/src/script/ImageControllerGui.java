package script;

import images.ImageModel;
import imageview.Features;
import imageview.View;

/**
 * The controller will implement all the features in the Feature interface.
 */
public class ImageControllerGui implements Features {
  private ImageModel model;
  private View view;
  
  /**
   * Constructor to assign a model.
   *
   * @param m model to be input.
   */
  public ImageControllerGui(ImageModel m) {
    this.model = m;
  }
  
  /**
   * Set up the view from the GUI.
   *
   * @param v an instance of the view
   */
  public void setView(View v) {
    this.view = v;
    // give feature callbacks to the view.
    this.view.setFeatures(this);
  }
  
  /**
   * Exit the program.
   */
  @Override
  public void exitProgram() {
    System.exit(0);
  }
  
  /**
   * Open file. Will use JFileChooser.
   */
  @Override
  public void openFiles(String filePath) {
    if (filePath != null) {
      model.loadImage(filePath);
      view.updateImage(model.getImage());
    } else {
      view.displayMessage("There's no image to display." + System.lineSeparator());
    }
  }
  
  /**
   * Save a file.
   */
  @Override
  public void saveFiles(String filePath) {
    try {
      model.saveImage(filePath);
      view.displayMessage("File saved. Path: " + filePath + System.lineSeparator());
    } catch (IllegalArgumentException e) {
      view.displayMessage(e.toString());
    }
  }
  
  
  /**
   * Controller integrated from homework 8: Script processing.
   * <p>
   * Apply all the filters from script. User shall run this after
   * inputting stuff from the textField. A message box should pop up
   * displaying any error messages.
   * <p>
   * Applying filters will not update the image preview
   */
  @Override
  public void applyFilterScript(String[] input) {
    
    boolean fileSelected = false;
    boolean isError = false;
    
    for (int i = 0; i < input.length; i++) {
      
      String[] tokens = input[i].split(" ");
      
      // Process Image
      switch (tokens[0].toUpperCase()) {
        case "LOAD":
          // check the file path
          try {
            String test = tokens[1];
          } catch (ArrayIndexOutOfBoundsException e) {
            view.displayMessage("Error on line " + (i + 1) + ". "
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
            view.displayMessage("Error on line " + (i + 1) + ". "
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
              view.displayMessage("Error on line " + (i + 1)
                      + e.getMessage() + System.lineSeparator());
              isError = true;
            }
            
            view.updateImage(model.getImage());
          } else {
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "BLUR":
          if (fileSelected) {
            // ask the model to apply blur filter
            model.applyBlur();
          } else {
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "SHARPEN":
          if (fileSelected) {
            // ask the model to apply a sharpen filter
            model.applySharpen();
          } else {
            view.displayMessage("Error on line " + (i + 1)
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
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "SEPIA":
          if (fileSelected) {
            // ask the model to apply a grayscale filter
            model.applySepia();
          } else {
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "DITHER":
          if (fileSelected) {
            // ask the model to apply a grayscale filter
            model.applyDither();
          } else {
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "MOSAIC":
          if (fileSelected) {
            
            int seedCount = 0;
            // make sure the input is an integer
            try {
              seedCount = Integer.parseInt(tokens[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
              view.displayMessage("Error on line " + (i + 1)
                      + ". Seed count must be a positive integer"
                      + System.lineSeparator());
              isError = true;
            }
            
            // make sure to catch any exceptions from the model
            try {
              // ask the model to apply a grayscale filter
              model.applyMosaic(seedCount);
            } catch (IllegalArgumentException e) {
              view.displayMessage("Error on line " + (i + 1)
                      + e.getMessage() + System.lineSeparator());
              isError = true;
            }
          } else {
            view.displayMessage("Error on line " + (i + 1)
                    + ". No file loaded." + System.lineSeparator());
            isError = true;
          }
          break;
        case "":
          break;
        default:
          view.displayMessage("Invalid command on line " + (i + 1)
                  + ". Input: " + tokens[0] + System.lineSeparator());
          isError = true;
      }
    }
    
    view.displayMessage("Script running complete!" + System.lineSeparator());
  }
  
  /**
   * Apply a mosaic filter. Will prompt user to enter the seed number. Maybe.
   */
  @Override
  public void applyMosaic(String seeds) {
    try {
      model.applyMosaic(Integer.parseInt(seeds));
      view.updateImage(model.getImage());
    } catch (IllegalArgumentException e) {
      view.displayMessage(e.toString());
    }
  }
  
  /**
   * Apply a blur filter.
   */
  @Override
  public void applyBlur() {
    model.applyBlur();
    view.updateImage(model.getImage());
  }
  
  /**
   * Apply a sharpen filter.
   */
  @Override
  public void applySharpen() {
    model.applySharpen();
    view.updateImage(model.getImage());
  }
  
  /**
   * Apply a dither filter.
   */
  @Override
  public void applyDither() {
    model.applyDither();
    view.updateImage(model.getImage());
  }
  
  /**
   * Apple a sepia filter.
   */
  @Override
  public void applySepia() {
    model.applySepia();
    view.updateImage(model.getImage());
  }
  
  /**
   * Apply a grayscale filter.
   */
  @Override
  public void applyGrayScale() {
    model.applyGrayscale();
    view.updateImage(model.getImage());
  }
  
  /**
   * display readme.
   */
  @Override
  public void readMe() {
  
  }
  
  @Override
  public void revertImage() {
    model.revertImage();
    view.updateImage(model.getImage());
  }
  
  /**
   * Crops the image.
   *
   * @param topLeftXpt  top left x point to start crop
   * @param topLeftYpt  top left y point to start crop
   * @param btmRightXpt top right x point to start crop
   * @param btmRightYpt top right y point to start crop
   */
  @Override
  public void cropImage(int topLeftXpt, int topLeftYpt, int btmRightXpt, int btmRightYpt) {
    
    if (topLeftXpt <= 0 || topLeftYpt <= 0
            || btmRightXpt < topLeftXpt || btmRightYpt < topLeftYpt) {
      view.displayMessage("Invalid Input, try cropping again." + System.lineSeparator());
    } else {
      model.cropImage(topLeftXpt, topLeftYpt, btmRightXpt, btmRightYpt);
      view.updateImage(model.getImage());
    }
  }
  
  /**
   * Apply sobel edge detection.
   */
  @Override
  public void applyEdgeDetection() {
    model.applyEdgeDetection(false);
    view.updateImage(model.getImage());
  }
  
  /**
   * Apply contrast enhancement.
   */
  @Override
  public void applyContrastEnhancement() {
    model.applyGrayscaleContrastEnhance();
    view.updateImage(model.getImage());
  }
  
  @Override
  public void rotateImageCounterClockwise() {
    model.rotateImageCounterClockwise();
    view.updateImage(model.getImage());
  }
  
  @Override
  public void rotateImageClockwise() {
    model.rotateImageClockwise();
    view.updateImage(model.getImage());
  }
  
  @Override
  public void flipImageHorizontal() {
    model.flipImageHorizontal();
    view.updateImage(model.getImage());
  }
  
  @Override
  public void flipImageVertical() {
    model.flipImageVertical();
    view.updateImage(model.getImage());
  }
  
  /**
   * Generate flag of a country.
   *
   * @param fileName    fileName to be saved
   * @param countryName Name of the country
   * @param height      Height of the image
   */
  @Override
  public void generateCountryFlag(String fileName, String countryName, int height) {
    try {
      model.generateCountryFlag(fileName, countryName.toUpperCase(), height);
      view.displayMessage("Image saved as " + fileName);
    } catch (IllegalArgumentException e) {
      view.displayMessage("Something wrong happened. " + e.getMessage());
    }
  }
  
  /**
   * Generate rainbow flag based on the input height.
   *
   * @param fileName   file name to be saved
   * @param width      width of image
   * @param height     height of the image
   * @param isVertical is the image vertical
   */
  @Override
  public void generateRainbowFlag(String fileName, int width, int height, boolean isVertical) {
    try {
      model.generateRainbowFlag(fileName, isVertical, height, width);
      view.displayMessage("Image saved as " + fileName);
    } catch (IllegalArgumentException e) {
      view.displayMessage("Something wrong happened. " + e.getMessage());
    }
  }
  
  /**
   * Generate an image with check board pattern.
   *
   * @param fileName     name of the file to be saved
   * @param colorsRgb    two colors chosen from users
   * @param checkerCount number of checkers per row
   * @param squareSize   size of the image
   */
  @Override
  public void generateCheckerBoard(String fileName,
                                   int[][] colorsRgb, int checkerCount, int squareSize) {
    try {
      model.generateCheckBoardPattern(fileName, colorsRgb, checkerCount, squareSize);
      view.displayMessage("Image saved as " + fileName);
    } catch (IllegalArgumentException e) {
      view.displayMessage("Something wrong happened. " + e.getMessage());
    }
  }
}
