import {TileDirection} from "./TileDirection.js";
import {TileType} from "./TileType.js";
import Tile from "./Tile.js";

export class BonusTile extends Tile{
    type;
    direction;

    constructor(type, direction) {
        super("", type)
        console.assert(this.#isValidDirection(direction),"Tile direction is not valid");
        console.assert(this.#isValidType(type),"Tile type is not valid");

        this.type = type;
        this.direction = direction;
    }

    #isValidDirection(direction){
        let isValid = false
        Object.keys(TileDirection).forEach(key => {
            if(TileDirection[key] == direction) isValid = true;
        });
        return isValid;
    }

    #isValidType(type){
        let isValid = false
        Object.keys(TileType).forEach(key => {
            if(TileType[key] == type) isValid = true;
        });
        return isValid;
    }

    toJSON(){
        return {
            type: this.type,
            direction: this.direction
        };
    }
}