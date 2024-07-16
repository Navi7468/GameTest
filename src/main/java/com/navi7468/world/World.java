package com.navi7468.world;

import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.Grid;

import java.util.HashMap;
import java.util.Map;

public class World {
    private Map<String, Chunk> chunks;
    private int worldHeight;
    private int worldSize;
    private NoiseGenerator noiseGenerator;

    public World(int worldSize, int worldHeight, NoiseGenerator noise) {
        this.worldSize = worldSize;
        this.worldHeight = worldHeight;
        this.noiseGenerator = noise;
        chunks = new HashMap<>();
        generateWorld();
    }

    private void generateWorld() {
        Grid grid = new Grid(worldSize * Chunk.CHUNK_SIZE, worldSize * Chunk.CHUNK_SIZE);
        noiseStage(grid, noiseGenerator, 32, 0.5f);
        noiseStage(grid, noiseGenerator, 16, 0.25f);
        noiseStage(grid, noiseGenerator, 8, 0.125f);
        noiseStage(grid, noiseGenerator, 4, 0.0625f);
        noiseStage(grid, noiseGenerator, 2, 0.03125f);

        for (int x = 0; x < worldSize; x++) {
            for (int z = 0; z < worldSize; z++) {
                for (int y = 0; y < worldHeight / Chunk.CHUNK_HEIGHT; y++) {
                    Chunk chunk = new Chunk(x, y, z);
                    generateChunkTerrain(chunk, grid);
                    chunks.put(getChunkKey(x, y, z), chunk);
                }
            }
        }
    }

    private void generateChunkTerrain(Chunk chunk, Grid grid) {
        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                int worldX = chunk.getChunkX() * Chunk.CHUNK_SIZE + x;
                int worldZ = chunk.getChunkZ() * Chunk.CHUNK_SIZE + z;

                int gridX = Math.floorMod(worldX, grid.getWidth());
                int gridZ = Math.floorMod(worldZ, grid.getHeight());

                double height = 0;
                height += grid.get(gridX, gridZ) * 30;
                height += grid.get(gridX / 2, gridZ / 2) * 15;
                height += grid.get(gridX / 4, gridZ / 4) * 7;
                height += 64;

                height = Math.min(height, Chunk.CHUNK_HEIGHT - 1);

                for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
                    int worldY = y + chunk.getChunkY() * Chunk.CHUNK_HEIGHT;

                    if (worldY <= 5) {
                        chunk.setBlock(x, y, z, BlockType.BEDROCK);
                    } else if (worldY <= height - 4) {
                        chunk.setBlock(x, y, z, BlockType.STONE);
                    } else if (worldY <= height - 1) {
                        chunk.setBlock(x, y, z, BlockType.DIRT);
                    } else if (worldY <= height) {
                        chunk.setBlock(x, y, z, BlockType.GRASS);
                    } else {
                        chunk.setBlock(x, y, z, BlockType.AIR);
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

    public Chunk getChunk(int x, int y, int z) {
        return chunks.get(getChunkKey(x, y, z));
    }

    private String getChunkKey(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    public int getBlock(int x, int y, int z) {
        int chunkX = x / Chunk.CHUNK_SIZE;
        int chunkY = y / Chunk.CHUNK_HEIGHT;
        int chunkZ = z / Chunk.CHUNK_SIZE;

        Chunk chunk = getChunk(chunkX, chunkY, chunkZ);
        if (chunk == null) {
            return BlockType.AIR;
        }

        int localX = x % Chunk.CHUNK_SIZE;
        int localY = y % Chunk.CHUNK_HEIGHT;
        int localZ = z % Chunk.CHUNK_SIZE;

        return chunk.getBlock(localX, localY, localZ);
    }

    public int getWorldSize() {
        return worldSize;
    }

    public int getWorldHeight() {
        return worldHeight;
    }
}