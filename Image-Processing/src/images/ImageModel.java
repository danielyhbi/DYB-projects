package images;

import java.awt.image.BufferedImage;

/**
 * The interface for ImageModel.
 */
public interface ImageModel {

  /**
   * Load an image into the image model.
   * 
   * @param filename the name of the file containing the image.
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong loading the image
   */
  public void loadImage(String filename) throws IllegalArgumentException;

  /**
   * Save the data in the image model to a file.
   * 
   * @param filename the name of the file to save to
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong saving the file
   */
  public void saveImage(String filename) throws IllegalArgumentException;

  /**
   * Apply the blur filter to the data in the image model.
   */
  public void applyBlur();

  /**
   * Apply the sharpen filter to the data in the image model.
   */
  public void applySharpen();

  /**
   * Apply the grayscale color transformation to the data in the image model.
   */
  public void applyGrayscale();

  /**
   * Apply the sepia color transformation to the data in the image model.
   */
  public void applySepia();

  /**
   * Apply the dithering effect to the data in the image model.
   */
  public void applyDither();

  /**
   * Apply the mosaic effect to the data in the image model.
   * 
   * @param seeds the number of seeds to use in the mosaic
   * @throws IllegalArgumentException if the number of seeds is not positive
   */
  public void applyMosaic(int seeds) throws IllegalArgumentException;
  
  /**
   * Apply edge detection through sobel operator. (Part of homework 10)
   * @param applyBlur allow user to select whether to apply gaussian blur before edge detection
   */
  public void applyEdgeDetection(boolean applyBlur);
  
  /**
   * Rotates image counterclockwise 90 degree.
   */
  void rotateImageCounterClockwise();
  
  /**
   * Rotates image clockwise 90 degree.
   */
  void rotateImageClockwise();
  
  /**
   * Flips image horizontally.
   */
  void flipImageHorizontal();
  
  /**
   * Flips image vertically.
   */
  void flipImageVertical();
  
  /**
   * Get an image for the controller to update the view.
   * @return a bufferImage. If there's no pixels, will return nothing.
   */
  public BufferedImage getImage();
  
  /**
   * Revert the image to its original state.
   */
  void revertImage();
  
  /**
   * Generates a rainbow flag to a user's preference.
   *
   * @param isHorizontal directionality of the flag. false=vertical Flag
   * @param height       height of the image
   * @param width        width of the image
   */
  void generateRainbowFlag(String filename, boolean isHorizontal, int height, int width);
  
  /**
   * Generate an image with check board pattern.
   *
   * @param filename  name of the file to be saved
   * @param colors  two colors chosen from users
   * @param checkerCount  number of checkers per row
   * @param squareSize  size of the image
   */
  void generateCheckBoardPattern(String filename, int[][] colors, int checkerCount, int squareSize);
  
  /**
   * Generates a flag of a chosen country.
   *
   * @param filename name of the file to be saved
   * @param country country of flag
   * @param height  height of flag
   */
  void generateCountryFlag(String filename, String country, int height);
  
  /**
   * Enhance an grey scale image to intensify the contrast of an image.
   * Method is done through simple histogram equalization.
   */
  void applyGrayscaleContrastEnhance();
  
  /**
   * Crops the image.
   *
   * @param topLeftXpt top left x point to start crop
   * @param topLeftYpt top left y point to start crop
   * @param btmRightXpt top right x point to start crop
   * @param btmRightYpt top right y point to start crop
   */
  void cropImage(int topLeftXpt, int topLeftYpt, int btmRightXpt, int btmRightYpt);
}
