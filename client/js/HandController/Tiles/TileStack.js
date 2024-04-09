import TileFactory from "./TileFactory.js";

export class TileStack {
    referredTile;
    count;
    maxValue;

    constructor(tileConfig, count=4){
        this.referredTile = TileFactory.get(tileConfig);
        this.count = count;
        this.maxValue = count;

        this.update();
    }

    getHtmlObject(){
        return this.referredTile.getHtmlObject();
    }
    increment(){
        ++this.count;
        this.update();
    }

    decrement(){
        console.assert(!this.isEmpty(), "can't be negative");
        --this.count;
        this.update();
    }

    isEmpty(){
        return this.count <= 0;
    }

    restore(){
        this.count = this.maxValue;
        this.update();
    }

    update(){

        let referredTile = this.referredTile.getHtmlObject();
        if(this.count > 0)
            referredTile.style.opacity = "1";
        else
            referredTile.style.opacity = "0.5";
    }
}