package images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * This class is a concrete implementation of the ImageModel interface.
 * Within this class. One can process and alter images with the methods below:
 * Blur; Sharpen; Grayscale; Speia; Dither; Mosaic;
 */
public class ConcreteImageModel implements ImageModel {
  
  // fields - various filtering matrix
  private static final double[][] BLUR_MATRIX_3x3
          = new double[][]{{0.0625, 0.125, 0.0625}, {0.125, 0.25, 0.125}, {0.0625, 0.125, 0.0625}};
  private static final int BLUR_MATRIX_HEIGHT = 3;
  //private static final int BLUR_MATRIX_WIDTH = 3;
  
  private static final double[][] SHARPEN_MATRIX_5x5
          = new double[][]{{-.125, -.125, -.125, -.125, -.125},
            {-.125, .25, .25, .25, -.125},
            {-.125, .25, 1.0, .25, -.125},
            {-.125, .25, .25, .25, -.125},
            {-.125, -.125, -.125, -.125, -.125}};
  
  private static final double[][] GREYSCALE_MATRIX_3x3
          = new double[][]{{0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}};
  
  private static final double[][] SEPIA_MATRIX_3x3
          = new double[][]{{.393, .769, .189}, {.349, .686, .168}, {.272, .534, .131}};
  
  private static final double[][] SOBEL_EDGE_X
          = new double[][]{{1, 0, -1},
            {2, 0, -2},
            {1, 0, -1}};
  
  private static final double[][] SOBEL_EDGE_Y
          = new double[][]{{1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}};
  
  private static final int MIN_COLOR_VALUE = 0;
  private static final int MAX_COLOR_VALUE = 255;
  
  private static final int[][] RAINBOW_COLORS
          = new int[][]{{255, 0, 0}, {255, 127, 0}, {255, 255, 0},
            {0, 255, 0}, {0, 0, 255}, {75, 0, 130}, {148, 0, 211}};
  
  // fields - operation variables
  // to load images, get image properties (static methods only)
  //private ImageUtilities image;
  private int imageHeight; // row
  private int imageWidth; // col
  
  // to store a 3D matrix in integer containing the image information
  // x - rows/height (first dimension); y - columns/width (second dimension)
  // z - Color channels: [0] = red; [1] = green; [2] = blue
  private int[][][] pixelsExistingImage;
  private int[][][] originalImage;
  private int[][][] generatedImage;
  
  private FlagGenerators flags;
  
  // constructors
  
  /**
   * Constructs an instance of the ImageModel object.
   */
  public ConcreteImageModel() {
    // it appears that nothing needs to be constructed.
    // reserved for now just in case further homework needs it.
  }
  
  // overrides
  
