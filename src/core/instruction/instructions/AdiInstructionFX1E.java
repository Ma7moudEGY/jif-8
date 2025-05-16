package core.instruction.instructions;

import core.instruction.Instruction;

public class AdiInstructionFX1E extends Instruction {
    private final int register;

    public AdiInstructionFX1E(int register) {
        this.register = register;
    }

    @Override
    public void execute() {
        int valueVx = cpu.getRegisters().getRegister(register) & 0xFF; // VX is 8-bit
        int valueI = cpu.getI() & 0xFFF; // I is 12-bit for addressing

        int sum = valueI + valueVx;

        // According to Cowgod's spec: "VF is set to 1 when range overflow (I+VX>0xFFF), and 0 when not."
        // This refers to the 12-bit nature of I.
        if (sum > 0xFFF) {
            cpu.getRegisters().setRegister(0xF, 1);
        } else {
            cpu.getRegisters().setRegister(0xF, 0);
        }
        cpu.setI((char)sum); // cpu.setI() will mask the result to 0xFFF
    }

    @Override
    public String toString() {
        return String.format("ADI V%X", register);
    }
}
