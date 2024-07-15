package com.navi7468;

import com.navi7468.ui.TextInputHandler;
import com.navi7468.ui.UIHandler;
import com.navi7468.util.Camera;
import com.navi7468.util.Settings;
import org.lwjgl.glfw.GLFW;

public class GameLogic {
    private Settings settings;
    private long window;
    private Camera camera;
    private UIHandler uiHandler;
    private TextInputHandler textInputHandler;
    private boolean debugKeyPressed = false;
    private boolean chatKeyPressed = false;

    private int frames = 0;
    private double lastTime = 0.0;
    private double fps = 0.0;

    public static final int TICKS_PER_SECOND = 20;
    public static final double TIME_PER_TICK = 1.0 / TICKS_PER_SECOND;

    public GameLogic(Settings settings, long window, Camera camera, UIHandler uiHandler, TextInputHandler textInputHandler) {
        this.settings = settings;
        this.window = window;
        this.camera = camera;
        this.uiHandler = uiHandler;
        this.textInputHandler = textInputHandler;
    }

    public TextInputHandler getTextInputHandler() {
        return textInputHandler;
    }

    // Runs every frame
    public void update(double deltaTime) {
        // Update UI elements with current camera position and rotation
        uiHandler.updateElement(2, " X: " + camera.getX(Main.ReturnType.SHORT) + " Y: " + camera.getY(Main.ReturnType.SHORT) + " Z: " + camera.getZ(Main.ReturnType.SHORT));
        uiHandler.updateElement(3, " Pitch: " + camera.getPitch(Main.ReturnType.SHORT) + " Yaw: " + camera.getYaw(Main.ReturnType.SHORT));

        frames++;
        double currentTime = GLFW.glfwGetTime();
        if (currentTime - lastTime >= 1.0) {
            fps = frames / (currentTime - lastTime);
            lastTime = currentTime;
            frames = 0;
            uiHandler.updateElement(5, "FPS: " + (int) fps);
        }

        // Toggle debug UI with debug key (default F3)
        if (GLFW.glfwGetKey(window, settings.debugKey) == GLFW.GLFW_PRESS) {
            if (!debugKeyPressed) {
                uiHandler.setVisible(!uiHandler.isVisible());
                debugKeyPressed = true;
            }
        } else {
            debugKeyPressed = false;
        }

        // Toggle text input mode with T key
        if (GLFW.glfwGetKey(window, settings.chatKey) == GLFW.GLFW_PRESS) {
            if (!chatKeyPressed) {
                chatKeyPressed = true;
                if (!textInputHandler.isActive()) {
                    textInputHandler.activate();
                    uiHandler.updateElement(4, "Chat: Open");
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                }
            }
        } else {
            chatKeyPressed = false;
        }

        // Handle camera input and movement
        if (!textInputHandler.isActive()) {
            camera.handleInput(window, settings.moveSpeed, settings.mouseSensitivity, deltaTime);
        }

        // Handle text input
        textInputHandler.handleInput(window);


        // Sprinting
        if (GLFW.glfwGetKey(window, settings.sprintKey) == GLFW.GLFW_PRESS) {
            camera.setSprinting(true);
        } else {
            camera.setSprinting(false);
        }
    }

    // Runs every tick
    public void fixedUpdate() {
        // Any fixed update logic can go here if needed
    }
}
