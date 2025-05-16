package core.instruction.instructions;

import core.instruction.Instruction;

public class ShlInstruction8X0E extends Instruction {
    private final int register;

    public ShlInstruction8X0E(int register) {
        this.register = register;
    }

    @Override
    public void execute() {
        // Implementation will shift register left, bit 7 goes to VF
        int valueVx = cpu.getRegisters().getRegister(register) & 0xFF; // Ensure VX is 0-255
        cpu.getRegisters().setRegister(0xF, (valueVx & 0x80) >> 7); // MSB of VX (0 or 1) to VF
        cpu.getRegisters().setRegister(register, valueVx << 1);     // VX = VX << 1 (result masked by setRegister)
    }

    @Override
    public String toString() {
        return String.format("SHL V%X", register);
    }
}