package core;

import java.util.Random;

import core.instruction.Instruction;

// fetch -> decode -> execute

public class CPU {
    private Registers registers;
    private int I;
    private int PC;
    private Stack stack;
    private int delayTimer;
    private int soundTimer;
    private Memory memory;
    private Display display;
    private Keyboard keyboard;
    private SoundSystem soundSystem;
    private Decoder decoder;
    private Executer executer;
    private boolean pcShouldIncrement = true; // Flag to control PC increment

    public CPU(Memory memory, Stack stack, Display display, Keyboard keyboard, SoundSystem soundSystem) {
        registers = new Registers();

        this.stack = stack;
        this.memory = memory;
        this.display = display;
        this.keyboard = keyboard;
        this.soundSystem = soundSystem;
        decoder = new Decoder(this);
        executer = new Executer();
        reset();
    }

    public Registers getRegisters() {
        return registers;
    }

    public Stack getStack() {
        return stack;
    }

    public Memory getMemory() {
        return memory;
    }

    public Display getDisplay() {
        return display;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public SoundSystem getSoundSystem() {
        return soundSystem;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public Executer getExecuter() {
        return executer;
    }

    public void reset() {
        registers.reset();
        I = 0;
        PC = 0x200;
        stack.reset();
        delayTimer = 0;
        soundTimer = 0;
        memory.reset();
        display.reset();
        memory.loadFontSet(); // Ensure font set is loaded into memory on reset
        soundSystem.stopSound();
    }

    public void cycle() {
        pcShouldIncrement = true; // Default to incrementing PC for the current cycle

        char instructionOpcode = fetch();
        Instruction decodedInstruction = decoder.decode(instructionOpcode);

        // Verbose Logging before execution (can be commented out for performance)
        // System.out.printf("EXECUTE: PC: 0x%03X, I: 0x%03X, Instruction: %s\n", (int) PC, (int) I,
        //         decodedInstruction.toString());

        executer.execute(decodedInstruction);

        if (pcShouldIncrement) {
            PC = (PC + 2) & 0xFFF; // Standard increment, wrap around 4095
        }
        // Verbose Logging for PC (can be commented out for performance)
        // System.out.println("PC: " + PC);
        updateTimers();
    }

    private void updateTimers() {
        if (delayTimer > 0) {
            delayTimer--;
        }
        if (soundTimer > 0) {
            soundSystem.beeb(); // Play sound while timer is active
            soundTimer--;
        } else {
            soundSystem.stopSound(); // Ensure sound is stopped if timer reaches 0 or was already 0
        }
    }

    public char fetch() {
        // Read high byte first, then low byte
        char high = (char) (memory.RAM[PC] & 0xFF);
        char low = (char) (memory.RAM[PC + 1] & 0xFF);
        char op = (char) ((high << 8) | low); 
        // Verbose Logging for fetch (can be commented out for performance)
        // System.out.printf("FETCH: PC=0x%03X, Opcode=0x%04X\n", PC, (int) op);
        return op;
    }

    public int getI() {
        return I;
    }

    public void setI(char value) {
        I = value & 0xFFF;
    }

    public int getPC() {
        return PC;
    }

    public void setPC(int value) {
        PC = value & 0xFFF; // Directly set PC
        pcShouldIncrement = false; // Instruction handled PC advancement for this cycle
    }

    /**
     * Called by skip instructions. They increment PC by 2.
     * The main loop will then add another 2 if pcShouldIncrement is true,
     * effectively skipping one 2-byte instruction (total PC + 4).
     */
    public void skipNextInstruction() {
        PC = (PC + 2) & 0xFFF;
        // pcShouldIncrement remains true, so the main loop adds another 2.
    }

    public void preventPCIncrement() {
        this.pcShouldIncrement = false;
    }

    public int getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(int value) {
        delayTimer = value;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public void setSoundTimer(int value) {
        soundTimer = value;
        if (soundTimer == 0) { // If timer is set to 0, ensure sound stops.
             soundSystem.stopSound();
        }
        // The updateTimers() method will handle playing the sound if soundTimer > 0.
    }

    public byte generateRandomByte() {
        return (byte) new Random().nextInt(256);
    }
}
