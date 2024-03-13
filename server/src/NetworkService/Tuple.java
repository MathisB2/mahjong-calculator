package NetworkService;

public class Tuple<A, B> {
    public final A obj1;
    public final B obj2;
    public Tuple(A arg1, B arg2){
        this.obj1 = arg1;
        this.obj2 = arg2;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "obj1=" + obj1 +
                ", obj2=" + obj2 +
                '}';
    }
}
