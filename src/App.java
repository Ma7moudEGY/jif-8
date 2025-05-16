import core.Emulator;
import scenes.GameScene;
import scenes.WelcomeScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class App extends Application {
    private static Emulator emulator = new Emulator();
    private Timeline gameLoop;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setScene(new WelcomeScene(emulator));

        // Setup global key listeners for the stage
        // These listeners will forward key events to the emulator's keyboard handler.
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (emulator.getCPU() != null && emulator.getCPU().getKeyboard() != null) {
                emulator.getCPU().getKeyboard().handleKeyPressed(event);
            }
        });
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (emulator.getCPU() != null && emulator.getCPU().getKeyboard() != null) {
                emulator.getCPU().getKeyboard().handleKeyReleased(event);
            }
        });

        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        // Game loop aims for roughly 60 FPS for display & timer updates.
        // Multiple CPU cycles will run per KeyFrame.
        KeyFrame kf = new KeyFrame(Duration.millis(16), e -> { // Approx 60 FPS (1000ms / 60 ~= 16.67ms)
            if (emulator.isRunning() && primaryStage.getScene() instanceof GameScene) {
                // Run multiple CPU cycles per frame to achieve desired emulation speed (e.g., ~500Hz)
                // 500Hz / 60FPS ~= 8-9 cycles. Let's use 10 as an example.
                for (int i = 0; i < 10; i++) {
                    emulator.emulateCycle();
                    // If FX0A (wait for key) is active, it will prevent PC advancement,
                    // effectively halting further cycles within this loop iteration if needed.
                    if (emulator.getCPU().getKeyboard().isWaitingForKey()) break;
                }
                ((GameScene) primaryStage.getScene()).updateDisplay();
            }
        });
        gameLoop.getKeyFrames().add(kf);

        primaryStage.setOnCloseRequest(e -> {
            gameLoop.stop();
            Platform.exit();
        });

        primaryStage.sceneProperty().addListener((ob, oldScene, currentScene) -> {
            System.out.println("Scene was changed");
            if (currentScene instanceof GameScene && emulator.isRunning()) {
                System.out.println("GameScene active and emulator running. Starting game loop.");
                gameLoop.play();
            } else {
                System.out.println("Not in GameScene or emulator not running. Stopping game loop.");
                gameLoop.stop(); // Stop gameloop if not in GameScene or emulator stopped
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        // emulator.emulateCycle(); // Removed: Don't run cycle before UI and ROM load
        launch();
    }
}