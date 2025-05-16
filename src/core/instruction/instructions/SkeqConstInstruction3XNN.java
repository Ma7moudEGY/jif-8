package core.instruction.instructions;

import core.instruction.Instruction;

public class SkeqConstInstruction3XNN extends Instruction {
    private final int register;
    private final int value;

    public SkeqConstInstruction3XNN(int register, int value) {
        this.register = register;
        this.value = value;
    }

    @Override
    public void execute() {
        // Implementation will skip next instruction if VX equals NN
        int valueX = cpu.getRegisters().getRegister(register) & 0xFF; // Ensure VX is treated as 0-255
        if (valueX == (value & 0xFF)) { // Ensure NN is treated as 0-255
            cpu.skipNextInstruction(); // Advances PC by 2; main loop will add another 2
        }
    }

    @Override
    public String toString() {
        return String.format("SE V%X, %X", register, value);
    }
}