package images;

/**
 * This is a class to generate flags around the country. Current supported country:
 * Norway, Greece, Switzerland
 */
public class FlagGenerators {
  
  private int[][][] generatedImage;
  
  /*
   * Flag geometries:
   *
   * Norway: https://commons.wikimedia.org/wiki/File:Flag_of_Norway_(construction_sheet).svg
   * Greece: https://www.reddit.com/r/vexillology/comments/k067k9/the_geometry_of_the_greek_flag/
   * Swiss: https://en.wikipedia.org/wiki/Flag_of_Switzerland#/media/File:Flag_of_Switzerland_(construction_sheet).svg
   */
  
  /**
   * Generates flag of Norway.
   *
   * @param height height of image
   * @return pixels of norway flag
   * @throws IllegalArgumentException if the height is too small
   */
  public int[][][] getNorwayFlag(int height) throws IllegalArgumentException {
    
    // check input validity and process
    if (height < 16) {
      throw new IllegalArgumentException("Invalid flag size");
    }
    
    // will pre-portion the image to 22:16 ratio
    int processedHeight = height - height % 16;
    int pixelPerPortion = processedHeight / 16;
    
    int[][] horizontalStripeColor = new int[][]{{186, 12, 47}, {255, 255, 255}, {186, 12, 47}};
    int[][] verticalStripeColor = new int[][]{{255, 255, 255}, {0, 32, 91}, {255, 255, 255}};
    
    generatedImage = new int[processedHeight][pixelPerPortion * 22][3];
    
    // paint the horizontal red-white-red stripe
    generateColorOnImage(horizontalStripeColor[0], 0,
            generatedImage.length - 1, 0, generatedImage[0].length - 1);
    
    // paint white stripes
    generateColorOnImage(verticalStripeColor[2], 0, generatedImage.length - 1,
            6 * pixelPerPortion, 10 * pixelPerPortion - 1);
    generateColorOnImage(verticalStripeColor[2], 6 * pixelPerPortion,
            10 * pixelPerPortion - 1, 0, generatedImage[0].length - 1);
    
    // paint the blue stripe
    generateColorOnImage(verticalStripeColor[1], 0, generatedImage.length - 1,
            7 * pixelPerPortion, 9 * pixelPerPortion - 1);
    generateColorOnImage(verticalStripeColor[1], 7 * pixelPerPortion,
            9 * pixelPerPortion - 1, 0, generatedImage[0].length - 1);
    
    
    return generatedImage;
  }
  
  /**
   * Generates flag of Greece.
   *
   * @param height height of flag
   * @return pixel of flag
   * @throws IllegalArgumentException if the height is too small
   */
  public int[][][] getGreeceFlag(int height) throws IllegalArgumentException {
    // check input validity and process
    if (height < 18) {
      throw new IllegalArgumentException("Invalid flag size");
    }
    
    // will pre-portion the image to 22:16 ratio
    int processedHeight = height - height % 18;
    int pixelPerPortion = processedHeight / 18;
    int[] horizontalStripeRatio = new int[]{6, 3, 6};
    
    int[][] color = new int[][]{{33, 117, 216}, {255, 255, 255}};
    
    generatedImage = new int[processedHeight][pixelPerPortion * 27][3];
    
    // print 9 stripes
    for (int a = 0; a < 9; a++) {
      generateColorOnImage(color[a % 2], 2 * a * pixelPerPortion,
              2 * (a + 1) * pixelPerPortion - 1, 0, generatedImage[0].length - 1);
    }
    
    // print the cross
    // blue background
    generateColorOnImage(color[0], 0, 10 * pixelPerPortion - 1,
            0, 10 * pixelPerPortion);
    generateColorOnImage(color[1], 4 * pixelPerPortion,
            6 * pixelPerPortion, 0, 10 * pixelPerPortion); // horiz
    generateColorOnImage(color[1], 0, 10 * pixelPerPortion,
            4 * pixelPerPortion, 6 * pixelPerPortion); // vert
    
    return generatedImage;
  }
  
  /**
   * Generates flag of Switzerland.
   *
   * @param height height of flag
   * @return pixel of flag
   * @throws IllegalArgumentException if the height is too small
   */
  public int[][][] getSwissFlag(int height) throws IllegalArgumentException {
    
    // check input validity and process
    if (height < 32) {
      throw new IllegalArgumentException("Invalid flag size");
    }
    
    // will pre-portion the image to 32:32 ratio
    int processedHeight;
    
    if (height % 32 <= 16) {
      // round down
      processedHeight = height - height % 32;
    } else {
      // round up
      processedHeight = height + height % 32;
    }
    
    int pixelPerPortion = processedHeight / 32;
    int[] horizontalStripeRatio = new int[]{6, 3, 6};
    
    int[][] color = new int[][]{{255, 0, 0}, {255, 255, 255}};
    
    generatedImage = new int[processedHeight][processedHeight][3];
    
    // genereate red background
    generateColorOnImage(color[0], 0, processedHeight - 1,
            0, processedHeight - 1);
    // generate white cross - vertical
    generateColorOnImage(color[1], 6 * pixelPerPortion, 26 * pixelPerPortion,
            13 * pixelPerPortion, 19 * pixelPerPortion);
    // generate white cross - horizontal
    generateColorOnImage(color[1], 13 * pixelPerPortion, 19 * pixelPerPortion,
            6 * pixelPerPortion, 26 * pixelPerPortion);
    
    
    return generatedImage;
  }
  
  private void generateColorOnImage(int[] color,
                                    int startRow, int endRow, int startCol, int endCol) {
    for (int row = startRow; row <= endRow; row++) {
      for (int col = startCol; col <= endCol; col++) {
        generatedImage[row][col] = color;
      }
    }
    
  }
  
}
