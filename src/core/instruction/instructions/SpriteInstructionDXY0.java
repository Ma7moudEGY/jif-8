package core.instruction.instructions;

import core.Display;
import core.instruction.Instruction;

public class SpriteInstructionDXY0 extends Instruction {
    private final int registerX;
    private final int registerY;
    private final int height;

    public SpriteInstructionDXY0(int rx, int ry, int height) {
        this.registerX = rx;
        this.registerY = ry;
        this.height = height;
    }

    @Override
    public void execute() {
        cpu.getRegisters().setRegister(0xF, 0); // Reset Collision Flag
        final int pixelWidth = 8; // Sprites are always 8 pixels wide

        Display display = cpu.getDisplay();
        if (display == null) {
            throw new IllegalStateException("Display is not initialized");
        }

        int xCoordinate = cpu.getRegisters().getRegister(registerX) & 0xFF;
        int yCoordinate = cpu.getRegisters().getRegister(registerY) & 0xFF;

        for (int row = 0; row < height; row++) {
            int spriteByte = cpu.getMemory().read( (cpu.getI() + row)); // (cpu.getI() + row) is address

            for (int column = 0; column < pixelWidth; column++) {
                if ((spriteByte & (0x80 >> column)) != 0) {
                    // Wrap coordinates around the screen
                    int screenX = (xCoordinate + column) % display.getWidth();
                    int screenY = (yCoordinate + row) % display.getHeight();

                    boolean currentPixelState = cpu.getDisplay().getPixel(screenX, screenY);

                    if (currentPixelState) { // Collision if drawing on an already set pixel
                        cpu.getRegisters().setRegister(0xF, 1); // Set collision flag
                    }
                    // XOR the pixel state and update the display
                    cpu.getDisplay().setPixel(screenX, screenY, !currentPixelState);
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("SPRITE V%X, V%X, %X", registerX, registerY, height);
    }
}