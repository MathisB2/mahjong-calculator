package Tiles;

public class BonusTile extends Tile{
    String windDirection;
    public BonusTile(String windDirection) {
        super(-1);
        this.windDirection = windDirection;
    }

    public String getWindDirection() {
        return windDirection;
    }

    @Override
    public boolean isHonor() {
        return false;
    }
}
