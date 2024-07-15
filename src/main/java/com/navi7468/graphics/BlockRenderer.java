package com.navi7468.graphics;

import com.navi7468.util.TextureLoader;
import com.navi7468.world.BlockType;
import com.navi7468.world.Chunk;
import com.navi7468.world.World;
import org.lwjgl.opengl.GL11;

public class BlockRenderer {
    private int grassSideTexture;
    private int grassTopTexture;
    private int dirtTexture;
    private int stoneTexture;
    private int bedrockTexture;

    public BlockRenderer() {
        grassSideTexture = TextureLoader.loadTexture("textures/blocks/grass_side.png");
        grassTopTexture = TextureLoader.loadTexture("textures/blocks/grass_top.png");
        dirtTexture = TextureLoader.loadTexture("textures/blocks/dirt.png");
        stoneTexture = TextureLoader.loadTexture("textures/blocks/stone.png");
        bedrockTexture = TextureLoader.loadTexture("textures/blocks/bedrock.png");
    }

    public void renderChunk(World world, Chunk chunk, float startX, float startY, float startZ) {
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                    int blockType = chunk.getBlock(x, y, z);
                    if (blockType != BlockType.AIR) {
                        renderBlock(world, chunk, blockType, startX + x, startY + y, startZ + z, x, y, z);
                    }
                }
            }
        }
    }

    private void renderBlock(World world, Chunk chunk, int blockType, float x, float y, float z, int cx, int cy, int cz) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        int sideTexture, topTexture, bottomTexture;

        switch (blockType) {
            case BlockType.GRASS:
                sideTexture = grassSideTexture;
                topTexture = grassTopTexture;
                bottomTexture = dirtTexture;
                break;
            case BlockType.DIRT:
                sideTexture = topTexture = bottomTexture = dirtTexture;
                break;
            case BlockType.STONE:
                sideTexture = topTexture = bottomTexture = stoneTexture;
                break;
            case BlockType.BEDROCK:
                sideTexture = topTexture = bottomTexture = bedrockTexture;
                break;
            default:
                sideTexture = topTexture = bottomTexture = TextureLoader.loadTexture("textures/blocks/error.png");
        }

        // Check for adjacent blocks
        if (shouldRenderFace(world, chunk, cx, cy, cz + 1)) {
            renderFace(sideTexture, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f); // Front face
        }
        if (shouldRenderFace(world, chunk, cx, cy, cz - 1)) {
            renderFace(sideTexture, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f); // Back face
        }
        if (shouldRenderFace(world, chunk, cx, cy + 1, cz)) {
            renderFace(topTexture, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f); // Top face
        }
        if (shouldRenderFace(world, chunk, cx, cy - 1, cz)) {
            renderFace(bottomTexture, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f); // Bottom face
        }
        if (shouldRenderFace(world, chunk, cx + 1, cy, cz)) {
            renderFace(sideTexture, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f); // Right face
        }
        if (shouldRenderFace(world, chunk, cx - 1, cy, cz)) {
            renderFace(sideTexture, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f); // Left face
        }

        GL11.glPopMatrix();
    }

    private boolean shouldRenderFace(World world, Chunk chunk, int x, int y, int z) {
        int block = chunk.getBlock(x, y, z);
        if (block != BlockType.AIR) {
            return false;
        }
        int chunkX = chunk.getChunkX();
        int chunkY = chunk.getChunkY();
        int chunkZ = chunk.getChunkZ();

        // Check if adjacent block is out of current chunk bounds
        if (x < 0 || x >= Chunk.CHUNK_SIZE || y < 0 || y >= Chunk.CHUNK_SIZE || z < 0 || z >= Chunk.CHUNK_SIZE) {
            // Get block from neighboring chunk
            int neighborBlock = world.getBlock(chunkX * Chunk.CHUNK_SIZE + x, chunkY * Chunk.CHUNK_SIZE + y, chunkZ * Chunk.CHUNK_SIZE + z);
            return neighborBlock == BlockType.AIR;
        }
        return true;
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



// package com.navi7468.graphics;
//
// import com.navi7468.util.TextureLoader;
// import com.navi7468.world.BlockType;
// import com.navi7468.world.Chunk;
// import org.lwjgl.opengl.GL11;
//
// public class BlockRenderer {
//     private int grassSideTexture;
//     private int grassTopTexture;
//     private int dirtTexture;
//     private int stoneTexture;
//     private int bedrockTexture;
//
//     public BlockRenderer() {
//         grassSideTexture = TextureLoader.loadTexture("textures/blocks/grass_side.png");
//         grassTopTexture = TextureLoader.loadTexture("textures/blocks/grass_top.png");
//         dirtTexture = TextureLoader.loadTexture("textures/blocks/dirt.png");
//         stoneTexture = TextureLoader.loadTexture("textures/blocks/stone.png");
//         bedrockTexture = TextureLoader.loadTexture("textures/blocks/bedrock.png");
//     }
//
//     public void renderChunk(Chunk chunk, float startX, float startY, float startZ) {
//         for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
//             for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
//                 for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
//                     int blockType = chunk.getBlock(x, y, z);
//                     if (blockType != BlockType.AIR) {
//                         renderBlock(chunk, blockType, startX + x, startY + y, startZ + z, x, y, z);
//                     }
//                 }
//             }
//         }
//     }
//
//     private void renderBlock(Chunk chunk, int blockType, float x, float y, float z, int cx, int cy, int cz) {
//         GL11.glPushMatrix();
//         GL11.glTranslatef(x, y, z);
//
//         int sideTexture, topTexture, bottomTexture;
//
//         switch (blockType) {
//             case BlockType.GRASS:
//                 sideTexture = grassSideTexture;
//                 topTexture = grassTopTexture;
//                 bottomTexture = dirtTexture;
//                 break;
//             case BlockType.DIRT:
//                 sideTexture = topTexture = bottomTexture = dirtTexture;
//                 break;
//             case BlockType.STONE:
//                 sideTexture = topTexture = bottomTexture = stoneTexture;
//                 break;
//             case BlockType.BEDROCK:
//                 sideTexture = topTexture = bottomTexture = bedrockTexture;
//                 break;
//             default:
//                 sideTexture = topTexture = bottomTexture = TextureLoader.loadTexture("textures/blocks/error.png");
//         }
//
//         // Render only the faces that are exposed (i.e., adjacent to air blocks)
//         if (chunk.getBlock(cx, cy, cz + 1) == BlockType.AIR) {
//             renderFace(sideTexture, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f); // Front face
//         }
//         if (chunk.getBlock(cx, cy, cz - 1) == BlockType.AIR) {
//             renderFace(sideTexture, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f); // Back face
//         }
//         if (chunk.getBlock(cx, cy + 1, cz) == BlockType.AIR) {
//             renderFace(topTexture, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f); // Top face
//         }
//         if (chunk.getBlock(cx, cy - 1, cz) == BlockType.AIR) {
//             renderFace(bottomTexture, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f); // Bottom face
//         }
//         if (chunk.getBlock(cx + 1, cy, cz) == BlockType.AIR) {
//             renderFace(sideTexture, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f); // Right face
//         }
//         if (chunk.getBlock(cx - 1, cy, cz) == BlockType.AIR) {
//             renderFace(sideTexture, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f); // Left face
//         }
//
//         GL11.glPopMatrix();
//     }
//
//     private void renderFace(int texture, float... vertices) {
//         if (vertices.length != 12) {
//             throw new IllegalArgumentException("renderFace requires exactly 12 vertices");
//         }
//
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
//         GL11.glBegin(GL11.GL_QUADS);
//
//         GL11.glTexCoord2f(0, 1);
//         GL11.glVertex3f(vertices[0], vertices[1], vertices[2]);
//         GL11.glTexCoord2f(1, 1);
//         GL11.glVertex3f(vertices[3], vertices[4], vertices[5]);
//         GL11.glTexCoord2f(1, 0);
//         GL11.glVertex3f(vertices[6], vertices[7], vertices[8]);
//         GL11.glTexCoord2f(0, 0);
//         GL11.glVertex3f(vertices[9], vertices[10], vertices[11]);
//
//         GL11.glEnd();
//     }
// }
