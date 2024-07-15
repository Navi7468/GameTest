package com.navi7468.world;

import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;

import java.util.HashMap;
import java.util.Map;

public class World {
    private Map<String, Chunk> chunks;
    private int worldHeight;
    private int worldSize;

    public World(int worldSize, int worldHeight, NoiseGenerator noise) {
        this.worldSize = worldSize;
        this.worldHeight = worldHeight;
        chunks = new HashMap<>();
        generateWorld(noise);
    }

    private void generateWorld(NoiseGenerator noise) {
        for (int x = 0; x < worldSize; x++) {
            for (int z = 0; z < worldSize; z++) {
                for (int y = 0; y < worldHeight / Chunk.CHUNK_HEIGHT; y++) { // Adjust iteration to match chunk height
                    chunks.put(getChunkKey(x, y, z), new Chunk(noise, x, y, z));
                }
            }
        }
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
