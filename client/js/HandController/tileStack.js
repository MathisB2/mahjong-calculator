import {Tile} from "./hand/Tile.js";

export class TileStack {
    tile
    count

    constructor(name, count=4){
        this.tile = new Tile(name);
        this.count = count;
    }

    getHtmlObject(){
        return this.tile.getHtmlObject(this.count > 0);
    }

}