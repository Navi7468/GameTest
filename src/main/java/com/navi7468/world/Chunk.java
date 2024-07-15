package com.navi7468.world;

import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.Grid;
import org.lwjgl.opengl.GL11;

public class Chunk {
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256; // Set chunk height to 256
    private int[][][] blocks;
    private int chunkX, chunkY, chunkZ;

    public Chunk(NoiseGenerator noise, int chunkX, int chunkY, int chunkZ) {
        this.blocks = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE]; // Adjust height dimension
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        generateTerrain(noise);
    }

    private void generateTerrain(NoiseGenerator noise) {
        Grid grid = new Grid(CHUNK_SIZE * 4, CHUNK_SIZE * 4); // Larger grid for smoother terrain
        noiseStage(grid, noise, 32, 0.5f);
        noiseStage(grid, noise, 16, 0.25f);
        noiseStage(grid, noise, 8, 0.125f);
        noiseStage(grid, noise, 4, 0.0625f);
        noiseStage(grid, noise, 2, 0.03125f);

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                int worldX = chunkX * CHUNK_SIZE + x;
                int worldZ = chunkZ * CHUNK_SIZE + z;

                // Normalize world coordinates to fit within the grid bounds
                int gridX = Math.floorMod(worldX, grid.getWidth());
                int gridZ = Math.floorMod(worldZ, grid.getHeight());

                // Adjust height calculation to add more variation and smoothing
                double height = 0;
                height += grid.get(gridX, gridZ) * 30;
                height += grid.get(gridX / 2, gridZ / 2) * 15;
                height += grid.get(gridX / 4, gridZ / 4) * 7;
                height += 64; // Base height level

                height = Math.min(height, CHUNK_HEIGHT - 1); // Ensure height doesn't exceed chunk height

                for (int y = 0; y < CHUNK_HEIGHT; y++) {
                    int worldY = y + chunkY * CHUNK_HEIGHT;

                    if (worldY <= 5) { // Bedrock layer
                        blocks[x][y][z] = BlockType.BEDROCK;
                    } else if (worldY <= height - 4) { // Stone layer
                        blocks[x][y][z] = BlockType.STONE;
                    } else if (worldY <= height - 1) { // Dirt layer
                        blocks[x][y][z] = BlockType.DIRT;
                    } else if (worldY <= height) { // Grass layer
                        blocks[x][y][z] = BlockType.GRASS;
                    } else { // Air
                        blocks[x][y][z] = BlockType.AIR;
                    }
                }
            }
        }
    }

    private static void noiseStage(final Grid grid, final NoiseGenerator noiseGenerator, final int radius, final float modifier) {
        noiseGenerator.setRadius(radius);
        noiseGenerator.setModifier(modifier);
        noiseGenerator.setSeed(12345); // Use a consistent seed for reproducibility
        noiseGenerator.generate(grid);
    }

    public int getBlock(int x, int y, int z) {
        if (x < 0 || x >= CHUNK_SIZE || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_SIZE) {
            return BlockType.AIR; // Return air for out-of-bound coordinates
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
