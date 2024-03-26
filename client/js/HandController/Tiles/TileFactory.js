import CommonTile from "./CommonTile.js";
import WindTile from "./WindTile.js";
import DragonTile from "./DragonTile.js";

let TileFactory = {
    get(tileConfig){
        switch (tileConfig.type) {
            case "dragon": return new DragonTile(tileConfig.filename,tileConfig.value);
            case "wind": return new WindTile(tileConfig.filename, tileConfig.value);
            default: return new CommonTile(tileConfig.filename, tileConfig.type, tileConfig.value);
        }
    }
}

export default TileFactory