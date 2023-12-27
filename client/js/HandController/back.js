import {ImageManager} from "../ImageController/ImageController.js";
import {Tile} from "./Tile.js";
import {Slot} from "./Slot.js";
import {openDrawer} from "./drawer.js";

const h = document.getElementById("hand");
const htmlDrawerTileList = document.getElementById("drawerTileList");
const trashButton = document.getElementById("trashButton");
const nextButton = document.getElementById("drawerNextButton");

let availableTiles = [];
let mahjongHand;

class Hand{
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
            h.innerHTML += this.slotList[i].drawSlot(i==this.activeSlot);
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
        this.setNextActive();
    }

    addTileByTile(tile){
        this.slotList[this.activeSlot].addTile(tile);
        this.setNextActive();
    }


    removeTile(id){
        let i=0;
        for (let element of this.slotList) {
            if (element.deleteTile(id)) {
                this.setActive(i)
            }
            i++;

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
        let element = document.getElementById("bouton"+i);
        if(element){
            element.scrollIntoView({ behavior: 'smooth'});
        }


    }

    setNextActive() {
        let case1 = this.getActiveSlot().tileList.length >= 4;
        let case2 = this.getActiveSlot().tileList.length == 3 && !this.getActiveSlot()._isTriple();
        let condition = this.activeSlot < this.slotList.length-1;

        if (condition && (case1 || case2)) {
            this.setActive(parseInt(this.activeSlot)+1);
            this.setNextActive();
        }
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

function toggleHidden(event){
    let id = event.target.id.slice(-1);

    mahjongHand.slotList[id].toggleHidden();
    mahjongHand.drawHand();
}

function onSlotClick(event){
    mahjongHand.setActive(event.target.id.slice(-1));
    openDrawer();
    mahjongHand.drawHand()
}
function onTileClick(event){
    let id=event.target.id;
    let tile=mahjongHand.getTile(id);

    addTileToDrawer(tile);

    mahjongHand.removeTile(id);
    mahjongHand.drawHand();
}

function drag(event){
    event.dataTransfer.setData("id",event.target.id);
}

function allowDrop(event) {
    event.preventDefault();
}

function drop(event){
    event.preventDefault();

    let id = event.dataTransfer.getData("id")
    let dropZone = event.target;

    if(dropZone.className=="tile"){
        let id2 = dropZone.id;

        if(getAvailableTile(id) == null){
            swapTiles(id,id2);
        }else{
            swapDrawerToHand(id,id2);
        }
    }else{
        let slot = dropZone.id;

        if(getAvailableTile(id) == null){
            insertTile(id,slot);
        }else{
            insertFromDrawer(id,slot);
        }
    }
}

function insertFromDrawer(id,slotId){
    let tile = getAvailableTile(id);

    if(mahjongHand.slotList[slotId.slice(-1)].canAdd(tile.name)){
        removeTileFromDrawer(id);
        mahjongHand.setActive(slotId.slice(-1));
        mahjongHand.addTileByTile(tile);
    }

    mahjongHand.drawHand();
}

function insertTile(id1,slotId){
    let tile = mahjongHand.getTile(id1);

    if(mahjongHand.slotList[slotId.slice(-1)].canAdd(tile.name)) {
        mahjongHand.removeTile(id1);
        mahjongHand.setActive(slotId.slice(-1));
        mahjongHand.addTileByTile(tile);
    }

    mahjongHand.drawHand();
}

function canSwap(tile1,tile2,drawer= false){
    let cope = mahjongHand.getSlotFor(tile1).copy();
    cope.deleteTile(tile1.id);

    if(drawer){
        return cope.canAdd(tile2.name)
    }

    let cope2 = mahjongHand.getSlotFor(tile2).copy();
    cope2.deleteTile(tile2.id);

    return cope.canAdd(tile2.name) && cope2.canAdd(tile1.name);

}

function swapTiles(id1, id2){
    let tile = mahjongHand.getTile(id1);
    let tile2 = mahjongHand.getTile(id2);
    if(!canSwap(tile,tile2)) return;

    let info;

    info = tile.name;
    tile.name = tile2.name;
    tile2.name = info;

    info = tile.type;
    tile.type = tile2.type;
    tile2.type = info;

    info = tile.img;
    tile.img = tile2.img;
    tile2.img = info;

    info = tile.id;
    tile.id = tile2.id;
    tile2.id = info;

    info = tile.shortenName;
    tile.shortenName = tile2.shortenName;
    tile2.shortenName = info;


    info = tile.value;
    tile.value = tile2.value;
    tile2.value = info;

    mahjongHand.drawHand();
}

function swapDrawerToHand(id1,id2){
    let tile = getAvailableTile(id1);
    let tile2 = mahjongHand.getTile(id2);
    let tile3=tile2.copy();

    if(!canSwap(tile2,tile,true)) return;

    addTileToDrawer(tile3);

    tile2.id=tile.id;
    tile2.name = tile.name;
    tile2.type = tile.type;
    tile2.img = tile.img;
    tile2.shortenName=tile.shortenName;
    tile2.value=tile.value;

    removeTileFromDrawer(tile.id);
    mahjongHand.drawHand();
}

function onDrawerTileClick(event){
    let id=event.target.id;
    let name=getAvailableTile(id,true).name;

    if(getSizeByName(name)>1 && mahjongHand.getActiveSlot().canAdd(name)){
        let tile=getAvailableTile(id);
        mahjongHand.addTileByTile(tile);
        removeTileFromDrawer(id);
        mahjongHand.drawHand();
    }

}

function importTiles(){
    availableTiles=[];
    let fileNames=[
        "dot_1", "dot_2", "dot_3", "dot_4", "dot_5", "dot_6", "dot_7", "dot_8", "dot_9",
        "bamboo_1", "bamboo_2", "bamboo_3", "bamboo_4", "bamboo_5", "bamboo_6", "bamboo_7", "bamboo_8", "bamboo_9",
        "character_1", "character_2", "character_3", "character_4", "character_5", "character_6", "character_7", "character_8", "character_9",
        "dragon_red", "dragon_green", "dragon_white"];
    let id=0;

    for(let tileIndex in fileNames) {
        let tmp=[]
        for(let i=0;i<5;i++){
            tmp.push(new Tile(fileNames[tileIndex], id))
            id++;
        }
        availableTiles.push(tmp);
    }
    drawDrawerTiles();

}

function drawDrawerTiles(){
    htmlDrawerTileList.innerHTML = "";

    for(let tileIndex in availableTiles){
        if(availableTiles[tileIndex].length<=1){
            htmlDrawerTileList.innerHTML+=availableTiles[tileIndex][0].drawUnavailable(50);
        }else{
            htmlDrawerTileList.innerHTML+=availableTiles[tileIndex][0].draw();
        }
    }

    for(let tileIndex in availableTiles){

        if(availableTiles[tileIndex].length>0){
            let e = document.getElementById(availableTiles[tileIndex][0].id);
            if(e){
                e.addEventListener("dragstart", drag);
                e.addEventListener("click", onDrawerTileClick);
            }
        }
    }
}


function getSizeByName(name){
    for(let i=0; i<availableTiles.length;i++){
        if(availableTiles[i][0].name==name){
            return availableTiles[i].length;
        }

    }
    return ""
}

function getAvailableTile(id,test){
    for(let machin of availableTiles){
        if(machin[0].id==id){
            if(test || machin.length>1)
            return machin[0];
        }
    }

    return null;
}
function getAvailableTileByName(name){
    for(let machin of availableTiles){
        if(machin[0].name==name){
            return machin[0];
        }
    }

    return null;
}


function removeTileFromDrawer(id){
    for(let i=0; i<availableTiles.length;i++){
        if(availableTiles[i].length>1){
            for(let j=0; j<availableTiles[i].length;j++){
                if(availableTiles[i][j].id==id){
                    availableTiles[i].splice(j,1);
                }
            }
        }
    }
}

function addTileToDrawer(tile){
    for(let i=0; i<availableTiles.length;i++){
        if(availableTiles[i][0].name==tile.name){
            let pos=0;

            if(availableTiles[i].length>1){
                pos=availableTiles[i].length-1
            }

            availableTiles[i].splice(pos, 0, tile);
        }
    }
}

function onTrashClick(){
    mahjongHand.clear()
    importTiles()
}

function onNextClick(){
    if(true){ //TODO check if tile-list is not empty
        console.log(mahjongHand.toString());
        localStorage.setItem("mahjongHand",mahjongHand.toString());
        window.location.href = "gameSettings.html";
    }
}

export async function startBack(){
    if((h && htmlDrawerTileList && trashButton && nextButton) == null) return;

    trashButton.addEventListener("click", onTrashClick);
    nextButton.addEventListener("click", onNextClick);

    mahjongHand = new Hand();
    mahjongHand.setActive(0);

    importTiles();
    mahjongHand.drawHand();

    let imageController = ImageManager.getController()
    imageController.OnTilesReceived.connect(mahjongHand.importDetectionResults);
}