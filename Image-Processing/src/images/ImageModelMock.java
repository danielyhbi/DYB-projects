package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * This is a mock class for the model.
 */
public class ImageModelMock implements ImageModel {
  
  private StringBuilder log;
  
  /**
   * Constructor for this mock model.
   *
   * @param log incoming log
   */
  public ImageModelMock(StringBuilder log) {
    this.log = log;
  }
  
  /**
   * Load an image into the image model.
   *
   * @param filename the name of the file containing the image.
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong loading the image
   */
  @Override
  public void loadImage(String filename) throws IllegalArgumentException {
    log.append("[Model] load image from: " + filename + System.lineSeparator());
  }
  
  /**
   * Save the data in the image model to a file.
   *
   * @param filename the name of the file to save to
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong saving the file
   */
  @Override
  public void saveImage(String filename) throws IllegalArgumentException {
    if (isFilenameValid(filename)) {
      log.append("[Model] save image from: " + filename + System.lineSeparator());
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  /**
   * Apply the blur filter to the data in the image model.
   */
  @Override
  public void applyBlur() {
    log.append("[Model] apply blur" + System.lineSeparator());
  }
  
  /**
   * Apply the sharpen filter to the data in the image model.
   */
  @Override
  public void applySharpen() {
    log.append("[Model] apply sharpen" + System.lineSeparator());
  }
  
  /**
   * Apply the grayscale color transformation to the data in the image model.
   */
  @Override
  public void applyGrayscale() {
    log.append("[Model] apply grayscale" + System.lineSeparator());
  }
  
  /**
   * Apply the sepia color transformation to the data in the image model.
   */
  @Override
  public void applySepia() {
    log.append("[Model] apply sepia" + System.lineSeparator());
  }
  
  /**
   * Apply the dithering effect to the data in the image model.
   */
  @Override
  public void applyDither() {
    log.append("[Model] apply dither" + System.lineSeparator());
  }
  
  /**
   * Apply the mosaic effect to the data in the image model.
   *
   * @param seeds the number of seeds to use in the mosaic
   * @throws IllegalArgumentException if the number of seeds is not positive
   */
  @Override
  public void applyMosaic(int seeds) throws IllegalArgumentException {
    if (seeds > 0) {
      log.append("[Model] seed is larger than 0" + System.lineSeparator());
      log.append("[Model] apply mosaic with seed: " + seeds + System.lineSeparator());
    } else {
      log.append("[Model] seed is not a positive integer" + System.lineSeparator());
    }
  }
  
  /**
   * Apply edge detection through sobel operator. (Part of homework 10)
   *
   * @param applyBlur allow user to select whether to apply gaussian blur before edge detection
   */
  @Override
  public void applyEdgeDetection(boolean applyBlur) {
    
    if (applyBlur) {
      log.append("[Model] Apply edge detection with Gaussian blur." + System.lineSeparator());
    } else {
      log.append("[Model] Apply edge detection." + System.lineSeparator());
    }
    
  }
  
  /**
   * Get an image for the controller to update the view.
   *
   * @return a bufferImage. If there's no pixels, will return nothing.
   */
  @Override
  public BufferedImage getImage() {
    log.append("[Model] send out the current image" + System.lineSeparator());
    return null;
  }
  
  /**
   * Revert the image to its original state.
   */
  @Override
  public void revertImage() {
    log.append("[Model] Restore the image to the original condition." + System.lineSeparator());
  }
  
  /**
   * Generates a rainbow flag to a user's preference.
   *
   * @param filename     file name to be saved
   * @param isHorizontal directionality of the flag. false=vertical Flag
   * @param height       height of the image
   * @param width        width of the image
   */
  @Override
  public void generateRainbowFlag(String filename, boolean isHorizontal, int height, int width) {
    String direction = "";
    
    if (isHorizontal) {
      direction = "horizontal";
    } else {
      direction = "vertical";
    }
    
    log.append("[Model] Generated a rainbow flag: Direction: " + direction
            + " height: " + height + " width " + width + System.lineSeparator()
            + "[Model] File saved as " + filename + System.lineSeparator());
  }
  
  /**
   * Generate an image with check board pattern.
   *
   * @param filename     name of the file to be saved
   * @param colors       two colors chosen from users
   * @param checkerCount number of checkers per row
   * @param squareSize   size of the image
   */
  @Override
  public void generateCheckBoardPattern(String filename, int[][] colors, int checkerCount,
                                        int squareSize) {
    log.append("[Model] Generated check board pattern" + System.lineSeparator()
            + "Color " + Arrays.toString(colors[0]) + " and " + Arrays.toString(colors[1])
            + " checker count: " + checkerCount + " img size: " + squareSize
            + System.lineSeparator()
            + "[Model] File saved as " + filename + System.lineSeparator());
  }
  
  /**
   * Generates a flag of a chosen country.
   *
   * @param filename name of the file to be saved
   * @param country  country of flag
   * @param height   height of flag
   */
  @Override
  public void generateCountryFlag(String filename, String country, int height) {
    log.append("[Model] Generated flag of " + country + ". img height: " + height
            + System.lineSeparator()
            + "[Model] File saved as " + filename + System.lineSeparator());
  }
  
  /**
   * Enhance an grey scale image to intensify the contrast of an image.
   * Method is done through simple histogram equalization.
   */
  @Override
  public void applyGrayscaleContrastEnhance() {
    log.append("[Model] Apply Contrast Enhance." + System.lineSeparator());
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
    log.append("[Model] Crop image: from (" + topLeftXpt + ", " + topLeftYpt + ") to ("
            + btmRightXpt + ", " + btmRightYpt + ")" + System.lineSeparator());
  }
  
  @Override
  public String toString() {
    return log.toString();
  }
  
  // private helpers
  private boolean isFilenameValid(String filename) {
    File tempFile = new File(filename);
    try {
      tempFile.getCanonicalFile();
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
