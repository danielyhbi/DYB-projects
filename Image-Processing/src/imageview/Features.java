package imageview;

/**
 * This represents a set of features that this program offers. Each feature is exposed as
 * a function in this interface.
 *
 * <p>Each function is designed to take in the neessary data to complete that function.</p>
 */
public interface Features {
  
  /**
   * Exit the program.
   */
  void exitProgram();
  
  /**
   * Open file. Will use JFileChooser.
   */
  void openFiles(String filePath);
  
  /**
   * Save a file.
   */
  void saveFiles(String filePath);
  
  /**
   * Apply all the filters from script. User shall run this after
   * inputting stuff from the textField. A message box should pop up
   * displaying any error messages.
   * <p>
   * Applying filters will not update the image preview
   */
  void applyFilterScript(String[] tokens);
  
  /**
   * Apply a mosaic filter. Will prompt user to enter the seed number. Maybe.
   */
  void applyMosaic(String seeds);
  
  /**
   * Apply a blur filter.
   */
  void applyBlur();
  
  /**
   * Apply a sharpen filter.
   */
  void applySharpen();
  
  /**
   * Apply a dither filter.
   */
  void applyDither();
  
  /**
   * Apple a sepia filter.
   */
  void applySepia();
  
  /**
   * Apply a grayscale filter.
   */
  void applyGrayScale();
  
  /**
   * display readme.
   */
  void readMe();
  
  /**
   * Revert Image.
   */
  void revertImage();
  
  /**
   * Crops the image.
   *
   * @param topLeftXpt  top left x point to start crop
   * @param topLeftYpt  top left y point to start crop
   * @param btmRightXpt top right x point to start crop
   * @param btmRightYpt top right y point to start crop
   */
  void cropImage(int topLeftXpt, int topLeftYpt, int btmRightXpt, int btmRightYpt);
  
  /**
   * Apply sobel edge detection.
   */
  void applyEdgeDetection();
  
  /**
   * Apply contrast enhancement.
   */
  void applyContrastEnhancement();
  
  /**
   * Generate flag of a country.
   * @param countryName Name of the country
   * @param height  Height of the image
   */
  void generateCountryFlag(String fileName, String countryName, int height);
  
  /**
   * Generate rainbow flag based on the input height.
   * @param height height of the image
   * @param isVertical is the image vertical
   */
  void generateRainbowFlag(String fileName, int width, int height, boolean isVertical);
  
  
  /**
   * Generate an image with check board pattern.
   *
   * @param filename     name of the file to be saved
   * @param colors       two colors chosen from users
   * @param checkerCount number of checkers per row
   * @param squareSize   size of the image
   */
  void generateCheckerBoard(String filename, int[][] colors, int checkerCount, int squareSize);
  
}
