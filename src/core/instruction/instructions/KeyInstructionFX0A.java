package core.instruction.instructions;

import core.instruction.Instruction;

public class KeyInstructionFX0A extends Instruction {
    private final int register;

    public KeyInstructionFX0A(int register) {
        this.register = register;
    }

    @Override
    public void execute() {
        Integer keyPressed = cpu.getKeyboard().getPressedKeyAndStopWaiting();
        if (keyPressed != null) {
            cpu.getRegisters().setRegister(register, keyPressed);
            // PC will be incremented by CPU.cycle() because pcShouldIncrement is true by default
            System.out.println("FX0A: Key " + Integer.toHexString(keyPressed).toUpperCase() + " pressed, stored in V" + Integer.toHexString(register).toUpperCase());
        } else {
            // No key has been pressed yet (or one was pressed but not yet processed by a previous FX0A cycle)
            cpu.getKeyboard().startWaitingForKey(); // Ensure keyboard knows we are waiting
            cpu.preventPCIncrement(); // Halt PC advancement, so this instruction re-executes
            // System.out.println("FX0A: Waiting for key press for V" + Integer.toHexString(register).toUpperCase() + "..."); // Removed for performance
        }
    }

    @Override
    public String toString() {
        return String.format("KEY V%X, K", register);
    }
}
