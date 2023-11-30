const h=document.getElementById("hand");
const htmlDrawerTileList=document.getElementById("drawerTileList");

class Tile {
    type;
    img;
    name;
    id;
    constructor(name,id) {
        this.name=name;
        this.id=id;
        this.img="img/tiles/"+name+".png";
    }



    draw(){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" draggable=\"true\" ";
        tmp += " style=\"background-image:url('";
        tmp += this.img;
        tmp += "')\"> </div>";
        return tmp;
    }

    drawUnavailable(opacity){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" ";
        tmp += " style=\"opacity:"+opacity+"%; background-image:url('";
        tmp += this.img;
        tmp += "')\"> </div>";
        return tmp;
    }


}

class Slot{
    tileList;
    slotId;
    constructor(id) {
        this.tileList = [];
        this.slotId=id;
    }

    drawSlot(){
        let tmp="<section class='lineHand' id='slot"+this.slotId+"'>";

        for(let element of this.tileList) {
            tmp+=element.draw();
        }
        if(this.tileList.length<3 || (this.tileList.length==3 && this._isTriple())) {
            tmp += this._drawAddButton();
        }
        tmp+="</section>";
        return tmp;


    }
    addTile(tile){
        this.tileList.push(tile);
    }

    _drawAddButton(){
        return "<div class=\"emptyTile\" id=\"bouton"+this.slotId+"\">+</div>";
    }


    _isTriple(){
        let name=this.tileList[0].name;
        if(this.tileList.length!=3){
            return false;
        }
        console.log("lenght "+this.tileList.length);
        for(let element of this.tileList) {
            console.log(element);
            if(element.name!=name){
                return false;
            }
        }
        return true;
    }

    deleteTile(id){
        let i=0;
        for(let element of this.tileList) {
            //console.log(element);
            if(element.id==id){
                console.log("test"+this.tileList);
                this.tileList.splice(i,1);

                console.log("test2"+this.tileList);
            }
            i++;
        }
    }


    find(id){
        for (let tile of this.tileList){
            if(id==tile.id){
                return true;
            }
        }
        return false;
    }



}


class Hand{
    slotList;
    activeSlot;
    currentId;

    constructor() {
        this.slotList = [];
        for(let i=0;i<5;i++){
            this.slotList[i]=new Slot(i);
        }

        this.activeSlot=0;
        this.currentId=0;
    }


    drawHand() {
        h.innerHTML = "";
        for (let element of this.slotList) {
            element.drawSlot();
            h.innerHTML += element.drawSlot()

        }
        for (let element of this.slotList) {
            for (let ele of element.tileList) {
                let e = document.getElementById(ele.id);

                e.addEventListener("dragstart", drag);
                e.addEventListener("dragover", allowDrop);
                e.addEventListener("drop", drop);

            }
        }
        for (let i = 0; i < 5; i++) {
            let e = document.getElementById("bouton" + i);
            if (e) {
                e.addEventListener("dragover", allowDrop);
                e.addEventListener("drop", drop);
                e.addEventListener("click", onSlotClick)
            }


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
                if(ele.id==id){
                    return ele;
                }
            }
        }
        return null;
    }



    setActive(i){
        this.activeSlot=i;
        console.log("Active slot : "+i);
    }


}






function onSlotClick(id){
    main.setActive(id);
    openDrawer();
}



function drag(event){
    console.log("Drag Start - Element ID:", event.target.id);
    event.dataTransfer.setData("id",event.target.id);
}
function allowDrop(event) {
    event.preventDefault();
}
function drop(event){
    event.preventDefault();

    let id=event.dataTransfer.getData("id")
    let dropZone = event.target;
    console.log("classe "+dropZone.className);
    if(dropZone.className=="tile"){
        console.log("ok");
        let id2 = dropZone.id;
        if(getAvailableTile(id)==null){
            console.log("drag :" + id + " and drop :" + id2);
            swapTiles(id,id2);
        }else{
            swapDrawerToHand(id,id2);
        }

    }else{
        console.log("id "+id);
        let slot = dropZone.id;
        if(getAvailableTile(id)==null){
            // console.log("drag :"+id+" and drop on slot :"+slot);
            insertTile(id,slot);
        }else{
            insertFromDrawer(id,slot);
        }
    }

}



function insertFromDrawer(id,slotId){

    let tile=getAvailableTile(id);
    removeAvailableTile(id);
    console.log("infos:")
    console.log(tile.name);
    console.log(tile.id);
    main.setActive(slotId.slice(-1));

    console.log(tile);
    main.addTileByTile(tile);
    // main.addTile("bamboo_1");
    main.drawHand();

}

function insertTile(id1,slotId){
    let tile = main.getTile(id1);
    console.log("tile "+tile);
    // console.log(id1);
    main.removeTile(id1);
    main.setActive(slotId.slice(-1));
    main.addTileByTile(tile);
    main.drawHand();

}
function swapTiles(id1, id2){
    let tile = main.getTile(id1);
    let tile2 = main.getTile(id2);

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

    main.drawHand();
}



function swapDrawerToHand(id1,id2){
    let tile = getAvailableTile(id1);
    let tile2 = main.getTile(id2);
    console.log(tile);

    addAvailableTile(tile2,tile2.name);

    tile2.name = tile.name;
    tile2.type = tile.type;
    tile2.img = tile.img;
    tile2.id=tile.id;

    removeAvailableTile(tile.id);

    console.log(tile2.name);
    main.drawHand();
}

function onDrawerTileClick(){
    // main.addTile();
}





function importTiles(){
    let folder="img\/tiles\/"
    let fileExtension=".png";
    let fileNames=[
        "dot_1", "dot_2", "dot_3", "dot_4", "dot_5", "dot_6", "dot_7", "dot_8", "dot_9",
        "bamboo_1", "bamboo_2", "bamboo_3", "bamboo_4", "bamboo_5", "bamboo_6", "bamboo_7", "bamboo_8", "bamboo_9",
        "character_1", "character_2", "character_3", "character_4", "character_5", "character_6", "character_7", "character_8", "character_9",
        "wind_east", "wind_south", "wind_west", "wind_north",
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
    htmlDrawerTileList.innerHTML="";
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
            }
        }

    }
}


let availableTiles=[];




function getAvailableTile(id){
    for(let machin of availableTiles){
        if(machin[0].id==id && machin.length>1){
            return machin[0];
        }
    }
    return null;
}


function removeAvailableTile(id){
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

function addAvailableTile(tile,name){
    for(let i=0; i<availableTiles.length;i++){
        if(availableTiles[i].length>1){
            let pos=availableTiles[i].length-1
        }else{
            let pos=0;
        }
        if(availableTiles[i][0].name==name){


            // availableTiles[i].push(tile);
            console.log("tableau : "+availableTiles);
            availableTiles[i].splice(pos, 0, tile); //add the tile at index pos
            //console.log(availableTiles[i]);
        }
    }
}



const main = new Hand();
main.setActive(0);
// main.addTile("bamboo_1");
// main.addTile("bamboo_1");
// main.addTile("bamboo_2");
importTiles();
main.drawHand();






