package com.navi7468.world;

import org.lwjgl.opengl.GL11;

public class Chunk {
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;
    private int[][][] blocks;
    private int chunkX, chunkY, chunkZ;

    public Chunk(int chunkX, int chunkY, int chunkZ) {
        this.blocks = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
    }

    public void setBlock(int x, int y, int z, int blockType) {
        if (x >= 0 && x < CHUNK_SIZE && y >= 0 && y < CHUNK_HEIGHT && z >= 0 && z < CHUNK_SIZE) {
            blocks[x][y][z] = blockType;
        }
    }

    public int getBlock(int x, int y, int z) {
        if (x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_SIZE) {
            return BlockType.AIR;
        }
        return blocks[x][y][z];
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void render() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_HEIGHT; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    int blockType = blocks[x][y][z];
                    if (blockType != BlockType.AIR) {
                        renderBlock(x, y, z, blocks[x][y][z]);
                    }
                }
            }
        }
    }

    private void renderBlock(int x, int y, int z, int type) {
        GL11.glPushMatrix();
        GL11.glTranslatef(chunkX * CHUNK_SIZE + x, y, chunkZ * CHUNK_SIZE + z);
        BlockType.getBlockTextures(type).render(x, y, z);
        GL11.glPopMatrix();
    }
}
