import {TileDirection} from "./TileDirection.js";

class TileConfig {
    filename;
    type;
    value;

    constructor(filename, type, value) {
        this.filename = filename;
        this.type = type;
        this.value = value;
    }
}

export const TilesConfig = [
    new TileConfig("dot_1", "dot", 1),
    new TileConfig("dot_2", "dot", 2),
    new TileConfig("dot_3", "dot", 3),
    new TileConfig("dot_4", "dot", 4),
    new TileConfig("dot_5", "dot", 5),
    new TileConfig("dot_6", "dot", 6),
    new TileConfig("dot_7", "dot", 7),
    new TileConfig("dot_8", "dot", 8),
    new TileConfig("dot_9", "dot", 9),

    new TileConfig("bamboo_1", "bamboo", 1),
    new TileConfig("bamboo_2", "bamboo", 2),
    new TileConfig("bamboo_3", "bamboo", 3),
    new TileConfig("bamboo_4", "bamboo", 4),
    new TileConfig("bamboo_5", "bamboo", 5),
    new TileConfig("bamboo_6", "bamboo", 6),
    new TileConfig("bamboo_7", "bamboo", 7),
    new TileConfig("bamboo_8", "bamboo", 8),
    new TileConfig("bamboo_9", "bamboo", 9),

    new TileConfig("character_1", "character", 1),
    new TileConfig("character_2", "character", 2),
    new TileConfig("character_3", "character", 3),
    new TileConfig("character_4", "character", 4),
    new TileConfig("character_5", "character", 5),
    new TileConfig("character_6", "character", 6),
    new TileConfig("character_7", "character", 7),
    new TileConfig("character_8", "character", 8),
    new TileConfig("character_9", "character", 9),

    new TileConfig("wind_east", "wind", TileDirection.EAST),
    new TileConfig("wind_west", "wind", TileDirection.WEST),
    new TileConfig("wind_north", "wind", TileDirection.NORTH),
    new TileConfig("wind_south", "wind", TileDirection.SOUTH),

    new TileConfig("dragon_red", "dragon", "red"),
    new TileConfig("dragon_green", "dragon", "green"),
    new TileConfig("dragon_white", "dragon", "white"),
]
export const getTileConfig = (filename) => {
    for(let tileConfig of TilesConfig){
        if(tileConfig.filename == filename) return tileConfig;
    }
}