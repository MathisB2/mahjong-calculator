import {Tile} from "./hand/Tile.js";

export class TileStack {
    tile
    count

    constructor(name, count=4){
        this.tile = new Tile(name);
        this.count = count;
    }

    getHtmlObject(){
        this.update();
        return this.tile.getHtmlObject();;
    }
    increment(){
        ++this.count;
        this.update();
    }

    decrement(){
        console.assert(this.count > 0, "can't be negative")
        --this.count;
        this.update();
    }
    update(){
        let tile = this.tile.getHtmlObject();
        if(this.count > 0)
            tile.style.opacity = "1";
        else
            tile.style.opacity = "0.5";

    }
}