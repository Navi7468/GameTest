package com.navi7468;

import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;

import com.navi7468.graphics.BitmapFont;
import com.navi7468.graphics.BlockRenderer;
import com.navi7468.ui.TextInputHandler;
import com.navi7468.ui.UIHandler;
import com.navi7468.util.Camera;
import com.navi7468.util.Settings;
import com.navi7468.world.Chunk;
import com.navi7468.world.World;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class Renderer {
    // Window properties
    private long window;
    public int windowWidth;
    public int windowHeight;

    // Game settings
    private Settings settings;
    private Camera camera;

    // UI elements
    private BitmapFont bitmapFont;
    private UIHandler uiHandler;
    private TextInputHandler textInputHandler;

    // Game elements
    private BlockRenderer blockRenderer;
    private Chunk chunk;
    private World world;


    public Renderer(Settings settings) {
        this.settings = settings;
        this.windowWidth = settings.windowWidth;
        this.windowHeight = settings.windowHeight;
    }

    public long getWindow() { return window; }
    public Camera getCamera() { return camera; }
    public UIHandler getUiHandler() { return uiHandler; }
    public TextInputHandler getTextInputHandler() { return textInputHandler; }

    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = GLFW.glfwCreateWindow(windowWidth, windowHeight, "MCTest", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Enable blending for transparency
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Set up a basic projection and model-view matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        camera = new Camera(settings);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        try {
            bitmapFont = new BitmapFont("textures/fonts/font.png", "src/main/resources/textures/fonts/font.fnt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        blockRenderer = new BlockRenderer();
        // chunk = new Chunk();
        NoiseGenerator noise = new NoiseGenerator();
        world = new World(50, 256, noise);

        // Initialize UIHandler
        uiHandler = new UIHandler(bitmapFont, 8.0f);

        // Initialize TextInputHandler
        textInputHandler = new TextInputHandler(bitmapFont);

        // Add initial debug elements
        uiHandler.addElement("Debug Window!");
        uiHandler.addElement("Camera Pos:");
        uiHandler.addElement(""); // Placeholder for camera position
        uiHandler.addElement(""); // Placeholder for camera rotation
        uiHandler.addElement("Chat: Closed");
        uiHandler.addElement("FPS: 0");

        GLFW.glfwSetCursorPos(window, windowWidth / 2, windowHeight / 2);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        // Set up the framebuffer size callback for handling window resizing
        GLFW.glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                windowWidth = width;
                windowHeight = height;
                GL11.glViewport(0, 0, width, height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                camera.setPerspective(settings.cameraFov, (float) width / height, settings.cameraNear, settings.cameraFar);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
            }
        });
    }

    public void render(GameLogic gameLogic, double deltaTime) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Handle camera input and movement
        if (!gameLogic.getTextInputHandler().isActive()) {
            camera.handleInput(window, settings.moveSpeed, settings.mouseSensitivity, deltaTime);
            camera.applyTransformations();
        }

        // 3D rendering setup
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        camera.setPerspective(settings.cameraFov, (float) windowWidth / windowHeight, settings.cameraNear, settings.cameraFar);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        camera.applyTransformations();

        // blockRenderer.renderChunk(chunk, 0, 0, 0);
        for (int x = 0; x < world.getWorldSize(); x++) {
            for (int z = 0; z < world.getWorldSize(); z++) {
                for (int y = 0; y < world.getWorldHeight() / Chunk.CHUNK_HEIGHT; y++) { // Adjust iteration to match chunk height
                    blockRenderer.renderChunk(world, world.getChunk(x, y, z), x * Chunk.CHUNK_SIZE, y * Chunk.CHUNK_HEIGHT, z * Chunk.CHUNK_SIZE);
                }
            }
        }

        // Switch to orthographic projection for 2D rendering
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        // Render UI elements
        uiHandler.render(5, 5, 2.0f);

        // Render text input box if active
        if (textInputHandler.isActive()) {
            textInputHandler.renderTextBox(10, windowHeight - 40, windowWidth - 20, 30, 0.5f); // Adjust the position, size, and alpha as needed
            textInputHandler.renderCurrentInput(15, windowHeight - 30, 2.0f);
        }

        // Render chat history
        textInputHandler.renderChatHistory(10, windowHeight - 100, windowWidth - 20, 60, 0.5f); // Adjust the position, size, and alpha as needed

        // Restore projection and model-view matrices
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GLFW.glfwSwapBuffers(window);
    }
}
