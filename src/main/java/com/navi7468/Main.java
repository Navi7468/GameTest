package com.navi7468;

import com.navi7468.util.Settings;
import org.lwjgl.glfw.GLFW;

public class Main {
    private Settings settings;
    private int windowWidth;
    private int windowHeight;
    private Renderer renderer;
    private GameLogic gameLogic;

    public enum ReturnType {
        SHORT,
        INT
    }

    public void run() {
        settings = new Settings("src/main/resources/config.properties");
        windowWidth = settings.windowWidth;
        windowHeight = settings.windowHeight;

        renderer = new Renderer(settings);
        renderer.init();

        // Enable VSync
        GLFW.glfwSwapInterval(1);

        gameLogic = new GameLogic(settings, renderer.getWindow(), renderer.getCamera(), renderer.getUiHandler(), renderer.getTextInputHandler());

        gameLoop();

        GLFW.glfwTerminate();
    }

    private void gameLoop() {
        double previousTime = GLFW.glfwGetTime();
        double accumulator = 0.0;

        while (!GLFW.glfwWindowShouldClose(renderer.getWindow())) {
            double currentTime = GLFW.glfwGetTime();
            double deltaTime = currentTime - previousTime;
            previousTime = currentTime;
            accumulator += deltaTime;

            gameLogic.update(deltaTime); // Update game logic every frame

            while (accumulator >= GameLogic.TIME_PER_TICK) {
                gameLogic.fixedUpdate(); // Run fixed game logic at a fixed rate
                accumulator -= GameLogic.TIME_PER_TICK;
            }

            renderer.render(gameLogic, deltaTime); // Render as often as possible

            GLFW.glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
