package Settement;

public interface IRule<T> {
    boolean isVerified(T param);
}
