package core.instruction.instructions;

import core.instruction.Instruction;

public class RsbInstruction8XY7 extends Instruction {
    private final int registerX;
    private final int registerY;

    public RsbInstruction8XY7(int rx, int ry) {
        this.registerX = rx;
        this.registerY = ry;
    }

    @Override
    public void execute() {
        // Implementation will set VX to VY - VX, set VF = NOT borrow
        int valueX = cpu.getRegisters().getRegister(registerX) & 0xFF; // Ensure VX is 0-255
        int valueY = cpu.getRegisters().getRegister(registerY) & 0xFF; // Ensure VY is 0-255

        cpu.getRegisters().setRegister(0xF, (valueY >= valueX) ? 1 : 0); // VF = 1 if VY >= VX (no borrow), 0 otherwise
        cpu.getRegisters().setRegister(registerX, valueY - valueX); // Result is masked by setRegister
    }

    @Override
    public String toString() {
        return String.format("SUBN V%X, V%X", registerX, registerY);
    }
}