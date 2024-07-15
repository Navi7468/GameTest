package com.navi7468.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {
    private static final Map<String, Integer> textureCache = new HashMap<>();

    public static int loadTexture(String filePath) {
        if (textureCache.containsKey(filePath)) {
            return textureCache.get(filePath);
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image;
        String fullPath = "src/main/resources/" + filePath;
        if (Files.exists(Paths.get(fullPath))) {
            System.out.println("Loading texture: " + fullPath);
            image = STBImage.stbi_load(fullPath, width, height, channels, 4);
        } else {
            System.err.println("Failed to load texture file: " + fullPath + ". Using default error texture.");
            image = STBImage.stbi_load("src/main/resources/textures/blocks/error.png", width, height, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load default error texture: textures/blocks/error.png");
            }
        }

        if (image == null) {
            throw new RuntimeException("Failed to load texture image: " + filePath);
        }

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        STBImage.stbi_image_free(image);

        textureCache.put(filePath, textureID);
        System.out.println("Loaded texture: " + fullPath + " with ID: " + textureID);
        return textureID;
    }
}


// package com.navi7468.util;
//
// import org.lwjgl.BufferUtils;
// import org.lwjgl.opengl.GL11;
// import org.lwjgl.stb.STBImage;
//
// import java.nio.ByteBuffer;
// import java.nio.IntBuffer;
// import java.nio.file.Files;
// import java.nio.file.Paths;
//
// public class TextureLoader {
//
//     public static int loadTexture(String filePath) {
//         IntBuffer width = BufferUtils.createIntBuffer(1);
//         IntBuffer height = BufferUtils.createIntBuffer(1);
//         IntBuffer channels = BufferUtils.createIntBuffer(1);
//
//         ByteBuffer image;
//         String fullPath = "src/main/resources/" + filePath;
//         if (Files.exists(Paths.get(fullPath))) {
//             System.out.println("Loading texture: " + fullPath);
//             image = STBImage.stbi_load(fullPath, width, height, channels, 4);
//         } else {
//             System.err.println("Failed to load texture file: " + fullPath + ". Using default error texture.");
//             image = STBImage.stbi_load("src/main/resources/textures/blocks/error.png", width, height, channels, 4);
//             if (image == null) {
//                 throw new RuntimeException("Failed to load default error texture: textures/blocks/error.png");
//             }
//         }
//
//         if (image == null) {
//             throw new RuntimeException("Failed to load texture image: " + filePath);
//         }
//
//         int textureID = GL11.glGenTextures();
//         if (textureID == 0) {
//             System.err.println("Failed to generate a new texture ID.");
//             int errorCode = GL11.glGetError();
//             if (errorCode != GL11.GL_NO_ERROR) {
//                 System.err.println("OpenGL error during texture ID generation: " + errorCode);
//             }
//             throw new RuntimeException("Failed to generate a new texture ID.");
//         }
//
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
//         checkGLError("glBindTexture");
//
//         GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
//         checkGLError("glTexImage2D");
//
//         // Use GL_NEAREST for both minification and magnification to avoid blurriness
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
//         checkGLError("glTexParameteri MIN_FILTER");
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//         checkGLError("glTexParameteri MAG_FILTER");
//
//         int errorCode = GL11.glGetError();
//         if (errorCode != GL11.GL_NO_ERROR) {
//             System.err.println("OpenGL error during texture loading: " + errorCode);
//             return 0;
//         }
//
//         STBImage.stbi_image_free(image);
//
//         System.out.println("Loaded texture: " + fullPath + " with ID: " + textureID);
//         return textureID;
//     }
//
//     private static void checkGLError(String operation) {
//         int errorCode = GL11.glGetError();
//         if (errorCode != GL11.GL_NO_ERROR) {
//             System.err.println("OpenGL error during " + operation + ": " + errorCode);
//         }
//     }
// }
