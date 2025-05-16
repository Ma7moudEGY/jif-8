package core.instruction.instructions;

import core.instruction.Instruction;

public class FontInstructionFX29 extends Instruction {
    private final int register;

    public FontInstructionFX29(int register) {
        this.register = register;
    }

    @Override
    public void execute() {
        int digit = cpu.getRegisters().getRegister(register) & 0x0F; // Get lower 4 bits (0–F)
        // Font set is loaded starting at 0x050 in memory. Each character is 5 bytes long.
        // So, address for digit 'd' is 0x050 + (d * 5).
        cpu.setI((char) (0x50 + (digit * 5))); 
    }

    @Override
    public String toString() {
        return String.format("FONT V%X", register);
    }
}