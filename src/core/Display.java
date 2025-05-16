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
        clearDrawFlag(); // Or drawFlag = false;
    }

    public void setPixel(int x, int y, boolean value) {
        // Assuming x is horizontal (0 to width-1) and y is vertical (0 to height-1)
        // And pixels array is pixels[x_coord][y_coord] or pixels[col][row]
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[x][y] = value;
        } else {
            // Optionally log or handle out-of-bounds, though CHIP-8 often wraps around.
            // For strictness, current behavior (ignoring) is fine, or throw an error.
        }
    }

    public boolean getPixel(int x, int y) {
        if (x < width && x >= 0 && y < height && y >= 0) {
            return pixels[x][y];
        }
        throw new IndexOutOfBoundsException("Invalid Pixel Coordinates");
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