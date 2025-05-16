package core.instruction.instructions;

import core.instruction.Instruction;

public class StrInstructionFX55 extends Instruction {
    private final int lastRegister;

    public StrInstructionFX55(int lastRegister) {
        this.lastRegister = lastRegister;
    }

    @Override
    public void execute() {
        for (int i = 0; i <= lastRegister; i++) {
            int value = cpu.getRegisters().getRegister(i);
            cpu.getMemory().write((char)(cpu.getI()+i),(char)value);
        }
    }

    @Override
    public String toString() {
        return String.format("STR V0-V%X", lastRegister);
    }
}
