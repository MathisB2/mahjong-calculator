import {tileDirection, tileType} from "./tileDirection.js";

export class BonusTile{
    type;
    direction;


    constructor(type, direction) {
        console.assert(this.#isValidDirection(direction),"Tile direction is not valid");
        console.assert(this.#isValidType(type),"Tile type is not valid");
        this.type = type;
        this.direction = direction;
    }

    #isValidDirection(direction){
        let isValid = false
        Object.keys(tileDirection).forEach(key => {
            if(tileDirection[key] == direction) isValid = true;
        });
        return isValid;
    }

    #isValidType(type){
        let isValid = false
        Object.keys(tileType).forEach(key => {
            if(tileType[key] == type) isValid = true;
        });
        return isValid;
    }

}