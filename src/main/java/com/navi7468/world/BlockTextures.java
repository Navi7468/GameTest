package com.navi7468.world;

import com.navi7468.util.TextureLoader;
import org.lwjgl.opengl.GL11;

public class BlockTextures {
    public final String side;
    public final String top;
    public final String bottom;
    private int sideTexture;
    private int topTexture;
    private int bottomTexture;

    public BlockTextures(String side) {
        this.side = side;
        this.top = side;
        this.bottom = side;
        loadTextures();
    }

    public BlockTextures(String side, String top, String bottom) {
        this.side = side;
        this.top = top;
        this.bottom = bottom;
        loadTextures();
    }

    private void loadTextures() {
        this.sideTexture = loadTexture(side);
        this.topTexture = loadTexture(top);
        this.bottomTexture = loadTexture(bottom);
    }

    private int loadTexture(String texturePath) {
        return TextureLoader.loadTexture(texturePath);
    }

    public void render(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        // Render each face with the appropriate texture
        renderFace(sideTexture, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f); // Front face
        renderFace(sideTexture, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f); // Back face
        renderFace(topTexture, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f); // Top face
        renderFace(bottomTexture, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f); // Bottom face
        renderFace(sideTexture, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f); // Right face
        renderFace(sideTexture, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f); // Left face

        GL11.glPopMatrix();
    }

    private void renderFace(int texture, float... vertices) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(vertices[0], vertices[1], vertices[2]);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(vertices[3], vertices[4], vertices[5]);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(vertices[6], vertices[7], vertices[8]);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(vertices[9], vertices[10], vertices[11]);

        GL11.glEnd();
    }
}
