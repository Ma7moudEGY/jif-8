package core.instruction.instructions;

import core.instruction.Instruction;

public class SSoundInstructionFX18 extends Instruction {
    private final int registerX; // VX, where X is from opcode 0xFX18

    public SSoundInstructionFX18(int registerX) {
        this.registerX = registerX;
    }

    @Override
    public void execute() {
        int value = cpu.getRegisters().getRegister(registerX);
        cpu.setSoundTimer(value);
    }

    @Override
    public String toString() {
        return String.format("SSOUND V%X", registerX);
    }
}
