package Tiles;

public class WindTile extends Tile{
    private String windDirection;
    WindTile(String windDirection) {
        super(-1);
        this.windDirection = windDirection;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public boolean isDirection(String windDirection){
        return this.windDirection.equals(windDirection);
    }

    @Override
    public boolean isHonor() {
        return true;
    }
}
