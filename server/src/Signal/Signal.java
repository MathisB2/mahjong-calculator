package Signal;

import java.util.ArrayList;

public class Signal<T> {
    private ArrayList<Event> events;
    public Signal(){
        events = new ArrayList();
    }

    public void connect(Event event){
        events.add(event);
    }

    public void fire(T arg){
        for (Event event: events) {
            event.run(arg);
        }
    }
}
