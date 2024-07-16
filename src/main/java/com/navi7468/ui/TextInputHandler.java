package com.navi7468.ui;

import com.navi7468.Renderer;
import com.navi7468.graphics.BitmapFont;
import com.navi7468.util.RenderUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TextInputHandler {
    private StringBuilder currentInput;
    private List<String> chatHistory;
    private boolean active;
    private boolean[] keyStates;
    private BitmapFont bitmapFont;
    private static final int MAX_KEYS = 349;

    public TextInputHandler(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
        currentInput = new StringBuilder();
        chatHistory = new ArrayList<>();
        active = false;
        keyStates = new boolean[MAX_KEYS];
    }

    public void activate() {
        active = true;
        System.out.println("Text input activated");
    }

    public void deactivate() {
        active = false;
        currentInput.setLength(0);
        System.out.println("Text input deactivated");
    }

    public boolean isActive() {
        return active;
    }

    public String getCurrentInput() {
        return currentInput.toString();
    }

    public List<String> getChatHistory() {
        return chatHistory;
    }

    public void handleInput(long window) {
        if (!active) return;

        // Handle key presses
        for (int key = GLFW.GLFW_KEY_SPACE; key <= GLFW.GLFW_KEY_LAST; key++) {
            if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS) {
                if (!keyStates[key]) {
                    System.out.println("Key pressed: " + key);
                    handleSpecialKeys(window, key);
                    char character = getCharacterForKey(key);
                    if (character != 0) {
                        currentInput.append(character);
                        System.out.println("Character appended: " + character);
                    }
                    keyStates[key] = true;
                }
            } else {
                keyStates[key] = false;
            }
        }
    }

    private void handleSpecialKeys(long window, int key) {
        switch (key) {
            case GLFW.GLFW_KEY_BACKSPACE:
                if (!keyStates[GLFW.GLFW_KEY_BACKSPACE]) {
                    System.out.println("Backspace pressed");
                    if (currentInput.length() > 0) {
                        currentInput.deleteCharAt(currentInput.length() - 1);
                        System.out.println("Current input after backspace: " + currentInput.toString());
                    } else {
                        System.out.println("Current input is empty, nothing to delete");
                    }
                    keyStates[GLFW.GLFW_KEY_BACKSPACE] = true;
                }
                break;
            case GLFW.GLFW_KEY_ENTER:
                if (!keyStates[GLFW.GLFW_KEY_ENTER]) {
                    System.out.println("Enter pressed");
                    if (currentInput.length() > 0) {
                        chatHistory.add(currentInput.toString());
                        System.out.println("Added to chat history: " + currentInput.toString());

                        // TODO: Handle commands here

                        currentInput.setLength(0);
                        System.out.println("Current input cleared after adding to chat history");
                    } else {
                        System.out.println("Current input is empty, nothing to add to chat history");
                    }
                    keyStates[GLFW.GLFW_KEY_ENTER] = true;
                }
                break;
            case GLFW.GLFW_KEY_ESCAPE:
                if (!keyStates[GLFW.GLFW_KEY_ESCAPE]) {
                    System.out.println("Escape pressed");
                    deactivate();
                    System.out.println("Text input deactivated");
                    keyStates[GLFW.GLFW_KEY_ESCAPE] = true;
                }
                break;
        }
    }

    // Helper method to get the character for a given key
    private char getCharacterForKey(int key) {
        if (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) {
            return (char) (key - GLFW.GLFW_KEY_A + 'A');
        } else if (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) {
            return (char) (key - GLFW.GLFW_KEY_0 + '0');
        } else {
            switch (key) {
                case GLFW.GLFW_KEY_SPACE:
                    return ' ';
                case GLFW.GLFW_KEY_PERIOD:
                    return '.';
                case GLFW.GLFW_KEY_COMMA:
                    return ',';
                case GLFW.GLFW_KEY_MINUS:
                    return '-';
                case GLFW.GLFW_KEY_SLASH:
                    return '/';
                default:
                    return 0;
            }
        }
    }

    public void renderTextBox(int x, int y, int width, int height, float alpha) {
        RenderUtil.renderTextBox(x, y, width, height, alpha);
    }

    public void renderCurrentInput(int x, int y, float scale) {
        bitmapFont.renderText("> " + currentInput.toString(), x, y, scale);
    }

    public void renderChatHistory(int x, int y, int width, int height, float alpha) {
        renderTextBox(x, y, width, height, alpha);
        float textY = y + 20;
        for (String message : chatHistory) {
            bitmapFont.renderText(message, x + 5, textY, 1.5f);
            textY += 20;
        }
    }
}
