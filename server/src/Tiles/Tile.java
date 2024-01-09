package Tiles;

import java.util.Objects;

public abstract class Tile {
    public int value;
    Tile(int value){
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile tile)) return false;
        return value == tile.value && tile.getClass() == this.getClass();
    }

    @Override
    public String toString() {
        return "Tile{" +
                "value=" + value +
                '}';
    }
}
