package Signal;

public interface Event<T> {
    void run(T parameter);
}
