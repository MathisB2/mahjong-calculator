import {Slot} from "./Slot.js";
import {Tile} from "./Tile.js";

export class Hand{
    slotList;
    activeSlot;
    currentId;

    gameWind="gameNorth";
    playerWind="playerNorth";
    playerFlowers=[];
    playerSeasons=[];

    constructor() {
        this._initSlotList();

        this.activeSlot=0;
        this.currentId=0;
    }

    _initSlotList(){
        this.slotList = [];
        for(let i=0;i<5;i++){
            this.slotList[i]=new Slot(i);
        }
    }

    drawHand() {
        h.innerHTML = "";
        for (let i=0;i<this.slotList.length;i++) {
            h.innerHTML += this.slotList[i].drawSlot(i===this.activeSlot);
        }

        for (let element of this.slotList) {
            for (let ele of element.tileList) {
                let e = document.getElementById(ele.id);

                e.addEventListener("dragstart", drag);
                e.addEventListener("dragover", allowDrop);
                e.addEventListener("drop", drop);
                e.addEventListener("click", onTileClick);
            }
        }

        for (let i = 0; i < 5; i++) {
            let e = document.getElementById("bouton" + i);
            if (e) {
                e.addEventListener("dragover", allowDrop);
                e.addEventListener("drop", drop);
                e.addEventListener("click", onSlotClick);
            }
        }

        for (let i=0;i<5;i++){
            let e=document.getElementById("check"+i);
            if(e == null) continue;

            e.addEventListener("click",toggleHidden);
        }

        drawDrawerTiles();
    }

    addTile(name){
        this.slotList[this.activeSlot].addTile(new Tile(name,this.currentId));
        this.currentId++;
    }

    addTileByTile(tile){
        this.slotList[this.activeSlot].addTile(tile);
    }


    removeTile(id){
        for (let element of this.slotList){
            element.deleteTile(id);
        }
    }

    getTile(id){
        for (let element of this.slotList){
            for (let ele of element.tileList){
                if(ele.id != id) continue

                return ele;
            }
        }

        return null;
    }

    getActiveSlot(){
        return this.slotList[this.activeSlot];
    }



    setActive(i){
        this.activeSlot = i;
    }

    getSlotFor(t){
        for (let slot of this.slotList){
            for (let tile of slot.tileList){
                if(t.id==tile.id){
                    return slot;
                }
            }
        }

        return null;
    }


    importDetectionResults(callback){
        this.clear();
        let obj = JSON.parse(callback);
        this.activeSlot=0;

        for(let cluster of obj){
            for(let tile of cluster){
                tile = JSON.parse(tile)
                tile = getAvailableTileByName(tile.name);
                if (tile == null) continue;

                if(this.slotList[this.activeSlot].canAdd(tile.name)){
                    removeTileFromDrawer(tile.id);

                    this.addTileByTile(tile);
                }
            }
            ++this.activeSlot;
        }
        this.activeSlot = 0;
        this.drawHand();
    }

    clear(){
        delete this.slotList;

        this._initSlotList();
        this.setActive(0);
        this.drawHand();

        importTiles();
    }

    toString(){
        return JSON.stringify(this);
    }
}