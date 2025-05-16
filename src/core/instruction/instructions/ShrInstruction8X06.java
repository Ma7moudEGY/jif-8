package core.instruction.instructions;

import core.instruction.Instruction;

public class ShrInstruction8X06 extends Instruction {
    private final int register;

    public ShrInstruction8X06(int register) {
        this.register = register;
    }

    @Override
    public void execute() {
        // Implementation will shift register right, bit 0 goes to VF
        int valueVx = cpu.getRegisters().getRegister(register) & 0xFF; // Ensure VX is 0-255
        cpu.getRegisters().setRegister(0xF, valueVx & 0x1);       // LSB of VX to VF
        cpu.getRegisters().setRegister(register, valueVx >> 1);    // VX = VX >> 1 (result masked by setRegister)
    }

    @Override
    public String toString() {
        return String.format("SHR V%X", register);
    }
}