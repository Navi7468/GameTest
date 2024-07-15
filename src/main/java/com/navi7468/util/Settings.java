package com.navi7468.util;

import org.lwjgl.glfw.GLFW;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class Settings {
    // Window properties
    public int windowWidth;
    public int windowHeight;
    // Camera properties
    public float cameraX;
    public float cameraY;
    public float cameraZ;
    public float cameraPitch;
    public float cameraYaw;
    public float cameraFov;
    public boolean invertPitch;
    public boolean invertYaw;
    public float cameraNear;
    public float cameraFar;
    // Movement settings
    public float moveSpeed;
    public float mouseSensitivity;
    // Key bindings
    public int debugKey;
    public int chatKey;
    public int moveUpKey;
    public int moveDownKey;
    public int moveForwardKey;
    public int moveBackwardKey;
    public int moveLeftKey;
    public int moveRightKey;
    public int sprintKey;

    public Settings(String configFilePath) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(configFilePath)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Window properties
        windowWidth = Integer.parseInt(properties.getProperty("window.width"));
        windowHeight = Integer.parseInt(properties.getProperty("window.height"));
        // Camera properties
        cameraX = Float.parseFloat(properties.getProperty("camera.x"));
        cameraY = Float.parseFloat(properties.getProperty("camera.y"));
        cameraZ = Float.parseFloat(properties.getProperty("camera.z"));
        cameraPitch = Float.parseFloat(properties.getProperty("camera.pitch"));
        cameraYaw = Float.parseFloat(properties.getProperty("camera.yaw"));
        cameraFov = Float.parseFloat(properties.getProperty("camera.fov"));
        invertPitch = Boolean.parseBoolean(properties.getProperty("camera.invert.pitch"));
        invertYaw = Boolean.parseBoolean(properties.getProperty("camera.invert.yaw"));
        cameraNear = Float.parseFloat(properties.getProperty("camera.near"));
        cameraFar = Float.parseFloat(properties.getProperty("camera.far"));
        // Movement settings
        moveSpeed = Float.parseFloat(properties.getProperty("move.speed"));
        mouseSensitivity = Float.parseFloat(properties.getProperty("mouse.sensitivity"));
        // Key bindings
        debugKey = getKeyCode(properties.getProperty("key.debug"));
        chatKey = getKeyCode(properties.getProperty("key.chat"));
        moveUpKey = getKeyCode(properties.getProperty("key.move.up"));
        moveDownKey = getKeyCode(properties.getProperty("key.move.down"));
        moveForwardKey = getKeyCode(properties.getProperty("key.move.forward"));
        moveBackwardKey = getKeyCode(properties.getProperty("key.move.backward"));
        moveLeftKey = getKeyCode(properties.getProperty("key.move.left"));
        moveRightKey = getKeyCode(properties.getProperty("key.move.right"));
        sprintKey = getKeyCode(properties.getProperty("key.move.sprint"));
    }

    private int getKeyCode(String keyName) {
        try {
            Field field = GLFW.class.getField("GLFW_KEY_" + keyName.toUpperCase());
            return field.getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Unknown key: " + keyName, e);
        }
    }
}
