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

        float textureWidth = 128.0f; // Assuming the texture width is 128
        float textureHeight = 128.0f; // Assuming the texture height is 128

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



// package com.navi7468.graphics;
//
// import com.navi7468.util.TextureLoader;
// import org.lwjgl.opengl.GL11;
//
// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import static org.lwjgl.opengl.GL11.*;
// import static org.lwjgl.opengl.GL11C.glBindTexture;
//
// public class BitmapFont {
//     private int textureID;
//     private int lineHeight;
//     private Map<Integer, Glyph> glyphs = new HashMap<>();
//
//     public BitmapFont(String texturePath, String fontDataPath) throws IOException {
//         this.textureID = TextureLoader.loadTexture(texturePath);
//         loadFontData(fontDataPath);
//     }
//
//     private void loadFontData(String fontDataPath) throws IOException {
//         List<String> lines = Files.readAllLines(Paths.get(fontDataPath));
//
//         for (String line : lines) {
//             if (line.startsWith("info")) {
//                 // Skip info line
//             } else if (line.startsWith("common")) {
//                 String[] parts = line.split(" ");
//                 for (String part : parts) {
//                     String[] keyValue = part.split("=");
//                     if (keyValue[0].equals("lineHeight")) {
//                         this.lineHeight = Integer.parseInt(keyValue[1]);
//                     }
//                 }
//             } else if (line.startsWith("char")) {
//                 String[] parts = line.split(" ");
//                 Glyph glyph = new Glyph();
//                 for (String part : parts) {
//                     String[] keyValue = part.split("=");
//                     switch (keyValue[0]) {
//                         case "id":
//                             glyph.id = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "x":
//                             glyph.x = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "y":
//                             glyph.y = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "width":
//                             glyph.width = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "height":
//                             glyph.height = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "xoffset":
//                             glyph.xoffset = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "yoffset":
//                             glyph.yoffset = Integer.parseInt(keyValue[1]);
//                             break;
//                         case "xadvance":
//                             glyph.xadvance = Integer.parseInt(keyValue[1]);
//                             break;
//                     }
//                 }
//                 glyphs.put(glyph.id, glyph);
//             }
//         }
//     }
//
//     public void drawString(String text, float x, float y, float scale) {
//         glBindTexture(GL_TEXTURE_2D, textureID);
//
//         glBegin(GL_QUADS);
//         float xPos = x;
//         for (char c : text.toCharArray()) {
//             Glyph glyph = glyphs.get((int) c);
//             if (glyph != null) {
//                 float x0 = xPos + glyph.xoffset * scale;
//                 float y0 = y + glyph.yoffset * scale;
//                 float x1 = x0 + glyph.width * scale;
//                 float y1 = y0 + glyph.height * scale;
//                 float s0 = (float) glyph.x / 128.0f;
//                 float t0 = (float) glyph.y / 128.0f;
//                 float s1 = s0 + (float) glyph.width / 128.0f;
//                 float t1 = t0 + (float) glyph.height / 128.0f;
//
//                 glTexCoord2f(s0, t0); glVertex2f(x0, y0);
//                 glTexCoord2f(s1, t0); glVertex2f(x1, y0);
//                 glTexCoord2f(s1, t1); glVertex2f(x1, y1);
//                 glTexCoord2f(s0, t1); glVertex2f(x0, y1);
//
//                 xPos += glyph.xadvance * scale;
//             }
//         }
//         glEnd();
//     }
//
//     private class Glyph {
//         int id;
//         int x, y;
//         int width, height;
//         int xoffset, yoffset;
//         int xadvance;
//     }
// }
//
//
// // public class BitmapFont {
// //     private int textureID;
// //     private Map<Integer, Glyph> glyphs;
// //
// //     public BitmapFont(String texturePath, String fontFilePath) {
// //         this.textureID = TextureLoader.loadTexture(texturePath);
// //         this.glyphs = new HashMap<>();
// //         loadFontData(fontFilePath);
// //     }
// //
// //     private void loadFontData(String fontFilePath) {
// //         try (BufferedReader reader = new BufferedReader(new FileReader(fontFilePath))) {
// //             String line;
// //             while ((line = reader.readLine()) != null) {
// //                 System.out.println("Reading line: " + line);
// //                 if (line.startsWith("char ")) {
// //                     String[] parts = line.split("\\s+");
// //                     if (parts.length >= 11) {
// //                         int id = Integer.parseInt(parts[1].split("=")[1]);
// //                         int x = Integer.parseInt(parts[2].split("=")[1]);
// //                         int y = Integer.parseInt(parts[3].split("=")[1]);
// //                         int width = Integer.parseInt(parts[4].split("=")[1]);
// //                         int height = Integer.parseInt(parts[5].split("=")[1]);
// //                         int xoffset = Integer.parseInt(parts[6].split("=")[1]);
// //                         int yoffset = Integer.parseInt(parts[7].split("=")[1]);
// //                         int xadvance = Integer.parseInt(parts[8].split("=")[1]);
// //                         int page = Integer.parseInt(parts[9].split("=")[1]);
// //                         int chnl = Integer.parseInt(parts[10].split("=")[1]);
// //
// //                         glyphs.put(id, new Glyph(x, y, width, height, xoffset, yoffset, xadvance));
// //                     } else {
// //                         System.err.println("Error parsing line: " + line);
// //                     }
// //                 }
// //             }
// //         } catch (IOException e) {
// //             e.printStackTrace();
// //         }
// //     }
// //
// //     public void renderText(String text, float x, float y, float scale) {
// //         GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
// //         for (char c : text.toCharArray()) {
// //             Glyph glyph = glyphs.get((int) c);
// //             if (glyph != null) {
// //                 renderGlyph(glyph, x, y, scale);
// //                 x += glyph.xadvance * scale;
// //             }
// //         }
// //         GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
// //     }
// //
// //     private void renderGlyph(Glyph glyph, float x, float y, float scale) {
// //         GL11.glBegin(GL11.GL_QUADS);
// //         GL11.glTexCoord2f(glyph.x / 128.0f, glyph.y / 128.0f);
// //         GL11.glVertex2f(x, y);
// //         GL11.glTexCoord2f((glyph.x + glyph.width) / 128.0f, glyph.y / 128.0f);
// //         GL11.glVertex2f(x + glyph.width * scale, y);
// //         GL11.glTexCoord2f((glyph.x + glyph.width) / 128.0f, (glyph.y + glyph.height) / 128.0f);
// //         GL11.glVertex2f(x + glyph.width * scale, y + glyph.height * scale);
// //         GL11.glTexCoord2f(glyph.x / 128.0f, (glyph.y + glyph.height) / 128.0f);
// //         GL11.glVertex2f(x, y + glyph.height * scale);
// //         GL11.glEnd();
// //     }
// //
// //     private static class Glyph {
// //         int x, y, width, height, xoffset, yoffset, xadvance;
// //
// //         Glyph(int x, int y, int width, int height, int xoffset, int yoffset, int xadvance) {
// //             this.x = x;
// //             this.y = y;
// //             this.width = width;
// //             this.height = height;
// //             this.xoffset = xoffset;
// //             this.yoffset = yoffset;
// //             this.xadvance = xadvance;
// //         }
// //     }
// // }
