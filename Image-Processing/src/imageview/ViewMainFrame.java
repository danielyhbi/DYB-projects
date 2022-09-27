package imageview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileSystemView;

/*
  References:
  https://web.mit.edu/6.005/www/sp14/psets/ps4/java-6-tutorial/components.html
  https://stackoverflow.com/questions/13366793/how-do-you-make-menu-item-jmenuitem-shortcut
  https://alvinalexander.com/blog/post/jfc-swing/how-program-apple-command-key-keystroke-java-swing-mac-osx/
  https://stackoverflow.com/questions/11006496/select-an-area-to-capture-using-the-mouse
  https://stackoverflow.com/questions/7851505/how-can-a-keylistener-detect-key-combinations-e-g-alt-1-1
 */
 

/**
 * This is a view class that provides graphical user interface for user to process images.
 */
public class ViewMainFrame extends JFrame implements View {
  
  JMenu menu;
  JMenuBar menuBar;
  // JMenuItems
  JMenuItem menuItem;
  JMenuItem menuOpenFile;
  JMenuItem menuSaveFile;
  JMenuItem menuOpenFolder;
  JMenuItem menuExit;
  JMenuItem menuMosaic;
  JMenuItem menuBlur;
  JMenuItem menuSharpen;
  JMenuItem menuDither;
  JMenuItem menuSepia;
  JMenuItem menuGrayscale;
  JMenuItem menuEditScript;
  JMenuItem menuRevert;
  JMenuItem menuReadMe;
  JMenuItem menuCrop;
  JMenuItem menuContrastEnhance;
  JMenuItem menuEdgeDetect;
  JMenuItem menuGenerateFlag;
  JMenuItem menuGenerateRainbowFlag;
  JMenuItem menuGenerateCheckBoard;
  JMenuItem menuEditFlipHorizontal;
  JMenuItem menuEditFlipVertical;
  JMenuItem menuRotateCounterClockWise;
  JMenuItem menuRotateClockWise;
  // JLabels
  JLabel imageLabel;
  JLabel screenCropLabel;
  JLabel selectionLabel;
  JLabel mainMousePosition;
  
  JPanel imagePanel;
  JPanel textPanel;
  JPanel cropImgDialogPanel;
  JTextArea scriptText;
  // JButtons
  JButton buttonApplyFilter;
  // Misc JFrames
  
  JScrollPane scrollPane;
  JScrollPane screenScroll;
  
  BufferedImage img;
  BufferedImage imgCopy;
  BufferedImage imgCrop;
  BufferedImage imgCropCopy;
  JFileChooser fileChooser;
  
  File fileFolder;
  String filePath;
  
  Rectangle captureRect;
  Boolean mainMouseListenerActive = true;
  Boolean cropMouseListenerActive = false;
  
  /**
   * Construct a frame for the user interface.
   */
  public ViewMainFrame() {
    // set the title of the frame
    super("Image Edit - GUI");
    
    setSize(1000, 800);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.setLayout(new BorderLayout());
    
    // create menu
    this.addOverallMenu();
    
    // create main work area
    this.addWorkArea();
    
    setVisible(true);
  }
  
  private void addOverallMenu() {
    menuBar = new JMenuBar();
    
    // build the first menu - File
    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_A);
    menuBar.add(menu);
    
