package Signal;

import java.util.ArrayList;
import java.util.Vector;

public class Signal<T> {
    private ArrayList<Event<T>> events;
    public Signal(){
        events = new ArrayList();
    }

    public Connection connect(Event<T> event){
        Connection conn = new Connection(() -> {
            events.remove(event);
        });
        events.add(event);

        return conn;
    }

    public void fire(T arg) throws Exception {
        for (Event<T> event: events) {
            event.run(arg);
        }
    }
    public void fireSafely(T arg) {
        for (Event<T> event: events) {
            try{
                event.run(arg);
            }catch(Exception e){
                System.err.println(e);
            }
        }
    }

    public void fireAsync(T arg) throws InterruptedException {
        Vector<Thread> threads = new Vector<>();
        for (Event<T> event: events) {
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
