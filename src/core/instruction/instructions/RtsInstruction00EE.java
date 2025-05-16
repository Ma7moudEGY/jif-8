package core.instruction.instructions;

import core.instruction.Instruction;

public class RtsInstruction00EE extends Instruction {
    public RtsInstruction00EE() {
        // Constructor logic if needed
    }

    public void execute() {
        // Logic to return from a subroutine
        int returnAddress = cpu.getStack().pop();
        cpu.setPC(returnAddress); // setPC also sets pcShouldIncrement = false
    }

    @Override
    public String toString() {
        return "RTS";
    }
}