  /**
   * Overrides loadImage where an image is loaded into the model.
   *
   * @param filename the name of the file containing the image.
   * @throws IllegalArgumentException when filename is valid or a wrong operation
   */
  @Override
  public void loadImage(String filename) throws IllegalArgumentException {
    // check filename validity
    if (!isFilenameValid(filename)) {
      throw new IllegalArgumentException("Something wrong happened: file name might be invalid.");
    }
    // obtain the image matrix
    pixelsExistingImage = ImageUtilities.readImage(filename);
    imageHeight = pixelsExistingImage.length;
    imageWidth = pixelsExistingImage[0].length;
    
    originalImage = new int[pixelsExistingImage.length][pixelsExistingImage[0].length][3];
    
    // preserve original image
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[0].length; col++) {
        for (int channel = 0; channel < 3; channel++) {
          int intensity = pixelsExistingImage[row][col][channel];
          originalImage[row][col][channel] = intensity;
        }
      }
    }
    
  }
  
  /**
   * Overrides saveImage to save the processed image to local drive.
   *
   * @param filename the name of the file to save to
   * @throws IllegalArgumentException when filename is invalid or a wrong operation
   */
  @Override
  public void saveImage(String filename) throws IllegalArgumentException {
    // check filename validity
    if (!isFilenameValid(filename)) {
      throw new IllegalArgumentException("Something wrong happened: filename might be invalid.");
    }
    // write image to local drive
    ImageUtilities.writeImage(pixelsExistingImage, filename);
  }
  
  /**
   * Save a new image to a location.
   * @param filename  file name of the image
   * @param newImage  incoming new image
   * @throws IllegalArgumentException if filename is invalid
   */
  public void saveNewImage(String filename, int[][][] newImage) throws IllegalArgumentException {
    // check filename validity
    if (!isFilenameValid(filename)) {
      throw new IllegalArgumentException("Something wrong happened: filename might be invalid.");
    }
    // write image to local drive
    ImageUtilities.writeImage(newImage, filename);
  }
  
  /**
   * Override applyBlur to blue the image.
   */
  @Override
  public void applyBlur() {
    // Blurring is achieved by applying a matrix operation (convolution) to the whole image.
    // For each pixel in the image, each channel (R, G, B) is altered based on the blur matrix.
    // Specifically, each channel in the pixel is re-calculated by multiplying together the
    // corresponding numbers in the kernel and the pixels and adding them.
    // See the comments in the private method for specific operations.
    
    // in this method we will focus on looping through the matrix and obtaining a one-to-one
    // corresponding matrix (same size as the kernel).
    // Then operation delegates to a private method for the computation.
    int[][][] matrixG = new int[pixelsExistingImage.length][pixelsExistingImage[0].length][3];
    // OPERATION:
    // for loop to loop through each pixel row by row, column by column
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        for (int channel = 0; channel < pixelsExistingImage[row][col].length; channel++) {
          // TODO - simplify the process after testing
          // construct a one-to-one corresponding matrix that fits the blur filter
          int[][] toBeProcessed
                  = buildPixelSubMatrixToKernel(row, col, channel, BLUR_MATRIX_HEIGHT);
          
          // send to matrix convolution method fo the operation
          // replace the current matrix value
          matrixG[row][col][channel]
                  = getColorValue(matrixConvolution(BLUR_MATRIX_3x3, toBeProcessed));
        }
      }
    }
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        
        // send to matrix convolution method fo the operation
        int[] color = matrixG[row][col];
        // replace the current matrix value
        pixelsExistingImage[row][col] = color;
        
      }
    }
  }
  
  /**
   * Override applyShapren to shapren the image.
   */
  @Override
  public void applySharpen() {
    // verify similar to Blur, except the filter matrix for shapren is different.
    // see comments in applyBlur for more details.
    int[][][] matrixG = new int[pixelsExistingImage.length][pixelsExistingImage[0].length][3];
    // for loop to loop through each pixel row by row, column by column
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        for (int channel = 0; channel < pixelsExistingImage[row][col].length; channel++) {
          // TODO - simplify the process after testing
          // construct a one-to-one corresponding matrix that fits the blur filter
          int[][] toBeProcessed =
                  buildPixelSubMatrixToKernel(row, col, channel, SHARPEN_MATRIX_5x5.length);
          // send to matrix convolution method fo the operation
          // replace the current matrix value
          matrixG[row][col][channel]
                  = getColorValue(matrixConvolution(SHARPEN_MATRIX_5x5, toBeProcessed));
        }
      }
    }
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        
        // send to matrix convolution method fo the operation
        int[] color = matrixG[row][col];
        // replace the current matrix value
        pixelsExistingImage[row][col] = color;
        
      }
    }
  }
  
  /**
   * Override applyGrayscale to convert image colors to grayscale.
   */
  @Override
  public void applyGrayscale() {
    // similar to previous method, will loop through the whole image and process each pixel
    // individually using for loop
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        // for all three channels in one pixel, perform matrix transformatiion
        // by multiplying the color channel with a 3x3 matrix
        
        // generate existing color channel
        int[] existChannelValues = new int[3];
        for (int channel = 0; channel < pixelsExistingImage[row][col].length; channel++) {
          // obtain RGB values
          existChannelValues[channel] = pixelsExistingImage[row][col][channel];
        }
        
        pixelsExistingImage[row][col]
                = matrixMultiplication(GREYSCALE_MATRIX_3x3, existChannelValues);

      }
    }
  }
  
  /**
   * Override applySepia to transform current image to a color style of sepia.
   */
  @Override
  public void applySepia() {
    // similar to previous method, will loop through the whole image and proess each pixel
    // individually using for loop
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        // for all three channels in one pixel, perform matrix transformatiion
        // by multiplying the color channel with a 3x3 matrix
        
        // generate existing color channel
        int[] existChannelValues = new int[3];
        for (int channel = 0; channel < pixelsExistingImage[row][col].length; channel++) {
          // obtain RGB values
          existChannelValues[channel] = pixelsExistingImage[row][col][channel];
        }
        
        pixelsExistingImage[row][col]
                = matrixMultiplication(SEPIA_MATRIX_3x3, existChannelValues);
      }
    }
    
  }
  
  /**
   * Override applyDither to transform the image to dither style.
   */
  @Override
  public void applyDither() {
    // for the dither effect, color channel for each pixel is exammed and re-assigned to either
    // 0 or 255 based on how close the number to each end.
    // then their adjacent value is adjusted based on the difference between existing and 0/255.
    
    // first as always loop through each pixel, then for each pixel, convert existing channel to
    // either 0 or 255, and then delegate to another private method to update the adjacent channels.
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        //for (int channel = 0; channel < pixelsExistingImage[row][col].length; channel++) {
        // update current pixel value
        int channel = 0;
        int oldColor = pixelsExistingImage[row][col][channel];
        int newDitherColor = getDitherValue(pixelsExistingImage[row][col][channel]);
        pixelsExistingImage[row][col][0] = newDitherColor;
        pixelsExistingImage[row][col][1] = newDitherColor;
        pixelsExistingImage[row][col][2] = newDitherColor;
        int delta = oldColor - newDitherColor; // old minus the new
        
        // update adjacent channel value
        ditherReAssignMatrixValues(row, col, channel, delta);
        
      }
    }
  }
  
  /**
   * Override applyMosaic to transform image to a mosaic style. Number of seeds
   * is needed to for the mosaic image.
   *
   * @param seeds the number of seeds to use in the mosaic
   * @throws IllegalArgumentException if the seed number is not positive
   */
  @Override
  public void applyMosaic(int seeds) throws IllegalArgumentException {
    // check input validity
    if (!isMosaicSeedValid(seeds)) {
      throw new IllegalArgumentException("Seed input shall be a positive integer!");
    }
    
    // initialize while loop and treeSet
    int count = 0;
    // get the seed points and construct a KD Tree
    int[][] seedPoints = getRandomPixel_2(seeds);
    KdTree seedTree = new KdTree(seedPoints);
    
    // 2. Loop thru each pixels
    // 2.0 create a 3D int[][][] of the pixels, each pixel location will record its nearest seed
    int[][][] pixelMapWithSeeds = new int[imageHeight][imageWidth][2];
    int[][] seedOccurrenceCount = new int[imageHeight][imageWidth];
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      //System.out.println(row);
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        // for each pixels, get the closest seed location
        pixelMapWithSeeds[row][col] = seedTree.getShortestDistance(row, col).toArray();
        // note whatever stored in the pixelMapWithSeeds[row][col] = [seedX, seedY]
        int currentSeedRow = pixelMapWithSeeds[row][col][0];
        int currentSeedCol = pixelMapWithSeeds[row][col][1];
        
        // if it is at the current seed, jump to the next iteration
        if (row == currentSeedRow && col == currentSeedCol) {
          seedOccurrenceCount[currentSeedRow][currentSeedCol] += 1;
          continue;
        }
        
        // within the actual pixel map, add the current pixel's value to it
        for (int index = 0; index < 3; index++) {
          pixelsExistingImage[currentSeedRow][currentSeedCol][index]
                  += pixelsExistingImage[row][col][index];
        }
        
        seedOccurrenceCount[currentSeedRow][currentSeedCol] += 1;
      }
    }
    
    // recompute the seed values
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        
        if (seedOccurrenceCount[row][col] != 0) {
          
          for (int index = 0; index < 3; index++) {
            pixelsExistingImage[row][col][index]
                    = pixelsExistingImage[row][col][index] / seedOccurrenceCount[row][col];
          }
        }
  
      }
    }
    
    
    // 3.2 for each pixel location [][], update the RGB value according to their seeds
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        // for each pixel location, grab the location of the seed point
        int[] seedLocation = pixelMapWithSeeds[row][col];
        int[] newColors = pixelsExistingImage[seedLocation[0]][seedLocation[1]];
        
        // update the existing image with seed colors
        pixelsExistingImage[row][col] = newColors;
      }
    }
  }
  
  //@Override
  public void applyMosaicOld(int seeds) throws IllegalArgumentException {
    // check input validity
    if (!isMosaicSeedValid(seeds)) {
      throw new IllegalArgumentException("Seed input shall be a positive integer!");
    }
    
    // 1. generate random set of pixels as maps
    //    HashMap <Integer, int[row, col, Sum R, Sum G, Sum B, Count]>
    //    key = Arrays.Hashcode({seedX, seedY})
    
    // initialize while loop and treeSet
    int count = 0;
    
    List<Map<Integer, int[]>> seedListByRow = getRandomPixel_2Old(seeds);
    
    // 2. Loop thru each pixels
    // 2.0 create a 2D int[][] of the pixels, the value will be whatever the seeds to be calced
    int[][] pixelMapWithSeeds = new int[imageHeight][imageWidth];
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      System.out.println(row);
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        
        double[] distance = new double[]{-10, 0, 0}; // impose an invalid double[] for initial use
        
        double precentAreaExtend = 0.10;
        
        int rowLowBound = (int) Math.max(0, row - imageHeight * precentAreaExtend);
        int rowUpBound = (int) Math.min(imageHeight, row + imageHeight * precentAreaExtend);
        
        // 2.1 - NEW
        // loop through the list for that extended area
        for (int i = rowLowBound; i < rowUpBound; i++) {
          // initialize a temp hash map
          Map<Integer, int[]> seedMapPerRow = seedListByRow.get(i);
          // for hashmap in that looped row, compute the closest distance
          for (int key : seedListByRow.get(i).keySet()) {
            int[] decode = unhashPixel(key);
            boolean isWithin = isWithinRange(row, col, decode[0], decode[1]);
            
            if (seeds <= 100 || isWithin) {
              // get the seed pixel information
              int[] value = seedMapPerRow.get(key);
              // Will output a new min distance package (or the same one if not less than)
              // input double[] distance {previous distance, previousSeedRow, previousSeedCol}
              distance = calcEuclideanDist(distance, row, col, value[0], value[1]);
            }
          }
          // at this point, the closest distance should be calculated for pixel [row, col].
          // moving on.
        }
        
        // 2.2 once looping thru the whole set, a min distance should be computed
        // update the pixel map[][] with the appropriate seed (with hashed code)
        //String newKey = hashPixel(new int[]{(int) distance[1], (int) distance[2]});
        int newKey = hashPixel(new int[]{(int) distance[1], (int) distance[2]});
        pixelMapWithSeeds[row][col] = newKey;
        
        // 2.3 go back to the random seed Map and update the values += R, G, B, count++
        // get the existing array of things from the map
        Map<Integer, int[]> seedMapPerRow = seedListByRow.get((int) distance[1]);
        int[] updateValue = seedMapPerRow.get(newKey);
        // update treeMap/hashMap with key seedMap[i][j] = += R, G, B, count++
        updateValue[2] += pixelsExistingImage[row][col][0];
        updateValue[3] += pixelsExistingImage[row][col][1];
        updateValue[4] += pixelsExistingImage[row][col][2];
        updateValue[5] += 1;
        
        seedMapPerRow.replace(newKey, updateValue);
        seedListByRow.set((int) distance[1], seedMapPerRow);
      }
    }
    
    // 3. output pixels
    // 3.1 once all pixels are assigned to a seed, go to the seed and calc their RGB value
    for (int i = 0; i < seedListByRow.size(); i++) {
      
      Map<Integer, int[]> seedMapPerRow = seedListByRow.get(i);
      
      for (int key : seedMapPerRow.keySet()) {
        
        // get the value of that key
        int[] value = seedMapPerRow.get(key);
        // replace RBG value
        value[2] = getColorValue(value[2] / value[5]);
        value[3] = getColorValue(value[3] / value[5]);
        value[4] = getColorValue(value[4] / value[5]);
      }
    }
    
    // 3.2 for each pixel location [][], go to the treemap and get the value
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[row].length; col++) {
        
        int seedRow = unhashPixel(pixelMapWithSeeds[row][col])[0];
        
        int[] newColors = seedListByRow.get(seedRow).get(pixelMapWithSeeds[row][col]);
        
        // modify the RGB value
        for (int channel = 0; channel < 3; channel++) {
          pixelsExistingImage[row][col][channel] = newColors[2 + channel];
        }
      }
    }
  }
  
  /**
   * Apply edge detection through sobel operator. (Part of homework 10)
   */
  @Override
  public void applyEdgeDetection(boolean applyBlur) {
    
    applyGrayscale();
    
    double maxMagnitude = -99999;
    double minMagnitude = 99999;
    
    double[][][] matrixG = new double[pixelsExistingImage.length][pixelsExistingImage[0].length][3];
    
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[0].length; col++) {
        for (int channel = 0; channel < 3; channel++) {
          
          if (row == 0 || col == 0 || row == pixelsExistingImage.length - 1
                  || col == pixelsExistingImage[0].length - 1) {
            matrixG[row][col][channel] = 0;
          } else {
            int[][] toBeProcessed
                    = buildPixelSubMatrixToKernel(row, col, channel, BLUR_MATRIX_HEIGHT);
            
            double geeX = matrixConvolution(SOBEL_EDGE_X, toBeProcessed);
            double geeY = matrixConvolution(SOBEL_EDGE_Y, toBeProcessed);
            
            double magnitudeG = Math.sqrt(geeX * geeX + geeY * geeY);
            
            if (magnitudeG > maxMagnitude) {
              maxMagnitude = magnitudeG;
            } else if (magnitudeG < minMagnitude) {
              minMagnitude = magnitudeG;
            }
            
            matrixG[row][col][channel] = magnitudeG;
          }
        }
      }
    }
    
    // normalize each value
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[0].length; col++) {
        for (int channel = 0; channel < 3; channel++) {
          
          // get current value
          int currentValue = (int) matrixG[row][col][channel];
          
          // normalize
          int normalizedValue = (int) ((currentValue) * 255 / (1448.154));
          pixelsExistingImage[row][col][channel] = normalizedValue;
          
        }
      }
    }
  }
  
  /**
   * Generates a rainbow flag to a user's preference.
   *
   * @param isHorizontal directionality of the flag. false=vertical Flag
   * @param height       height of the image
   * @param width        width of the image
   */
  @Override
  public void generateRainbowFlag(String filename, boolean isHorizontal, int height, int width) {
    
    generatedImage = new int[height][width][3];
    int[] segments = new int[8];
    double segment;
    
    // if is horizontal, it will loop the selected segments, and loop thru all columns
    // if it is vertical, it will loop through the whole column, and sub loop to all segments
    if (isHorizontal) {
      
      segment = height / 7;
      segments[7] = height;
    } else {
      
      segment = width / 7;
      segments[7] = width;
    }
    
    for (int a = 0; a < 8 - 2; a++) {
      double prevValue = segments[a];
      segments[a + 1] = (int) (prevValue + segment);
    }
    
    // loop through each rainbow colors and assign values
    for (int i = 0; i < RAINBOW_COLORS.length; i++) {
      
      generateRainbowFlagHelper(RAINBOW_COLORS[i], isHorizontal, segments[i], segments[i + 1] - 1);
    }
    
    // save image
    saveNewImage(filename, generatedImage);
    
  }
  
  // breaks down to updating each color
  private void generateRainbowFlagHelper(int[] colors, boolean isHorizontal,
                                         int segmentStart, int segmentEnd) {
    // if is horizontal, it will loop the selected segments, and loop thru all columns
    // if it is vertical, it will loop through the whole column, and sub loop to all segments
    
    if (isHorizontal) {
      for (int row = segmentStart; row <= segmentEnd; row++) {
        for (int col = 0; col < generatedImage[0].length; col++) {
          generatedImage[row][col] = colors;
        }
      }
    } else {
      for (int row = 0; row < generatedImage.length; row++) {
        for (int col = segmentStart; col <= segmentEnd; col++) {
          generatedImage[row][col] = colors;
        }
      }
    }
    
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
    // breakdown the images into different sizes
    generatedImage = new int[squareSize][squareSize][3];
    int[] segments = new int[checkerCount + 1];
    double segment = squareSize / checkerCount;
    
    segments[checkerCount] = squareSize;
    
    for (int a = 0; a < checkerCount - 1; a++) {
      double prevValue = segments[a];
      segments[a + 1] = (int) (prevValue + segment);
    }
    
    for (int row = 0; row < checkerCount; row++) {
      for (int col = 0; col < checkerCount; col++) {
        
        int[] currentColor = colors[(col + row) % 2];
        generateColorOnImage(currentColor, segments[row],
                segments[row + 1] - 1, segments[col], segments[col + 1] - 1);
      }
    }
    
    // save image
    saveNewImage(filename, generatedImage);
  }
  
  private void generateColorOnImage(int[] color, int startRow, int endRow,
                                    int startCol, int endCol) {
    for (int row = startRow; row <= endRow; row++) {
      for (int col = startCol; col <= endCol; col++) {
        generatedImage[row][col] = color;
      }
    }
    
  }
  
  // flags: norway, greece, switzerland
  
  /**
   * Generates a flag of a chosen country.
   *
   * @param filename name of the file to be saved
   * @param country  country of flag
   * @param height   height of flag
   */
  @Override
  public void generateCountryFlag(String filename, String country, int height) {
    
    flags = new FlagGenerators();
    
    switch (country) {
      case "NORWAY":
        generatedImage = flags.getNorwayFlag(height);
        break;
      case "GREECE":
        generatedImage = flags.getGreeceFlag(height);
        break;
      case "SWITZERLAND":
        generatedImage = flags.getSwissFlag(height);
        break;
      default:
        System.out.println("No name matched");
        break;
    }
    
    ImageUtilities.writeImage(generatedImage, filename);
  }
  
  /**
   * Enhance an grey scale image to intensify the contrast of an image.
   * Method is done through simple histogram equalization.
   */
  @Override
  public void applyGrayscaleContrastEnhance() {
    // Steps:
    // 1. Aggregate the intensity (just one because it's grey scale) into an array.
    // 2. Normalize the histogram
    // 3. reassign each pixel to the normalized channel
    
    // Define fields
    int[] histogramOrignal = new int[256]; // indexes represent each intensity
    int[] histogramNormalized = new int[256];
    
    // convert the image to grayscale
    applyGrayscale();
    
    // aggregate the pixels
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[0].length; col++) {
        // locate the intensity, add one to the counter
        // ie. if the intensity if 198, then histogram[198]++;
        histogramOrignal[pixelsExistingImage[row][col][0]]++;
      }
    }
    
    // Normalization
    // loop through the array to find the min value
    int totalPixelCount = pixelsExistingImage.length * pixelsExistingImage[0].length;
    int minIntensity = totalPixelCount;
    System.out.println(totalPixelCount);
    for (int a : histogramOrignal) {
      if (a < minIntensity && a != 0) {
        minIntensity = a;
      }
      
    }
    // apply normalization equation
    // reference: https://en.wikipedia.org/wiki/Histogram_equalization
    // https://www.math.uci.edu/icamp/courses/math77c/demos/hist_eq.pdf
    // looping through each intensity from 0 - 255
    int sumIntensity = 0;
    for (int b = 0; b < histogramOrignal.length; b++) {
      
      sumIntensity += histogramOrignal[b];
      
      histogramNormalized[b] = sumIntensity * 255 / totalPixelCount;
      
    }
    
    // reassign each pixels
    // the new intensity = histogramNormal[old intensity]
    for (int row = 0; row < pixelsExistingImage.length; row++) {
      for (int col = 0; col < pixelsExistingImage[0].length; col++) {
        
        int newIntensity = histogramNormalized[pixelsExistingImage[row][col][0]];
        // reassign values
        for (int channel = 0; channel < 3; channel++) {
          pixelsExistingImage[row][col][channel] = newIntensity;
        }
      }
    }
    
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
    // define the size of the cropped image
    int[][][] croppedImage = new int[btmRightYpt - topLeftYpt + 1][btmRightXpt - topLeftXpt + 1][3];
    
    int arrayHeight = btmRightXpt - topLeftXpt + 1;
    int arrayWidth = btmRightYpt - topLeftYpt + 1;
    
    for (int row = 0; row < croppedImage.length; row++) {
      for (int col = 0; col < croppedImage[0].length; col++) {
        for (int channel = 0; channel < 3; channel++) {
          croppedImage[row][col][channel] =
                  pixelsExistingImage[row + topLeftYpt][col + topLeftXpt][channel];
        }
      }
    }
    
    pixelsExistingImage = croppedImage;
  }
  
  /**
   * Returns an image for the controller to update the view.
   *
   * @return a bufferImage. If there's no pixels, will return null.
   */
  @Override
  public BufferedImage getImage() {
    
    if (pixelsExistingImage != null) {
      return createBufferImg(pixelsExistingImage);
    }
    return null;
  }
  
  /**
   * Revert the image to its original state.
   */
  @Override
  public void revertImage() {
    
    pixelsExistingImage = new int[originalImage.length][originalImage[0].length][3];
    
    // preserve original image
    for (int row = 0; row < originalImage.length; row++) {
      for (int col = 0; col < originalImage[0].length; col++) {
        for (int channel = 0; channel < 3; channel++) {
          int intensity = originalImage[row][col][channel];
          pixelsExistingImage[row][col][channel] = intensity;
        }
      }
    }
  }
  
  private BufferedImage createBufferImg(int[][][] pixels) {
    
    imageHeight = pixelsExistingImage.length;
    imageWidth = pixelsExistingImage[0].length;
    
    BufferedImage output
            = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    // Reference: ImageUtilities, writeImage on how to generate a buffered Image
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {
        int r = pixels[i][j][0];
        int g = pixels[i][j][1];
        int b = pixels[i][j][2];
        
        // color is stored in 1 integer, with the 4 bytes storing ARGB in that
        // order. Each of r,g,b are stored in 8 bits (hence between 0 and 255).
        // So we put them all in one integer by using bit-shifting << as below
        int color = (r << 16) + (g << 8) + b;
        output.setRGB(j, i, color);
      }
    }
    
    return output;
  }
  
  // getters
  
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
  
  private boolean isMosaicSeedValid(int seeds) {
    return (seeds >= 0);
  }
  
  // private helper to a sub-matrix in preparation to the blur operation
  // it takes in row, col, and channel position of the matrix as center value
  // crop around the existing matrix to fix the kernel matrix size
  // and will return a sub-matrix that matches the kernel size
  private int[][] buildPixelSubMatrixToKernel(int row, int col, int channel, int size) {
    // NOTE: this only works for all sizes of matrix.
    int[][] subPixelMatrix = new int[size][size];
    
    //r = 0; c = 0; want row - 1; col - 1
    //r = 0; c = 1; want row - 1; col
    //r = 0; c = 2; want row - 1; col + 1
    
    int[] calcCoeff = new int[size];
    
    for (int a = 0; a < size; a++) {
      calcCoeff[a] = -1 * size / 2 + a;
    }
    
    for (int r = 0; r < subPixelMatrix.length; r++) {
      for (int c = 0; c < subPixelMatrix[r].length; c++) {
        int pointerRow;
        if (row + calcCoeff[r] < 0 || row + calcCoeff[r] >= pixelsExistingImage.length
                || col + calcCoeff[c] < 0 || col + calcCoeff[c] >= pixelsExistingImage[r].length) {
          subPixelMatrix[r][c] = 0;
        } else {
          subPixelMatrix[r][c] =
                  pixelsExistingImage[row + calcCoeff[r]][col + calcCoeff[c]][channel];
        }
      }
      
    }
    
    return subPixelMatrix;
  }
  
  // private helper to perform matrix convolution
  // takes in a sub-matrix, multiply/add to the kernel (one-to-one spot)
  // and will return a sum channel value to replace the existing one
  private int matrixConvolution(double[][] filterMatrix, int[][] subMatrix) {
    int sum = 0;
    
    for (int r = 0; r < filterMatrix.length; r++) {
      for (int c = 0; c < filterMatrix[r].length; c++) {
        sum += filterMatrix[r][c] * (double) subMatrix[r][c];
      }
    }
    
    //System.out.println(sum);
    
    return sum;
  }
  
  // private helper to perform matrix multiplication
  // will return a result of the operation
  // tested
  private int[] matrixMultiplication(double[][] filterMatrix, int[] subMatrix) {
    // I'm going just to write it for a single column matrix multiplication
    // since other matrix sizes are irrelevant to the scope of this homework
    int[] finalResultSize = new int[]{filterMatrix.length, 1};
    int[] matrixProduct = new int[3];
    int matrix1 = 0;
    double matrix2 = 0;
    double product = 0;
    
    for (int i = 0; i < finalResultSize[0]; i++) {
      
      product = 0.0;
      
      for (int j = 0; j < finalResultSize[0]; j++) {
        matrix1 = subMatrix[j];
        matrix2 = filterMatrix[i][j];
        product += (double) matrix1 * matrix2;
      }
      matrixProduct[i] = getColorValue((int) product);
    }
    
    return matrixProduct;
  }
  
  // private helper to clamp the value between 0 - 255 (inclusive on each end)
  // tested
  private int getColorValue(int colorChannel) {
    // prevent color to go below 0
    if (colorChannel < MIN_COLOR_VALUE) {
      return MIN_COLOR_VALUE;
    }
    // prevent color to go above 255
    if (colorChannel > MAX_COLOR_VALUE) {
      return MAX_COLOR_VALUE;
    }
    // if in between, then return it's normal value
    return colorChannel;
  }
  
  // private method to obtain the desired ceiling/floor value for the dither effect.
  // for example, for channel value 230, it will return 255 since it's the closest to 255.
  // and value 126 will return 0 since it's the closest to 0.
  private int getDitherValue(int color) throws IllegalArgumentException {
    if (color >= 127 && color <= 255) {
      return 255;
    } else if (color >= 0) {
      return 0;
    } else {
      throw new IllegalArgumentException("input color value must be between 0 and 255");
    }
  }
  
  // private method to reassign the adjacent value within adjacent values.
  // since the Floyd-Schnieder model is quite straight forward, will "brute force" and solve
  private void ditherReAssignMatrixValues(int row, int col, int channel, int delta) {
    // position to be re-assigned
    // let the original position be (r, c)
    // new locations: (r, c+1), (r+1)(c+1), (r+1, c), (r+1, c+1)
    int test = (int) (7.0 / 16 * delta);
    for (int i = 0; i < 3; i++) {
      
      if (col + 1 < imageWidth) {
        pixelsExistingImage[row][col + 1][i]
                = getColorValue(pixelsExistingImage[row][col + 1][i]
                + (int) (7.0 / 16 * delta));
      }
      
      if (row + 1 < imageHeight && col - 1 > 0) {
        pixelsExistingImage[row + 1][col - 1][i]
                = getColorValue(pixelsExistingImage[row + 1][col - 1][i]
                + (int) (3.0 / 16 * delta));
      }
      
      if (row + 1 < imageHeight) {
        pixelsExistingImage[row + 1][col][i]
                = getColorValue(pixelsExistingImage[row + 1][col][i]
                + (int) (5.0 / 16 * delta));
      }
      
      if (row + 1 < imageHeight && col + 1 < imageWidth) {
        pixelsExistingImage[row + 1][col + 1][i]
                = getColorValue(pixelsExistingImage[row + 1][col + 1][i]
                + (int) (1.0 / 16 * delta));
      }
    }
  }
  
  private int[][] getRandomPixel_2(int seeds) {
    // generate an array of ordered pixels, first dim for width, second dim for height
    int[][] randomPixel = new int[imageWidth * imageHeight][2];
    int[][] randomPixelOutput = new int[seeds][2];
    
    int count = 0;
    
    // populate pixels
    for (int loopRow = 0; loopRow < imageHeight; loopRow++) {
      for (int loopCol = 0; loopCol < imageWidth; loopCol++) {
        randomPixel[count] = new int[]{loopRow, loopCol};
        count++;
      }
    }
    
    // shuffle the array
    randomPixel = shuffleArrayPrimitive(randomPixel);
    
    // assign random pixels but shuffle/reshuffling the array
    for (int i = 0; i < seeds; i++) {
      randomPixelOutput[i] = randomPixel[i];
    }
    
    return randomPixelOutput;
  }
  
  private List<Map<Integer, int[]>> getRandomPixel_2Old(int seeds) {
    // generate an array of ordered pixels, first dim for width, second dim for height
    int[][] randomPixel = new int[imageWidth * imageHeight][2];
    List<Map<Integer, int[]>> seedListByRow = new ArrayList<>();
    //Map<Integer, int[]> seedMap = new HashMap<Integer, int[]>();
    
    for (int i = 0; i < imageHeight; i++) {
      seedListByRow.add(new HashMap<Integer, int[]>());
    }
    
    int count = 0;
    
    // populate pixels
    for (int loopRow = 0; loopRow < imageHeight; loopRow++) {
      for (int loopCol = 0; loopCol < imageWidth; loopCol++) {
        randomPixel[count] = new int[]{loopRow, loopCol};
        count++;
      }
    }
    
    // shuffle the array
    randomPixel = shuffleArrayPrimitive(randomPixel);
    
    // assign random pixels but shuffle/reshuffling the array
    for (int i = 0; i < seeds; i++) {
      
      // get the pixel location
      int row = randomPixel[i][0];
      //int col = randomPixel[i][1];
      // take the seed map per row out, add in the pixel location, and re-add back
      Map<Integer, int[]> seedMapPerRow = seedListByRow.get(row);
      seedMapPerRow.put(hashPixel(randomPixel[i]),
              new int[]{randomPixel[i][0], randomPixel[i][1], 0, 0, 0, 0});
      seedListByRow.set(row, seedMapPerRow);
      
    }
    
    return seedListByRow;
  }
  
  private int[][] shuffleArrayPrimitive(int[][] toShuffle) {
    
    Random rand = ThreadLocalRandom.current();
    
    for (int i = toShuffle.length - 1; i > 0; i--) {
      int randomNumber = rand.nextInt(i + 1);
      int[] pixelAtRandom = toShuffle[randomNumber];
      // swap location
      toShuffle[randomNumber] = toShuffle[i];
      toShuffle[i] = pixelAtRandom;
    }
    
    return toShuffle;
  }
  
  // private method to filters out seeds that is too far away from the current pixel
  // Let's define the extend of the pixel range to be 25% of height/width in each direction
  // tested
  private Boolean isWithinRange(int row, int col, int seedRow, int seedCol) {
    
    double precentAreaExtend = 0.10;
    
    int rowLowBound = (int) Math.max(0, row - imageHeight * precentAreaExtend);
    int rowUpBound = (int) Math.min(imageHeight, row + imageHeight * precentAreaExtend);
    int colLowBound = (int) Math.max(0, col - imageWidth * precentAreaExtend);
    int colUpBound = (int) Math.min(imageWidth, col + imageWidth * precentAreaExtend);
    
    return (seedRow <= rowUpBound && seedRow >= rowLowBound
            && seedCol <= colUpBound && seedCol >= colLowBound);
  }
  
  // input double[] distance {previous distance, previousSeedRow, previousSeedCol}
  // tested
  private double[] calcEuclideanDist(double[] distance, int row,
                                     int col, int seedRow, int seedCol) {
    // compute the Euclidean distance
    double eucDistance = Math.sqrt(Math.pow((seedRow - row), 2) + Math.pow((seedCol - col), 2));
    // if the new distance is smaller, update the value
    if (distance[0] < 0 || distance[0] > eucDistance) {
      // means existing one has a larger distance, keep the current one instead
      return new double[]{eucDistance, seedRow, seedCol};
    }
    // if it's equal or larger, return previous value
    return distance;
  }
  
  // I have to use a more sophisticated way to uniquely identify each pixels since Arrays.hashcode
  // doesn't work well.
  private int hashPixel(int[] pixelArray) {
    /* Citation for using Center Pairing Function:
     “Pairing function,” Wikipedia, 30-Jan-2022. [Online].
         Available: https://en.wikipedia.org/wiki/Pairing_function. [Accessed: 25-Mar-2022].
     */
    int width = pixelArray[0];
    int height = pixelArray[1];
    int output = (int) (0.5 * (width + height) * (width + height + 1) + height);
    return output;
  }
  
  private int[] unhashPixel(int hashedCode) {
    /* Citation for using Inverse Canter Pairing Function.
     “Pairing function,” Wikipedia, 30-Jan-2022. [Online].
         Available: https://en.wikipedia.org/wiki/Pairing_function. [Accessed: 25-Mar-2022].
     */
    
    // Pairing function - reversed
    int w;
    int t;
    int y;
    int x;
    
    w = (int) Math.floor(0.5 * (Math.sqrt(8.0 * hashedCode + 1) - 1));
    t = (int) (0.5 * (w * w + w));
    y = hashedCode - t;
    x = w - y;
    
    return new int[]{x, y};
  }
}
