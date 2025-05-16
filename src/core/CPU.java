package core;

import java.util.Random;

import core.instruction.Instruction;

// fetch -> decode -> execute

public class CPU {
    private Registers registers;
    private int I;
    private int PC;
    private Stack stack;
    private byte delayTimer;
    private byte soundTimer;
    private Memory memory;
    private Display display;
    private Keyboard keyboard;
    private SoundSystem soundSystem;
    private Decoder decoder;
    private Executer executer;
    private final Random randomGenerator;
    private boolean soundWasPlaying; // To manage sound state transitions

    public CPU(Memory memory, Stack stack, Display display, Keyboard keyboard, SoundSystem soundSystem) {
        registers = new Registers();

        this.stack = stack;
        this.memory = memory;
        this.display = display;
        this.keyboard = keyboard;
        this.soundSystem = soundSystem;
        decoder = new Decoder(this);
        executer = new Executer();
        randomGenerator = new Random();
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
        memory.reset(); // This should also call loadFontSet() if Memory.reset() clears everything
        display.reset();
        soundSystem.stopSound();
        soundWasPlaying = false;
    }

    public void cycle() {
        char opcode = fetch(); // PC is at the current instruction

        // Advance PC to the *next* instruction before execution.
        // This simplifies PC handling for jumps, calls, and skips within execute().
        // Jumps/Calls will set PC to target.
        // Skips will add 2 more to PC (which is already advanced).
        // Regular instructions don't touch PC.
        int currentPCOnFetch = PC; // For logging
        PC = (PC + 2) & 0xFFF;     // Advance PC for next cycle (unless execute changes it)

        Instruction decodedInstruction = decoder.decode(opcode);
        System.out.printf("EXECUTE: PC_fetched_at=0x%03X, Opcode=0x%04X, I: 0x%03X, Instruction: %s\n",
                currentPCOnFetch, (int) opcode, (int) I, decodedInstruction.toString());
        
        executer.execute(decodedInstruction);
        // Note: Instructions like JMP, CALL, or conditional skips (if true)
        // should modify the PC directly within their execute() method.
        // For JMP/CALL, they set PC to the target address.
        // For SKIPS, they would increment PC by another 2 if condition is met.

        System.out.println("PC after execute: " + String.format("0x%03X", PC));
        updateTimers();
    }

    private void updateTimers() {
        if (delayTimer > 0) {
            delayTimer--;
        }

        boolean soundShouldBeActive = soundTimer > 0;

        if (soundTimer > 0) {
            soundTimer--;
            if (soundTimer == 0) { // Timer just expired
                soundShouldBeActive = false;
            }
        }

        if (soundShouldBeActive && !soundWasPlaying) {
            soundSystem.playSound(); // Start sound if it just became active
        } else if (!soundShouldBeActive && soundWasPlaying) {
            soundSystem.stopSound(); // Stop sound if it just became inactive
        }
        soundWasPlaying = soundShouldBeActive;
    }

    public char fetch() {
        if (PC < 0 || PC + 1 >= Memory.getMemorySize()) {
            throw new RuntimeException(String.format("Program Counter out of bounds during fetch: 0x%03X", PC));
        }
        char high = (char) (memory.read(PC) & 0xFF);
        char low = (char) (memory.read(PC + 1) & 0xFF);
        char op = (char) ((high << 8) | low);
        System.out.printf("FETCH: PC=0x%03X, Opcode=0x%04X\n", PC, (int) op);
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
        PC = value & 0xFFF;
    }

    public byte getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(byte value) {
        delayTimer = value;
    }

    public byte getSoundTimer() {
        return soundTimer;
    }

    public void setSoundTimer(byte value) {
        soundTimer = value;
    }

    public byte generateRandomByte() {
        return (byte) randomGenerator.nextInt(256);
    }
}
