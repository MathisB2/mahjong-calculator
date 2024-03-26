import Tile from "./Tile.js";

class CommonTile extends Tile{
    #value;

    constructor(filename, type, value) {
        super(filename, type);

        this.#value = value;
    }

    toJSON(){
        return {
            type: this.getType(),
            value: this.getValue(),
            name: this.getFilename(),
        }
    }

    getValue(){
        return this.#value;
    }

    clone(){
        return new CommonTile(this.getFilename(), this.getType(), this.getValue());
    }
}

export default CommonTile