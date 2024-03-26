package Tiles;

import org.json.JSONException;
import org.json.JSONObject;

public class TileFactory {
    public Tile get(JSONObject jsonTile) throws JSONException {
        String tileType = jsonTile.getString("type");

        switch (tileType){
            case "bamboo": return new BambooTile(jsonTile.getInt("value"));
            case "character": return new CharacterTile(jsonTile.getInt("value"));
            case "dot": return new DotTile(jsonTile.getInt("value"));
            case "dragon": return new DragonTile(jsonTile.getString("color"));
            case "wind": return new WindTile(jsonTile.getString("direction"));
            case "flower": return new FlowerTile(jsonTile.getString("direction"));
            default: return new SeasonTile(jsonTile.getString("direction"));
        }
    }
}
