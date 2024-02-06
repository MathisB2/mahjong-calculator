import {Signal} from "../../Signal/Signal.js";

export class Hand{
    slotList;
    activeSlot;
    currentId;
    tileClicked;

    constructor() {
        this.tileClicked = new Signal();

        this.slotList = [];
        this.activeSlot = 0;
        this.currentId = 0;
    }

    insert(tile){
        tile.getHTMLObject().addEventListener("Click", (() => {
            this.tileClicked.fire(tile);
        }).bind(this))
    }


    remove(tile){

    }

    has(tile){

    }

    getActiveSlot(){
        return this.slotList[this.activeSlot];
    }

    clear(){
    }

    toString(){
        return JSON.stringify(this);
    }
}