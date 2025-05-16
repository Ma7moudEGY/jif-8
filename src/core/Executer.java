package core;

import core.instruction.Instruction;

public class Executer {
    public Executer() {
        System.out.println("Executer initialized");
    }

    public void execute(Instruction instruction) {
        // System.out.println("Executing instruction..."); // Verbose log
        instruction.execute();
        // System.out.printf("Instruction: %s\n", instruction.toString()); // Verbose log
    }

}
