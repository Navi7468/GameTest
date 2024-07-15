package com.navi7468.world;

import java.util.HashMap;
import java.util.Map;

public class BlockType {
    public static final int AIR = 0;
    public static final int DIRT = 1;
    public static final int GRASS = 2;
    public static final int STONE = 3;
    public static final int BEDROCK = 4;

    private static final Map<Integer, BlockTextures> blockTextures = new HashMap<>();

    static {
        blockTextures.put(DIRT, new BlockTextures("textures/blocks/dirt.png"));
        blockTextures.put(GRASS, new BlockTextures("textures/blocks/grass_side.png", "textures/blocks/grass_top.png", "textures/blocks/dirt.png"));
        blockTextures.put(STONE, new BlockTextures("textures/blocks/stone.png"));
        blockTextures.put(BEDROCK, new BlockTextures("textures/blocks/bedrock.png"));

        // Debug: Log the texture assignments
        System.out.println("Assigned textures:");
        blockTextures.forEach((type, textures) -> {
            System.out.println("Block Type: " + type + ", Textures: [side: " + textures.side + ", top: " + textures.top + ", bottom: " + textures.bottom + "]");
        });
    }

    public static BlockTextures getBlockTextures(int blockType) {
        BlockTextures textures = blockTextures.getOrDefault(blockType, new BlockTextures("textures/blocks/error.png"));

        // Debug: Log texture retrieval
        System.out.println("Retrieved textures for block type " + blockType + ": [side: " + textures.side + ", top: " + textures.top + ", bottom: " + textures.bottom + "]");

        return textures;
    }
}
