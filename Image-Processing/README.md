# Image Processing
This image processing app provides a graphical user interface to edit your images.

Implemented with Java Swing, with full Model-Control-View (MVC) design.

* Author: Daniel Bi (danielyhbi@gmail.com)
* Date: 9/27/2022

## List of Features
9/27/2022 update - For Public and Interested Professionals/Recruiters:
- Improved UI (Eliminated the script interface + minor UI improvements)
- Add feature to rotate, and flip image
- Generated image will be saved in folder `/saved-generated-image`

5/31/2022 update:
- Improved mosaic processing time (O(n log(n))). Implemented k-d tree to store seeds value.

5/4/2022 update - For Final Project Submission in CS5004:
- New filter added: Sobel Edge detection, Contrast Enhance (histogram equalization)
- Generate county flags, rainbow flags, and check board patterns without loading a file
- Crop image
- Revert image to original condition
- Display pointer's X and Y position on an image

4/17 update: List of filter/effect:
- Blur
- Sharpen
- Grayscale
- Sepia
- Dither
- Mosaic

All filters can be added on top of each other. 

## Limitation

* There's no un-do button. but there is a "revert to original image" option.
* Will not automatically resize large image
* Note: Script editing on the new features are no longer supported

## How to run

GUI: Run the file at: `/imageview/MainViewDriver.java`

## Citations

The two images I used on homework 8 were all taken by myself.

One photo portraits the Manhattan Bridge at night [1] where I photograph the view during a visit to NYC in an evening. The other one captures the scene where I had ice cream with a few friends [2] one evening near Wallingford.

I fully authorize the use of my own photos for CS5004 homework 8, and I gathered the consents from my friends in the photo for this project.

All the images shall not be reused or retransmitted by others for any purposes.

Readme was written based on the syntax from the Markdown Guide[3].

Citations:  
[1] D. Bi, *"NY Manhattan Bridge"*, October 30, 2021. Brooklyn, New York, USA. Accessed on: Match 25, 2022.

[2] D. Bi, *"Ice Cream with Friends"*, August 24, 2021. Seattle, WA, USA. Accessed on: Match 25, 2022.

[3] "Basic Syntax | Markdown Guide", Markdownguide.org, 2022. [Online]. Available: https://www.markdownguide.org/basic-syntax/. [Accessed: 02- Apr- 2022].