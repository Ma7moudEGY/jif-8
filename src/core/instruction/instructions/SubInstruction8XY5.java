package core.instruction.instructions;

import core.instruction.Instruction;

public class SubInstruction8XY5 extends Instruction {
    private final int registerX;
    private final int registerY;

    public SubInstruction8XY5(int rx, int ry) {
        this.registerX = rx;
        this.registerY = ry;
    }

    @Override
    public void execute() {
        // Implementation will subtract VY from VX, set VF = NOT borrow
        int valueX = cpu.getRegisters().getRegister(registerX) & 0xFF; // Ensure VX is 0-255
        int valueY = cpu.getRegisters().getRegister(registerY) & 0xFF; // Ensure VY is 0-255

        cpu.getRegisters().setRegister(0xF, (valueX >= valueY) ? 1 : 0); // VF = 1 if VX >= VY (no borrow), 0 otherwise
        cpu.getRegisters().setRegister(registerX, valueX - valueY); // Result is masked by setRegister
    }

    @Override
    public String toString() {
        return String.format("SUB V%X, V%X", registerX, registerY);
    }
}