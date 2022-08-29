# LPile Post-Process Tool
Author: Daniel Bi (daniel.bi@kpff.com or danielyhbi@gmail.com)
Extends technical support up to July 2025

## Note to Github Reader
I upload this code to demonstrate a side project I did at work. Where I refuse to spend hours looking, copy/pasting
and formatting stuff from a .txt file to excel and then transfer to another structural program. 

The macro is intended to run under Windows environment. I upload a sample output just to demonstrate what it looks like.

I wrote this script under my employment at KPFF special projects division. **All other usage shall be authorized by them.**

_Module 1 shows the bread and butter of my code, module 2 is a bunch of Macro stuff to make my life easier._

## Functionality:
The purpose of this macro is to extract py spring data at all of the computed depth
from the LPile output txt file.
  
## Version History:
- v0.7    2022-02-21      First working build. Proof of concept.
- v0.8    2022-03-16      Frst alpha version. Functional UI. Estalish data structure. Working PYSpring.
- v0.9    2022-04-19      Ability to populate database entries for SAP to import. User can now add modifiers based on depth
- v1.0    2022-04-20      First beta build:
    - UI Improvements: User now able to input preferences in creating SAP import
    - Ability to rename directions
- v1.01     2022-04-22      Minor fixes and more instruction on how to use the spreadsheet
- v1.02     2022-04-28      Fix error upon exit choose file without selecting a file
   -                       Provided System Status
- v1.1      2022-05-12      Included Preference Override: User can exclude springs and customize names
   -                       Include option to have no dividers for stock names
- v1.2      2022-05-21      Modified SAP output (one property definition per link)
   -                       Check file path validity before runs
- v1.2.1    2022-05-25      Minor UI Improvements
- v1.3      2022-07-23      Code Efficiency Improvements (Finite State Machine)
   -                        Output print py-spring if needed (runs 50% faster)
- v1.4      2022-08-12      UI Improvements


## Data Structure:
An Arraylist of Arraylists. See subroutine "ReadLineNew"

Time complexity: O(n + n) - linear processing time to read and generate negative values
                 O(2n)    - output springs (both positive and negative)
Space complexity O(2n)    - no constant storage

## Note from Daniel to Engineers:
There's no fix reference such as "read the line after XXX rows". You should know the fundamental
of loops, logics, and data structures before modifying the code.

I will comment as much as I can but feel free to contact me for any questions!

TODO:
  Check input to make sure it is a LPile output file
  Process battered piles