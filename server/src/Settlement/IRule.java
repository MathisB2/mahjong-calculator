package Settlement;

public interface IRule<T> {
    boolean isVerified(T param);
}
