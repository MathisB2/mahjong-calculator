package ScoreService;

import Tiles.FlowerTile;
import Tiles.Tile;
import Tiles.TileFactory;
import Tiles.WindTile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class MahjongHand {
    private ArrayList<MahjongSet> sets;
    private WindTile playerWind;
    private WindTile gameWind;
    ArrayList<String> settings;
    private ArrayList<FlowerTile> flowers;

    public MahjongHand(JSONObject hand) throws JSONException {
        this();

        JSONArray jsonSets = hand.getJSONArray("slotList");
        JSONArray jsonFlowers = hand.getJSONArray("playerFlowers");
        JSONArray jsonSettings = hand.getJSONArray("settings");
        TileFactory factory = new TileFactory();

        this.playerWind = (WindTile) factory.get(hand.getJSONObject("playerWind"));
        this.gameWind = (WindTile) factory.get(hand.getJSONObject("gameWind"));

        for(int i = 0; i < jsonFlowers.length(); ++i){
            FlowerTile flower = (FlowerTile) factory.get(jsonFlowers.getJSONObject(i));
            this.flowers.add(flower);
        }

        for (int i = 0; i < jsonSettings.length(); ++i){
            this.settings.add(jsonSettings.getString(i));
        }

        for(int i = 0; i < jsonSets.length(); ++i){
            JSONObject jsonSet = jsonSets.getJSONObject(i);
            this.addSet(new MahjongSet(jsonSet));
        }
    }
    public MahjongHand(WindTile playerWind, WindTile gameWind){
        this();
        this.playerWind = playerWind;
        this.gameWind = gameWind;
    }

    public MahjongHand(){
        this.sets = new ArrayList<>(5);
        this.settings = new ArrayList<>();
        this.flowers = new ArrayList<>();
    }
    public ArrayList<MahjongSet> getSets(){
        return sets;
    }
    public void addSet(MahjongSet set){
        assert !this.hasSet(set) : "can't add existing set";
        assert this.tilesCompatibleTo(set) : "can't add duplicated tiles";
        this.sets.add(set);
    }

    private boolean tilesCompatibleTo(MahjongSet set){
        for(Tile tile : set.getTiles()){
            for(MahjongSet storedSet : sets){
                if(storedSet.hasTile(tile)) return false;
            }
        }

        return true;
    }

    public void removeSet(MahjongSet set){
        assert this.hasSet(set) : "can't remove non-existent set";
        this.sets.remove(set);
    }


    public boolean hasSet(MahjongSet set){
        for(var storedSet: sets){
            if(storedSet == set) return true;
        }
        return false;
    }

    public boolean containsSetting(String settingName){
        return this.settings.contains(settingName);
    }
}