package Signal;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Vector;

public class Signal<T> {
    private ArrayList<Event> events;
    public Signal(){
        events = new ArrayList();
    }

    public Connection connect(Event event){
        Connection conn = new Connection(() -> {
            events.remove(event);
        });
        events.add(event);

        return conn;
    }

    public void fire(T arg) throws Exception {
        for (Event event: events) {
            event.run(arg);
        }
    }

    public void fireAsync(T arg) throws InterruptedException {
        Vector<Thread> threads = new Vector<>();
        for (Event event: events) {
            Thread th = new Thread(()->{
                try {
                    event.run(arg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            threads.add(th);
            th.start();
        }

        for(Thread th : threads)
            th.join();
    }
}
