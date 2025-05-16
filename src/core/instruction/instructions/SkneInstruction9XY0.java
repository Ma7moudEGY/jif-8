package core.instruction.instructions;

import core.instruction.Instruction;

public class SkneInstruction9XY0 extends Instruction {
    private final int registerX;
    private final int registerY;

    public SkneInstruction9XY0(int rx, int ry) {
        this.registerX = rx;
        this.registerY = ry;
    }

    @Override
    public void execute() {
        // Implementation will skip next instruction if VX not equals VY
        int valueX = cpu.getRegisters().getRegister(registerX);
        int valueY = cpu.getRegisters().getRegister(registerY);

        if (valueX != valueY) {
            cpu.skipNextInstruction();
        }
    }

    @Override
    public String toString() {
        return String.format("SKNE V%X, V%X", registerX, registerY);
    }
}