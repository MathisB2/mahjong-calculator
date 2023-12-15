package ScoreService;

import Tiles.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MahjongSet {
    ArrayList<Tile> tiles;
    boolean hidden;

    MahjongSet(){
        this.tiles = new ArrayList();
    }
    MahjongSet(JSONObject jsonSet) throws JSONException {
        this.tiles = new ArrayList();
        this.hidden = jsonSet.getBoolean("hidden");
        JSONArray jsonTiles = jsonSet.getJSONArray("Tiles");

        for(int i = 0; i < jsonTiles.length(); ++i){
            JSONObject jsonTile = jsonTiles.getJSONObject(i);
            this.addTile(TileFactory.get(jsonTile));
        }
    }

    MahjongSet(ArrayList<Tile> tiles){
        assert tiles.size() <= 4 : "can't have a group with more than 4 tiles";
        assert tiles.size() >= 2 : "can't have a group with less than 2 tiles";
        this.tiles = tiles;
    }

    public void addTile(Tile tile){
        assert tiles.size() <= 4 : "can't have a group with more than 4 tiles";
        assert !this.hasTile(tile) : "can't add existing tile";
        this.tiles.remove(tile);
    }

    public boolean isHidden(){
        return this.hidden;
    }

    public void removeTile(Tile tile){
        assert this.hasTile(tile) : "can't remove non-existent tile";
        this.tiles.remove(tile);
    }
    public boolean hasTile(Tile tile){
        for(var storedTile : tiles){
            if(storedTile == tile) return true;
        }

        return false;
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }
}
