package Tiles;

import org.json.JSONException;
import org.json.JSONObject;

public class TileFactory {
    public Tile get(JSONObject jsonTile) throws JSONException {
        String tileType = jsonTile.getString("type");

        switch (tileType){
            case "BambooTile": return new BambooTile(jsonTile.getInt("value"));
            case "CharacterTile": return new CharacterTile(jsonTile.getInt("value"));
            case "DotTile": return new DotTile(jsonTile.getInt("value"));
            case "DragonTile": return new DragonTile(jsonTile.getString("value"));
            case "WindTile": return new WindTile(jsonTile.getString("value"));
            case "FlowerTile": return new FlowerTile();
            default: return new SeasonTile();
        }
    }
}
