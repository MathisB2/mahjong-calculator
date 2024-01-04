package ScoreService;

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

    MahjongHand(JSONObject hand) throws JSONException {
        this.sets = new ArrayList(5);

        JSONArray jsonSets = hand.getJSONArray("slotList");
        TileFactory factory = new TileFactory();

        this.playerWind = (WindTile) factory.get(hand.getJSONObject("playerWind"));
        this.gameWind = (WindTile) factory.get(hand.getJSONObject("gameWind"));

        for(int i = 0; i < jsonSets.length(); ++i){
            JSONObject jsonSet = jsonSets.getJSONObject(i);
            this.addSet(new MahjongSet(jsonSet));
        }
    }
    MahjongHand(WindTile playerWind, WindTile gameWind){
        this.playerWind = playerWind;
        this.gameWind = gameWind;
        this.sets = new ArrayList(5);
    }
    public ArrayList<MahjongSet> getSets(){
        return sets;
    }
    public void addSet(MahjongSet set){
        assert !this.hasSet(set) : "can't add existing set";
        assert this.tilesCompatibleTo(set) : "can't add duplicated tiles";
        this.sets.add(set);
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

    private boolean tilesCompatibleTo(MahjongSet set){
        for(Tile tile : set.getTiles()){
            for(MahjongSet storedSet : sets){
                if(storedSet.hasTile(tile)) return false;
            }
        }

        return true;
    }
}