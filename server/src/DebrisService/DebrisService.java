package DebrisService;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DebrisService {
    static private DebrisService service = null;
    private ArrayList<Debris> debrisList;
    static public DebrisService get(){
        if(service == null) load();

        return service;
    }

    static public void load(){
        assert service == null : "DataService is already loaded";

        service = new DebrisService();
    }

    public DebrisService(){
        this.debrisList = new ArrayList<>();
    }

    public void insert(Debris debris){
        this.debrisList.add(debris);
    }

    public void remove(Debris debris){
        this.debrisList.remove(debris);
    }

    private void collect(){
        for(var debris : debrisList){

        }
    }
}
