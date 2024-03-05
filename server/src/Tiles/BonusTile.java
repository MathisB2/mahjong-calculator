package Tiles;

public class BonusTile extends Tile{
    public BonusTile(int value) {
        super(value);
    }
    @Override
    public boolean isHonor() {
        return false;
    }
}
