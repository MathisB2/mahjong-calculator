package Score;

public abstract class BasicTile extends AbstractTile{

    private int value;
    public BasicTile(String name,int v) {
        super(name);
        value=v;
    }
}
