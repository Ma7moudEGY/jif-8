package core;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import java.util.function.Consumer;

public class Keyboard {

    private final boolean[] keys = new boolean[16];
    // Specific state for FX0A instruction
    private boolean isFx0aWaiting = false;
    private Integer fx0aPressedKey = null;

    public Keyboard() {
        initialize();
    }

    public void initialize() {
        java.util.Arrays.fill(keys, false);
        isFx0aWaiting = false;
        fx0aPressedKey = null;
    }

    public boolean isKeyPressed(int key) {
        return keys[key & 0xF];
    }

    public void setKeyPressed(int key, boolean pressed) {
        keys[key & 0xF] = pressed;
    }

    public boolean isWaitingForKey() {
        return isFx0aWaiting;
    }

    // Called by KeyInstructionFX0A to start waiting
    public void startWaitingForKey() {
        this.isFx0aWaiting = true;
        this.fx0aPressedKey = null; // Clear any previously stored key
    }

    // Called by KeyInstructionFX0A to get the pressed key
    public Integer getPressedKeyAndStopWaiting() {
        if (fx0aPressedKey != null) {
            Integer key = fx0aPressedKey;
            this.isFx0aWaiting = false;
            this.fx0aPressedKey = null;
            return key;
        }
        return null;
    }

    public void handleKeyPressed(KeyEvent event) {
        Integer key = mapToChip8Key(event.getCode());
        if (key != null) {
            setKeyPressed(key, true);
            if (isFx0aWaiting && fx0aPressedKey == null) { // Capture the first key press if FX0A is waiting
                fx0aPressedKey = key;
            }
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        Integer key = mapToChip8Key(event.getCode());
        if (key != null) {
            setKeyPressed(key, false);
        }
    }

    private Integer mapToChip8Key(KeyCode code) {
        switch (code) {
            case DIGIT1: return 0x1;
            case DIGIT2: return 0x2;
            case DIGIT3: return 0x3;
            case DIGIT4: return 0xC;
    
            case Q: return 0x4;
            case W: return 0x5;
            case E: return 0x6;
            case R: return 0xD;
    
            case A: return 0x7;
            case S: return 0x8;
            case D: return 0x9;
            case F: return 0xE;
    
            case Z: return 0xA;
            case X: return 0x0;
            case C: return 0xB;
            case V: return 0xF;
    
            default: return null;
        }
    }
}