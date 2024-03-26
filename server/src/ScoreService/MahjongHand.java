package ScoreService;

import Tiles.*;
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
    private ArrayList<SeasonTile> seasons;

    public MahjongHand(JSONObject hand) throws JSONException {
        this();

        JSONArray jsonSets = hand.getJSONArray("slotList");
        JSONArray jsonFlowers = hand.getJSONArray("playerFlowers");
        JSONArray jsonSeasons = hand.getJSONArray("playerSeasons");
        JSONArray jsonSettings = hand.getJSONArray("settings");
        TileFactory factory = new TileFactory();

        this.playerWind = (WindTile) factory.get(hand.getJSONObject("playerWind"));
        this.gameWind = (WindTile) factory.get(hand.getJSONObject("gameWind"));

        for(int i = 0; i < jsonFlowers.length(); ++i){
            FlowerTile flower = (FlowerTile) factory.get(jsonFlowers.getJSONObject(i));
            this.flowers.add(flower);
        }
        for(int i = 0; i < jsonSeasons.length(); ++i){
            SeasonTile season = (SeasonTile) factory.get(jsonSeasons.getJSONObject(i));
            this.seasons.add(season);
        }

        for (int i = 0; i < jsonSettings.length(); ++i){
            this.settings.add(jsonSettings.getString(i));
        }

        for(int i = 0; i < jsonSets.length(); ++i){
            JSONObject jsonSet = jsonSets.getJSONObject(i);
            var slot = new MahjongSet(jsonSet);
            if(slot.size() == 0) continue;

            this.addSet(slot);
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
        this.seasons = new ArrayList<>();
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
    public ArrayList<FlowerTile> getFlowers() {
        return flowers;
    }

    public WindTile getPlayerWind() {
        return playerWind;
    }

    public WindTile getGameWind() {
        return gameWind;
    }

    public ArrayList<SeasonTile> getSeasons() {
        return seasons;
    }

    public ArrayList<BonusTile> getBonuses(){
        ArrayList<BonusTile> bonuses = (ArrayList<BonusTile>) this.seasons.clone();

        bonuses.addAll(this.flowers);
        return bonuses;
    }
}