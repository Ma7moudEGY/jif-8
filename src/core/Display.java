package core;

import java.util.Arrays;

public class Display {

    private boolean[][] pixels;
    private static final int width = 64;
    private static final int height = 32;
  
    public boolean drawFlag = false;

    public Display() {
        reset();
    }

    public boolean getDrawFlag() {
        return drawFlag;
    }

    public void setDrawFlag() {
        drawFlag = true;
    }

    public void clearDrawFlag() {
        drawFlag = false;
    }

    public void reset() {
        pixels = new boolean[width][height];
    }

    public void setPixel(int x, int y, boolean value) {
        // Assumes x is 0 to width-1, y is 0 to height-1.
        // Callers (like SpriteInstruction) are responsible for wrapping coordinates.
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[x][y] = value;
        }
    }

    public boolean getPixel(int x, int y) {
        // Assumes x is 0 to width-1, y is 0 to height-1.
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return pixels[x][y];
        }
        return false; // Default for out-of-bounds, should ideally not be hit if caller wraps.
    }

    public boolean[][] getDisplayBuffer() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}