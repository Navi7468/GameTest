package com.navi7468.util;

import org.lwjgl.opengl.GL11;

public class RenderUtil {

    public static void renderTextBox(int x, int y, int width, int height, float alpha) {
        GL11.glColor4f(0.0f, 0.0f, 0.0f, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Reset color to white
    }
}
