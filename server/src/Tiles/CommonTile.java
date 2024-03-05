package Tiles;

public class CommonTile extends Tile{
    public CommonTile(int value) {
        super(value);
    }

    @Override
    public boolean isHonor() {
        return value == 1 || value == 9;
    }
}
