package com.navi7468.graphics;

import com.navi7468.util.TextureLoader;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapFont {
    private int textureID;
    private int lineHeight;
    private Map<Integer, Glyph> glyphs = new HashMap<>();

    public BitmapFont(String texturePath, String fontDataPath) throws IOException {
        this.textureID = TextureLoader.loadTexture(texturePath);
        loadFontData(fontDataPath);
    }

    private void loadFontData(String fontDataPath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fontDataPath));
        for (String line : lines) {
            if (line.startsWith("info")) {
                // Skip info line
            } else if (line.startsWith("common")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    if (keyValue[0].equals("lineHeight")) {
                        this.lineHeight = Integer.parseInt(keyValue[1]);
                    }
                }
            } else if (line.startsWith("char")) {
                String[] parts = line.split(" ");
                Glyph glyph = new Glyph();
                for (String part : parts) {
                    String[] keyValue = part.split("=");
                    switch (keyValue[0]) {
                        case "id":
                            glyph.id = Integer.parseInt(keyValue[1]);
                            break;
                        case "x":
                            glyph.x = Integer.parseInt(keyValue[1]);
                            break;
                        case "y":
                            glyph.y = Integer.parseInt(keyValue[1]);
                            break;
                        case "width":
                            glyph.width = Integer.parseInt(keyValue[1]);
                            break;
                        case "height":
                            glyph.height = Integer.parseInt(keyValue[1]);
                            break;
                        case "xoffset":
                            glyph.xoffset = Integer.parseInt(keyValue[1]);
                            break;
                        case "yoffset":
                            glyph.yoffset = Integer.parseInt(keyValue[1]);
                            break;
                        case "xadvance":
                            glyph.xadvance = Integer.parseInt(keyValue[1]);
                            break;
                    }
                }
                glyphs.put(glyph.id, glyph);
            }
        }
    }

    public void renderText(String text, float x, float y, float scale) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glBegin(GL11.GL_QUADS);

        // Texture size (default 128x128)
        float textureWidth = 128.0f;
        float textureHeight = 128.0f;

        float xPos = x;
        for (char c : text.toCharArray()) {
            Glyph glyph = glyphs.get((int) c);
            if (glyph != null) {
                float x0 = xPos + glyph.xoffset * scale;
                float y0 = y + glyph.yoffset * scale;
                float x1 = x0 + glyph.width * scale;
                float y1 = y0 + glyph.height * scale;
                float s0 = (float) glyph.x / textureWidth;
                float t0 = (float) glyph.y / textureHeight;
                float s1 = s0 + (float) glyph.width / textureWidth;
                float t1 = t0 + (float) glyph.height / textureHeight;

                GL11.glTexCoord2f(s0, t0); GL11.glVertex2f(x0, y0);
                GL11.glTexCoord2f(s1, t0); GL11.glVertex2f(x1, y0);
                GL11.glTexCoord2f(s1, t1); GL11.glVertex2f(x1, y1);
                GL11.glTexCoord2f(s0, t1); GL11.glVertex2f(x0, y1);

                xPos += glyph.xadvance * scale;
            }
        }
        GL11.glEnd();
    }

    private class Glyph {
        int id;
        int x, y;
        int width, height;
        int xoffset, yoffset;
        int xadvance;
    }
}