    // several menu items
    menuOpenFile = new JMenuItem("Open File");
    menuOpenFile.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuOpenFile);
    
    menuSaveFile = new JMenuItem("Save File");
    menuSaveFile.setEnabled(false);
    menuSaveFile.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuSaveFile);
    
    menu.addSeparator();
    
    menuOpenFolder = new JMenuItem("Open Source File Folder");
    menuOpenFolder.setEnabled(false);
    menu.add(menuOpenFolder);
    
    menu.addSeparator();
    
    menuExit = new JMenuItem("Exit");
    menuExit.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuExit);
    
    // =================
    // build second menu - Edit
    menu = new JMenu("Edit");
    menuBar.add(menu);
    
    menuMosaic = new JMenuItem("Mosaic");
    menuMosaic.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
    menuMosaic.setEnabled(false);
    menu.add(menuMosaic);
    
    menuBlur = new JMenuItem("Blur");
    menuBlur.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK));
    menuBlur.setEnabled(false);
    menu.add(menuBlur);
    
    menuSharpen = new JMenuItem("Sharpen");
    menuSharpen.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK));
    menuSharpen.setEnabled(false);
    menu.add(menuSharpen);
    
    menuDither = new JMenuItem("Dither");
    menuDither.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK));
    menuDither.setEnabled(false);
    menu.add(menuDither);
    
    menuSepia = new JMenuItem("Sepia");
    menuSepia.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_DOWN_MASK));
    menuSepia.setEnabled(false);
    menu.add(menuSepia);
    
    menuGrayscale = new JMenuItem("Grayscale");
    menuGrayscale.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_6, KeyEvent.CTRL_DOWN_MASK));
    menuGrayscale.setEnabled(false);
    menu.add(menuGrayscale);
    
    menu.addSeparator();
    
    menuCrop = new JMenuItem("Crop Image");
    menuCrop.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_7, KeyEvent.CTRL_DOWN_MASK));
    menuCrop.setEnabled(false);
    menu.add(menuCrop);
    
    menuRevert = new JMenuItem("Revert to Original Image");
    menuRevert.setEnabled(false);
    menu.add(menuRevert);
    
    menu.addSeparator();
    
    menuRotateClockWise = new JMenuItem("Rotate Clockwise");
    menuRotateClockWise.setEnabled(false);
    menu.add(menuRotateClockWise);
    menuRotateCounterClockWise = new JMenuItem("Rotate Counter Clockwise");
    menuRotateCounterClockWise.setEnabled(false);
    menu.add(menuRotateCounterClockWise);
    
    
    menuEditFlipHorizontal = new JMenuItem("Flip Horizontally");
    menuEditFlipHorizontal.setEnabled(false);
    menu.add(menuEditFlipHorizontal);
    menuEditFlipVertical = new JMenuItem("Flip Vertically");
    menuEditFlipVertical.setEnabled(false);
    menu.add(menuEditFlipVertical);
    
    menuBar.add(menu);
    
    // =================
    // build third menu - Enhance
    menu = new JMenu("Enhance");
    menuBar.add(menu);
    
    menuEdgeDetect = new JMenuItem("Edge Detection");
    menuEdgeDetect.setEnabled(false);
    menuEdgeDetect.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_8, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuEdgeDetect);
    
    menuContrastEnhance = new JMenuItem("Contrast Enhancement");
    menuContrastEnhance.setEnabled(false);
    menuContrastEnhance.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_9, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuContrastEnhance);
    
    // =================
    // build forth menu - Generate
    menu = new JMenu("Generate");
    menuBar.add(menu);
    
    menuGenerateFlag = new JMenuItem("Country Flag");
    //menuGenerateFlag.setEnabled(false);
    menuGenerateFlag.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuGenerateFlag);
    
    menuGenerateRainbowFlag = new JMenuItem("Rainbow Flag");
    menuGenerateRainbowFlag.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuGenerateRainbowFlag);
    
    menu.addSeparator();
    
    menuGenerateCheckBoard = new JMenuItem("Checked Board");
    menuGenerateCheckBoard.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
    menu.add(menuGenerateCheckBoard);
    
    // =================
    // build fifth menu - Help
    
    menu = new JMenu("Help");
    menuBar.add(menu);
    
    menuReadMe = new JMenuItem("About ImageEdit");
    menuReadMe.setEnabled(false);
    menu.add(menuReadMe);
    
    menuBar.add(menu);
    
    this.setJMenuBar(menuBar);
  }
  
  private void addWorkArea() {
    // add the image preview
    try {
      img = ImageIO.read(new FileInputStream("src/welcome-page.png"));
      imgCrop = ImageIO.read(new FileInputStream("src/welcome-page.png"));
      
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
    
    
    imageLabel = new JLabel(new ImageIcon(img));
    imgCopy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    imageLabel.addMouseMotionListener(new MainImageMouseMotionListener());
    
    imagePanel = new JPanel();
    imagePanel.setLayout(new BorderLayout());
    imagePanel.add(imageLabel, BorderLayout.CENTER);
    
    mainMousePosition = new JLabel("Selection: ");
    imagePanel.add(mainMousePosition, BorderLayout.SOUTH);
    
    // use box layout
    // initialize the scrollPane with image to put on the frame
    scrollPane = new JScrollPane(imagePanel);
    scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    scrollPane.setAlignmentY(Component.CENTER_ALIGNMENT);
    
    this.add(scrollPane, BorderLayout.CENTER);
    
    this.pack();
  }
  
  /**
   * Accept the set of callbacks from the controller, and hoop up as needed to all
   * of the buttons, jmenuitem from the view.
   *
   * @param f sets of callbacks
   */
  public void setFeatures(Features f) {
    // creates actionListener itself, and call the appropriate feature method
    // features is also implemented in the controller
    
    // File menu
    menuOpenFile.addActionListener(l -> {
      fileChooser = new JFileChooser(fileFolder, FileSystemView.getFileSystemView());
      int r = fileChooser.showOpenDialog(null);
      if (r == JFileChooser.APPROVE_OPTION) {
        filePath = fileChooser.getSelectedFile().getAbsolutePath();
        System.out.println(fileChooser.getCurrentDirectory());
        fileFolder = fileChooser.getCurrentDirectory();
        f.openFiles(filePath);
        menuSaveFile.setEnabled(true);
        this.reEnableEdits();
      }
      menuOpenFolder.setEnabled(true);
      this.disableScriptEdit();
    });
    
    // save file
    menuSaveFile.addActionListener(l -> {
      fileChooser = new JFileChooser(fileFolder, FileSystemView.getFileSystemView());
      int r = fileChooser.showSaveDialog(null);
      if (r == JFileChooser.APPROVE_OPTION) {
        String savedPath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!savedPath.endsWith(".jpg")) {
          savedPath += ".jpg";
        }
        f.saveFiles(savedPath);
      }
    });
    
    // open file
    menuOpenFolder.addActionListener(l -> {
      Desktop dstp = Desktop.getDesktop();
      try {
        dstp.open(fileFolder);
      } catch (IOException e) {
        throw new IllegalArgumentException(e);
      }
    });
    menuExit.addActionListener(l -> f.exitProgram());
    
    // Edit Menu
    // Menu -> Mosaic
    menuMosaic.addActionListener(l -> {
      String seeds = JOptionPane.showInputDialog("Please a seed number for mosaic:", 1000);
      f.applyMosaic(seeds);
    });
    // Menu -> Blur
    menuBlur.addActionListener(l -> f.applyBlur());
    // Menu -> Sharpen
    menuSharpen.addActionListener(l -> f.applySharpen());
    // Menu -> Dither
    menuDither.addActionListener(l -> f.applyDither());
    // Menu -> Sepia
    menuSepia.addActionListener(l -> f.applySepia());
    // Menu -> Grayscale
    menuGrayscale.addActionListener(l -> f.applyGrayScale());
    // Menu -> Crop
    menuCrop.addActionListener(l -> {
      cropMouseListenerActive = true;
      imgCropCopy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
      screenCropLabel = new JLabel(new ImageIcon(imgCropCopy));
      CropImageBoxMouseListener cropMouseListen = new CropImageBoxMouseListener();
      screenCropLabel.addMouseMotionListener(cropMouseListen);
      screenScroll = new JScrollPane(screenCropLabel);
      
      // set the scroll size
      screenScroll.setPreferredSize(
              new Dimension(img.getWidth(), img.getHeight()));
      // setup the layout
      cropImgDialogPanel = new JPanel(new BorderLayout());
      cropImgDialogPanel.add(screenScroll, BorderLayout.CENTER);
      selectionLabel = new JLabel(
              "Select and drag on the image for cropping");
      cropImgDialogPanel.add(selectionLabel, BorderLayout.SOUTH);
      
      repaintImg(imgCrop, imgCropCopy);
      screenCropLabel.repaint();
      
      // show dialog
      int result = JOptionPane.showConfirmDialog(null, cropImgDialogPanel,
              "Select Area to Crop Image", JOptionPane.OK_CANCEL_OPTION);
      
      if (result == JOptionPane.OK_OPTION) {
        //System.out.println("Rectangle of interest: " + captureRect);
        
        // get the dimension of the rectangle
        int topLeftX = (int) captureRect.getX();
        int topLeftY = (int) captureRect.getY();
        int recWidth = (int) captureRect.getWidth();
        int recHeight = (int) captureRect.getHeight();
        
        f.cropImage(topLeftX, topLeftY, topLeftX + recWidth - 1,
                topLeftY + recHeight - 1);
      }
      
      // post-script
      cropMouseListenerActive = false;
      captureRect = null;   // reset the rectangle
      repaintImg(imgCrop, imgCropCopy);
      
    });
    // Menu -> Revert
    menuRevert.addActionListener(l -> f.revertImage());
    // Menu -> Rotate Counter Clockwise
    menuRotateCounterClockWise.addActionListener(l -> f.rotateImageCounterClockwise());
    // Menu -> Rotate clockwise
    menuRotateClockWise.addActionListener(l -> f.rotateImageClockwise());
    // Menu -> Flip Horiz
    menuEditFlipHorizontal.addActionListener(l -> f.flipImageHorizontal());
    // Menu -> Flip Vert
    menuEditFlipVertical.addActionListener(l -> f.flipImageVertical());
    
    // Enhance menu
    // Enhance -> Edge Detection
    menuEdgeDetect.addActionListener(l -> f.applyEdgeDetection());
    // Enhance -> Contrast Enhance
    menuContrastEnhance.addActionListener(l -> f.applyContrastEnhancement());
    
    // Generate menu
    // Generate -> flags
    menuGenerateFlag.addActionListener(l -> {
      // will prompt user to select which flag to have
      // and send the info to the controller
      JTextField imageHeght = new JTextField(5);
      JTextField fileName = new JTextField(5);
      Object[] countries = {"Norway", "Greece", "Switzerland"};
      JComboBox listOfCountries = new JComboBox(countries);
      JPanel flagOptions = new JPanel();
      flagOptions.setLayout(new BoxLayout(flagOptions, BoxLayout.Y_AXIS));
      
      flagOptions.add(new JLabel("Select Country:"));
      flagOptions.add(listOfCountries);
      flagOptions.add(new JLabel("Enter Approximate Image Height:"));
      flagOptions.add(imageHeght);
      flagOptions.add(new JLabel("Enter Filename to be Saved:"));
      flagOptions.add(fileName);
      
      flagOptions.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      int result = JOptionPane.showConfirmDialog(null, flagOptions, 
              "Country Flag Preferences", JOptionPane.OK_CANCEL_OPTION);
      String country;
      String savedFileName;
      int height;
      
      if (result == JOptionPane.OK_OPTION) {
        savedFileName = "saved-generated-image/" + fileName.getText();
        if (!savedFileName.endsWith(".png")) {
          savedFileName += ".png";
        }
        country = listOfCountries.getSelectedItem().toString();
        try {
          height = checkImageHeightInput(imageHeght.getText());
          f.generateCountryFlag(savedFileName, country, height);
        } catch (IllegalArgumentException e) {
          displayMessage("Please re-enter your options: " + e.getMessage());
        }
      }
    });
    // Generate -> Rainbow flags
    menuGenerateRainbowFlag.addActionListener(l -> {
      // will prompt user to input the height of the rainbow flag
      // and send the into to the controller
      JTextField imageHeight = new JTextField(5);
      JTextField imageWidth = new JTextField(5);
      JTextField fileName = new JTextField(5);
      JComboBox flagOrientation = new JComboBox(new Object[]{"Vertical Flag", "Horizontal Flag"});
      
      JPanel rainbowFlag = new JPanel();
      rainbowFlag.setLayout(new BoxLayout(rainbowFlag, BoxLayout.Y_AXIS));
      
      rainbowFlag.add(new JLabel("Select Flag Orientation:"));
      rainbowFlag.add(flagOrientation);
      rainbowFlag.add(new JLabel("Enter Image Height:"));
      rainbowFlag.add(imageHeight);
      rainbowFlag.add(new JLabel("Enter Image Width:"));
      rainbowFlag.add(imageWidth);
      rainbowFlag.add(new JLabel("Enter Filename to be Saved:"));
      rainbowFlag.add(fileName);
      
      int result = JOptionPane.showConfirmDialog(null, rainbowFlag, 
              "Enter Rainbow Flag Preferences", JOptionPane.OK_CANCEL_OPTION);
      
      if (result == JOptionPane.OK_OPTION) {
        try {
          int width = checkImageHeightInput(imageWidth.getText());
          int height = checkImageHeightInput(imageHeight.getText());
          String savedFileName = "saved-generated-image/" + fileName.getText();
          if (!savedFileName.endsWith(".png")) {
            savedFileName += ".png";
          }
          boolean isVertical;
          
          isVertical = (flagOrientation.getSelectedIndex() == 0);
          
          f.generateRainbowFlag(savedFileName, width, height, isVertical);
          
        } catch (IllegalArgumentException e) {
          displayMessage("Please revise your input: " + e.getMessage());
        }
      }
    });
    // Generate -> Checker board
    menuGenerateCheckBoard.addActionListener(l -> {
      // will prompt user to input the height of the rainbow flag
      // and send the into to the controller
      JTextField checkerCount = new JTextField(5);
      JTextField squareSize = new JTextField(5);
      JTextField fileName = new JTextField(5);
      
      Color color1 = JColorChooser.showDialog(null, "Select the first color", Color.RED);
      Color color2 = JColorChooser.showDialog(null, "Select the second color", Color.WHITE);
      
      JLabel labelColor1 = new JLabel("Color 1: " + color1.toString());
      labelColor1.setOpaque(true);
      labelColor1.setBackground(color1);
      JLabel labelColor2 = new JLabel("Color 2: " + color2.toString());
      labelColor2.setOpaque(true);
      labelColor2.setBackground(color2);
      
      JPanel checkerImagePanel = new JPanel();
      checkerImagePanel.setLayout(new BoxLayout(checkerImagePanel, BoxLayout.Y_AXIS));
      
      checkerImagePanel.add(labelColor1);
      checkerImagePanel.add(labelColor2);
      checkerImagePanel.add(new JSeparator());
      checkerImagePanel.add(new JLabel("Enter Amount of Checkers on each row/column:"));
      checkerImagePanel.add(checkerCount);
      checkerImagePanel.add(new JLabel("Enter Image Width/Height:"));
      checkerImagePanel.add(squareSize);
      checkerImagePanel.add(new JLabel("Enter Filename to be Saved:"));
      checkerImagePanel.add(fileName);
      
      
      int[][] colorArray = new int[][]{{color1.getRed(), color1.getGreen(), color1.getBlue()}, 
        {color2.getRed(), color2.getGreen(), color2.getBlue()}};
      
      int result = JOptionPane.showConfirmDialog(null, 
              checkerImagePanel, "Enter Checker Board Preferences", JOptionPane.OK_CANCEL_OPTION);
      
      if (result == JOptionPane.OK_OPTION) {
        try {
          int checker = Integer.parseInt(checkerCount.getText());
          int square = checkImageHeightInput(squareSize.getText());
          String savedFileName = "saved-generated-image/" + fileName.getText();
          if (!savedFileName.endsWith(".png")) {
            savedFileName += ".png";
          }
          f.generateCheckerBoard(savedFileName, colorArray, checker, square);
        } catch (IllegalArgumentException e) {
          displayMessage("Please revise your input: " + e.getMessage());
        }
      }
    });
    
    // keyboard event
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      
      }
      
      @Override
      public void keyPressed(KeyEvent e) {
        //
        if (e.isControlDown() && e.getKeyChar() != 'a' && e.getKeyCode() == 65) {
          System.out.println("control + A pressed");
        }
        
        if (e.isControlDown() && e.getKeyChar() != 'o' && e.getKeyCode() == 79) {
          fileChooser = new JFileChooser(fileFolder, FileSystemView.getFileSystemView());
          int r = fileChooser.showOpenDialog(null);
          if (r == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(fileChooser.getCurrentDirectory());
            fileFolder = fileChooser.getCurrentDirectory();
            f.openFiles(filePath);
            menuSaveFile.setEnabled(true);
          }
          menuOpenFolder.setEnabled(true);
          reEnableEdits();
          disableScriptEdit();
        }
        
        if (e.isControlDown() && e.getKeyChar() != 's' && e.getKeyCode() == 83) {
          if (menuSaveFile.isArmed()) {
            fileChooser = new JFileChooser(fileFolder, FileSystemView.getFileSystemView());
            int r = fileChooser.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
              String savedPath = fileChooser.getSelectedFile().getAbsolutePath();
              if (!savedPath.endsWith(".jpg")) {
                savedPath += ".jpg";
              }
              f.saveFiles(savedPath);
            }
          }
        }
        
        if (e.isControlDown() && e.getKeyChar() != 'q' && e.getKeyCode() == 81) {
          f.exitProgram();
        }
        
        if (e.isControlDown() && e.getKeyCode() == 49) {
          if (menuMosaic.isArmed()) {
            String seeds =
                    JOptionPane.showInputDialog("Please a seed number for mosaic:", 1000);
            System.out.println(seeds);
            f.applyMosaic(seeds);
          }
        }
        
        if (e.isControlDown() && e.getKeyCode() == 50) {
          if (menuBlur.isArmed()) {
            f.applyBlur();
          }
          System.out.println("control + 2 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 51) {
          if (menuSharpen.isArmed()) {
            f.applySharpen();
          }
          System.out.println("control + 3 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 52) {
          if (menuDither.isArmed()) {
            f.applyDither();
            
          }
          System.out.println("control + 4 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 53) {
          if (menuSepia.isArmed()) {
            f.applySepia();
            
          }
          System.out.println("control + 5 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 54) {
          if (menuGrayscale.isArmed()) {
            f.applyGrayScale();
          }
          System.out.println("control + 6 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 55) {
          if (menuCrop.isArmed()) {
            menuCrop.doClick();
          }
          System.out.println("control + 7 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 56) {
          if (menuEdgeDetect.isArmed()) {
            menuEdgeDetect.doClick();
          }
          System.out.println("control + 8 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 57) {
          if (menuContrastEnhance.isArmed()) {
            menuContrastEnhance.doClick();
          }
          System.out.println("control + 9 pressed");
        }
        
        if (e.isControlDown() && e.getKeyCode() == 48) {
          if (menuGenerateFlag.isArmed()) {
            menuGenerateFlag.doClick();
          }
          System.out.println("control + 0 pressed");
        }
        
        if (e.isControlDown() && e.getKeyChar() != 'b' && e.getKeyCode() == 66) {
          if (menuGenerateRainbowFlag.isArmed()) {
            menuGenerateRainbowFlag.doClick();
          }
          System.out.println("control + b pressed");
        }
        
        if (e.isControlDown() && e.getKeyChar() != 'd' && e.getKeyCode() == 68) {
          if (menuGenerateCheckBoard.isArmed()) {
            menuGenerateCheckBoard.doClick();
          }
          System.out.println("control + d pressed");
        }
        
        
      }
      
      @Override
      public void keyReleased(KeyEvent e) {
      
      }
    });
  }
  
  @Override
  public void updateImage(BufferedImage newImg) throws NullPointerException {
    
    // update the current image
    img = newImg;
    imgCrop = newImg;
    imgCopy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    
    imagePanel.remove(imageLabel);
    //imageLabel = new JLabel(new ImageIcon(imgCopy));
    imageLabel = new JLabel(new ImageIcon(img));
    
    mainMouseListenerActive = true;
    imageLabel.addMouseMotionListener(new MainImageMouseMotionListener());
    //blImage = new BoxLayout(imagePanel, BoxLayout.X_AXIS);
    
    //imagePanel.setLayout(blImage);
    imagePanel.add(imageLabel, BorderLayout.CENTER);
    imagePanel.revalidate();
    imagePanel.repaint();
    scrollPane.repaint();
    this.repaint();
    this.pack();
  }
  
  @Override
  public void displayMessage(String msg) {
    JOptionPane.showMessageDialog(this, msg);
  }
  
  private void reEnableEdits() {
    menuMosaic.setEnabled(true);
    menuBlur.setEnabled(true);
    menuSharpen.setEnabled(true);
    menuDither.setEnabled(true);
    menuSepia.setEnabled(true);
    menuGrayscale.setEnabled(true);
    menuCrop.setEnabled(true);
    menuGenerateFlag.setEnabled(true);
    menuGenerateRainbowFlag.setEnabled(true);
    menuGenerateCheckBoard.setEnabled(true);
    menuContrastEnhance.setEnabled(true);
    menuEdgeDetect.setEnabled(true);
    menuRevert.setEnabled(true);
    menuRotateClockWise.setEnabled(true);
    menuRotateCounterClockWise.setEnabled(true);
    menuEditFlipHorizontal.setEnabled(true);
    menuEditFlipVertical.setEnabled(true);
  }
  
  private void disableEdits() {
    menuMosaic.setEnabled(false);
    menuBlur.setEnabled(false);
    menuSharpen.setEnabled(false);
    menuDither.setEnabled(false);
    menuSepia.setEnabled(false);
    menuGrayscale.setEnabled(false);
    menuCrop.setEnabled(false);
    menuGenerateFlag.setEnabled(false);
    menuGenerateRainbowFlag.setEnabled(false);
    menuGenerateCheckBoard.setEnabled(false);
    menuContrastEnhance.setEnabled(false);
    menuEdgeDetect.setEnabled(false);
    menuRevert.setEnabled(false);
    menuRotateClockWise.setEnabled(false);
    menuRotateCounterClockWise.setEnabled(false);
    menuEditFlipHorizontal.setEnabled(false);
    menuEditFlipVertical.setEnabled(false);
  }
  
  private void disableScriptEdit() {
    scriptText.setText("Enable Script Editing in the \"Edit\" menu" + System.lineSeparator()
            + "Note the script editing does not currently support Enhance and Generate options.");
    scriptText.setEnabled(false);
    textPanel.setEnabled(false);
    buttonApplyFilter.setEnabled(false);
  }
  
  private int checkImageHeightInput(String input) throws IllegalArgumentException {
    
    int output = 0;
    
    try {
      output = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Input must be a number");
    }
    
    if (output < 50) {
      throw new IllegalArgumentException("Flag height must be larger than 50!");
    }
    
    return output;
  }
  
  private class CropImageBoxMouseListener implements MouseMotionListener {
    
    private Point start = new Point();
    
    @Override
    public void mouseMoved(MouseEvent e) {
      if (cropMouseListenerActive) {
        int[] offset = getWindowOffset();
        start = e.getPoint();
        //start = new Point((int)(start.getX() - offset[0]),(int)(start.getY() - offset[1]));
        //repaintImg(img, imgCopy);
        repaintImg(imgCrop, imgCropCopy);
        screenCropLabel.repaint();
        //imageLabel.repaint();
        selectionLabel.setText("Point: " + start);
      }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
      if (cropMouseListenerActive) {
        int[] offset = getWindowOffset();
        Point end = e.getPoint();
  
        captureRect = new Rectangle(start,
                new Dimension(end.x - start.x, end.y - start.y));
  
        //repaintImg(img, imgCopy);
        repaintImg(imgCrop, imgCropCopy);
        screenCropLabel.repaint();
        //imageLabel.repaint();
        selectionLabel.setText("Selected area: " + captureRect);
      }
    }
    
    private int[] getWindowOffset() {
      
      int offsetXcoord = (imagePanel.getWidth() - img.getWidth()) / 2;
      int offsetYcoord = (imagePanel.getHeight() - img.getHeight() - 10) / 2;
      
      return new int[]{offsetXcoord, offsetYcoord};
    }
  }
  
  private class MainImageMouseMotionListener implements MouseMotionListener {
    
    private Point start = new Point();
    
    @Override
    public void mouseDragged(MouseEvent e) {
    
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
      if (mainMouseListenerActive) {
        int[] offset = getWindowOffset();
        start = e.getPoint();
        start = new Point((int) (start.getX() - offset[0]), (int) (start.getY() - offset[1]));
        mainMousePosition.setText("Point: " + start);
      }
    }
    
    private int[] getWindowOffset() {
      
      int offsetXcoord = (imagePanel.getWidth() - img.getWidth()) / 2;
      int offsetYcoord = (imagePanel.getHeight() - img.getHeight() - 10) / 2;
      
      return new int[]{offsetXcoord, offsetYcoord};
    }
  }
  
  // create a method to "repaint the box" as the mouse drag on
  private void repaintImg(BufferedImage original, BufferedImage copy) {
    Graphics2D g = copy.createGraphics();
    
    g.drawImage(original, 0, 0, null);
    
    if (captureRect != null) {
      g.setColor(Color.RED);
      g.draw(captureRect);
      g.setColor(new Color(255, 255, 255, 50));
      g.fill(captureRect);
    }
    
    g.dispose();
  }
  
}
