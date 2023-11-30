package Signal;

import org.json.JSONException;

public interface Event<T> {
    void run(T parameter) throws Exception;
}
