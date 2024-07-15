package com.navi7468.util;

import com.navi7468.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Camera {
    private Settings settings;
    private float x, y, z;
    private float pitch, yaw;
    private boolean invertPitch, invertYaw;
    private boolean isSprinting;

    public Camera(Settings settings) {
        this.settings = settings;
        this.x = settings.cameraX;
        this.y = settings.cameraY;
        this.z = settings.cameraZ;
        this.pitch = settings.cameraPitch;
        this.yaw = settings.cameraYaw;
        this.invertPitch = settings.invertPitch;
        this.invertYaw = settings.invertYaw;
    }

    public void handleInput(long window, float moveSpeed, float mouseSensitivity, double deltaTime) {
        float actualMoveSpeed = moveSpeed * (float) deltaTime;
        if (isSprinting) {
            actualMoveSpeed *= 5.0f;
        }

        if (GLFW.glfwGetKey(window, settings.moveUpKey) == GLFW.GLFW_PRESS) {
            y += actualMoveSpeed;
        }
        if (GLFW.glfwGetKey(window, settings.moveDownKey) == GLFW.GLFW_PRESS) {
            y -= actualMoveSpeed;
        }
        if (GLFW.glfwGetKey(window, settings.moveForwardKey) == GLFW.GLFW_PRESS) {
            z -= actualMoveSpeed * Math.cos(Math.toRadians(yaw));
            x += actualMoveSpeed * Math.sin(Math.toRadians(yaw));
        }
        if (GLFW.glfwGetKey(window, settings.moveBackwardKey) == GLFW.GLFW_PRESS) {
            z += actualMoveSpeed * Math.cos(Math.toRadians(yaw));
            x -= actualMoveSpeed * Math.sin(Math.toRadians(yaw));
        }
        if (GLFW.glfwGetKey(window, settings.moveLeftKey) == GLFW.GLFW_PRESS) {
            x -= actualMoveSpeed * Math.cos(Math.toRadians(yaw));
            z -= actualMoveSpeed * Math.sin(Math.toRadians(yaw));
        }
        if (GLFW.glfwGetKey(window, settings.moveRightKey) == GLFW.GLFW_PRESS) {
            x += actualMoveSpeed * Math.cos(Math.toRadians(yaw));
            z += actualMoveSpeed * Math.sin(Math.toRadians(yaw));
        }

        double[] mouseX = new double[1];
        double[] mouseY = new double[1];
        GLFW.glfwGetCursorPos(window, mouseX, mouseY);
        GLFW.glfwSetCursorPos(window, 1500 / 2.0, 800 / 2.0);

        float deltaX = (float) (mouseX[0] - 1500 / 2.0) * mouseSensitivity;
        float deltaY = (float) (mouseY[0] - 800 / 2.0) * mouseSensitivity;

        yaw += invertYaw ? -deltaX : deltaX;
        pitch += invertPitch ? -deltaY : deltaY;

        if (pitch > 90) pitch = 90;
        if (pitch < -90) pitch = -90;
    }

    public void applyTransformations() {
        GL11.glRotatef(pitch, 1, 0, 0);
        GL11.glRotatef(yaw, 0, 1, 0);
        GL11.glTranslatef(-x, -y, -z);
    }

    public void setPerspective(float fov, float aspectRatio, float zNear, float zFar) {
        float yScale = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
        float xScale = yScale / aspectRatio;
        float frustumLength = zFar - zNear;

        float[] perspectiveMatrix = {
                xScale, 0,      0,                              0,
                0,      yScale, 0,                              0,
                0,      0,      -(zFar + zNear) / frustumLength, -1,
                0,      0,      -2 * zNear * zFar / frustumLength, 0
        };

        GL11.glMultMatrixf(perspectiveMatrix);
    }

    public void setSprinting(boolean sprinting) {
        isSprinting = sprinting;
    }

    public float getValue(float value, Main.ReturnType returnType) {
        switch (returnType) {
            case SHORT:
                return Math.round(value * 100.0f) / 100.0f; // Returns value rounded to two decimal places
            case INT:
                return (int) value; // Returns value as an integer
            default:
                return value; // Returns value as a float by default
        }
    }

    public float getX(Main.ReturnType returnType) {
        return getValue(x, returnType);
    }

    public float getY(Main.ReturnType returnType) {
        return getValue(y, returnType);
    }

    public float getZ(Main.ReturnType returnType) {
        return getValue(z, returnType);
    }

    public float getPitch(Main.ReturnType returnType) {
        return getValue(pitch, returnType);
    }

    public float getYaw(Main.ReturnType returnType) {
        return getValue(yaw, returnType);
    }
}
