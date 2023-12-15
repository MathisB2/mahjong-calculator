package Tiles;

import org.json.JSONException;
import org.json.JSONObject;

public class TileFactory {
    public static Tile get(JSONObject jsonTile) throws JSONException {
        String tileType = jsonTile.getString("Tile");

        switch (tileType){
            case "WindTile": return new WindTile();
            default: return new DragonTile();
        }
    }
}
