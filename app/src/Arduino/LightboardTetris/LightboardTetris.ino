/*
 * Modified version of https://github.com/adafruit/RGB-matrix-Panel project.
 * This Project has been modified to take the commands from an Android tablet 
 * It also utilizes this library https://github.com/adafruit/Adafruit-GFX-Library
 *
 */

//////////////////////
// Library Includes //
//////////////////////
#include <Adafruit_GFX.h>   // Core graphics library
#include <RGBmatrixPanel.h> // Hardware-specific library
#include <Usb.h> //library to use the USB port
#include <AndroidAccessory.h> // Android library
#include "bitmap.h"

/////////////////////
// Hardware Hookup //
/////////////////////
// R0, G0, B0, R1, G1, B1 should be connected to pins 
// 2, 3, 4, 5, 6, and 7 respectively. Their pins aren't defined,
// because they're controlled directly in the library. These pins
// can be moved (somewhat):
#define OE  9
#define LAT 10
#define A   A0
#define B   A1
#define C   A2
#define D   A3 // Comment this line out if you're using a 32x16
// CLK can be moved but must remain on PORTB(8, 9, 10, 11, 12, 13)
#define CLK 11  // MUST be on PORTB!

////////////
// Colors //
////////////
#define ANDROID_BLACK  -1
#define ANDROID_GREEN   0
#define ANDROID_RED     1
#define ANDROID_BLUE    2
#define ANDROID_YELLOW  3
#define ANDROID_PURPLE  4

#define ARDUINO_BLACK  -1
#define ARDUINO_GREEN   0
#define ARDUINO_RED     1
#define ARDUINO_BLUE    2
#define ARDUINO_YELLOW  3
#define ARDUINO_PURPLE  4

//Message size
#define MSG_SIZE 2050

//Connection to Tablet
AndroidAccessory acc("Manufacturer",
    "Model",
    "Description",
    "1.0",
    "cpre388.iastate.edu",
                "0000000012345678");

// For 32x32 LED panels:
RGBmatrixPanel matrix(A, B, C, D, CLK, LAT, OE, false); // 32x32


int8_t cursorX = 0;  // Cursor x position, initialize left
int8_t cursorY = 0;  // Cursor y position, initialize top
int16_t color = 0; // Keep track of color under cursor
int8_t red = 0;   // Red paint value 
int8_t blue = 7;  // Blue paint value
int8_t green = 0; // Green paint value

// The setup() function initializes the rgb matrix, and clears the 
// screen. It also starts the Serial connection.
void setup()
{
  matrix.begin();       // Initialize the matrix.
  blankEasel();         // Blank screen
  Serial.begin(115200);   // Start serial
  acc.powerOn();        // Start up android app

//  drawDot();
  color = matrix.Color333(7,0,0);
//  matrix.drawPixel(20, 30, color);
//  matrix.fillScreen(color);
//  Serial.print(matrix.Color333(7,0,0), HEX);
}

void loop()
{
  byte msg[MSG_SIZE];
  if(acc.isConnected())
  {
    int len = acc.read(msg, sizeof(msg), 1);
    if (len > 0)
    {
////      Serial.print((int)msg[0], DEC);
//      Serial.print(msg[0], HEX);
//      Serial.print("\n");
//      Serial.print(msg[1], HEX);
//      Serial.print("\n");
//      matrix.fillScreen(color);
      int cols = (int) msg[0];
      int rows = (int) msg[1];
//      Serial.print(cols, HEX);
//      Serial.print("\n");
//      Serial.print(rows, HEX);
//      Serial.print("\n");
//      matrix.drawPixel(0,0,color);
      for (int j = 0; j < rows/*MSG_SIZE/64*/; j++)
      {
        for (int i = 0; i < cols/*MSG_SIZE/64*/; i++)
        {
//          int loc = j*64+i*2;
          int loc = j*cols*2+i*2+2;
          color = (msg[loc] << 8) | (msg[loc+1]);
//          Serial.print(color, HEX);
          matrix.drawPixel(i,j,color);
        }
      }
//      Serial.print("\n");
    }
  }
}

// Draws active color at (cursorX, cursorY).
void drawDot()
{
  color = matrix.Color333(red, green, blue);
  matrix.drawPixel(cursorX, cursorY, color);
}

// loadBitmap loads up a stored bitmap into the matrix's data buffer.
void loadBitmap()
{
  uint8_t *ptr = matrix.backBuffer(); // Get address of matrix data
  memcpy_P(ptr, bmp, sizeof(bmp));
}

// Reset the screen. Set cursors back to top-left. Reset oldColor
// and blank the screen.
void blankEasel()
{
  cursorX = 0;
  cursorY = 0;
  color = 0;
  matrix.fillScreen(0); // Blank screen
}
