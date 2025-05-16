package core;

public class Registers {
    private int[] registers;
    private static final int NUM_REGISTERS = 16;

    public Registers() {
        reset();
        System.out.println("Registers initialized");
    }

    public int getRegister(int index) {
        if (index < 0 || index >= registers.length) {
            throw new IllegalArgumentException("Invalid register index");
        }
        return registers[index];
    }

    public void setRegister(int index, int value) {
        if (index < 0 || index >= registers.length) {
            throw new IllegalArgumentException("Invalid register index");
        }
        registers[index] = value & 0xFF; // Ensure value is stored as an 8-bit unsigned integer
    }

    public void reset() {
        registers = new int[NUM_REGISTERS];
    }

    public int getSize() {
        return NUM_REGISTERS;
    }

}
