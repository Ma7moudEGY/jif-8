package core.instruction.instructions;

import core.instruction.Instruction;

public class AddInstruction8XY4 extends Instruction {
    private final int registerX;
    private final int registerY;

    public AddInstruction8XY4(int rx, int ry) {
        this.registerX = rx;
        this.registerY = ry;
    }

    @Override
    public void execute() {
        // Implementation will add VY to VX, set VF = carry
        int valueX = cpu.getRegisters().getRegister(registerX) & 0xFF; // Ensure VX is 0-255
        int valueY = cpu.getRegisters().getRegister(registerY) & 0xFF; // Ensure VY is 0-255

        int sum = valueX + valueY;
        cpu.getRegisters().setRegister(0xF, (sum > 0xFF) ? 1 : 0); // Set VF if carry (sum > 255)
        cpu.getRegisters().setRegister(registerX, sum); // Result is masked to 0xFF by setRegister
    }

    @Override
    public String toString() {
        return String.format("ADD V%X, V%X", registerX, registerY);
    }
}