import Tile from "./Tile.js";

class WindTile extends Tile{
    #direction;

    constructor(filename, direction) {
        super(filename, "wind");
        this.#direction = direction;
    }

    getValue() {
        return this.#direction;
    }

    toJSON(){
        return {
            type: this.getType(),
            direction: this.#direction,
            name: this.getFilename(),
        }
    }

    clone(){
        return new WindTile(this.getFilename(), this.#direction);
    }
}

export default WindTile