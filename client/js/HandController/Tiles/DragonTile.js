import Tile from "./Tile.js";


class DragonTile extends Tile{
    #color;

    constructor(filename, color) {
        super(filename, "dragon");
        this.#color = color;
    }

    getValue(){
        return this.#color;
    }

    toJSON() {
        return {
            type: this.getType(),
            color: this.#color,
            name: this.getFilename(),
        }
    }

    clone(){
        return new DragonTile(this.getFilename(), this.getValue());
    }
}

export default DragonTile;