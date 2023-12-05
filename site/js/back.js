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


    copy(){
        let t=new Tile(this.name, this.id);
        t.type=this.type;
        t.img=this.img;
        return t;
    }


}



class Slot{
    tileList;
    slotId;
    constructor(id) {
        this.tileList = [];
        this.slotId=id;
    }

    drawSlot(active=false){
        let tmp="<section class='lineHand' id='slot"+this.slotId+"'>";

        for(let element of this.tileList) {
            tmp+=element.draw();
        }
        if(this.tileList.length<3 || (this.tileList.length==3 && this._isTriple())) {
            if(active){
                tmp += this._drawActiveAddButton()
            }else{
                tmp += this._drawAddButton();
            }

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

    _drawActiveAddButton(){
        return "<div class=\"activeEmptyTile\" id=\"bouton"+this.slotId+"\">+</div>";
    }


    _isTriple(){
        if(this.tileList.length!=3){
            return false;
        }
        let name=this.tileList[0].name;
        //console.log("lenght "+this.tileList.length);
        for(let element of this.tileList) {
            //console.log(element);
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
                //console.log("test"+this.tileList);
                this.tileList.splice(i,1);

                //console.log("test2"+this.tileList);
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

    canAdd(name){

        if(this._isTriple() && this.tileList[0].name==name){
            console.log("cas1");
            return true;
        }
        if(this.tileList.length<3){
            console.log("cas2");
            return true;
        }
        console.log("cas3");
        return false;
    }

    copy(){
        let s=new Slot(this.slotId);
        //s.tileList=this.tileList;
        for(let tile of this.tileList){
            s.tileList.push(tile.copy());
        }

        return s;
    }

}


class Hand{
    slotList;
    activeSlot;
    currentId;

    constructor() {
        this._initSlotList();

        this.activeSlot=0;
        this.currentId=0;
    }


    _initSlotList(){
        this.slotList = [];
        for(let i=0;i<5;i++){
            // this.slotList[i]=[];
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
                e.addEventListener("click", onTileClick)

                

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

    getActiveSlot(){
        return this.slotList[this.activeSlot];
    }



    setActive(i){
        this.activeSlot=i;
        console.log("Active slot : "+i);
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
                tile=JSON.parse(tile);
                /*

                console.log(tile.name);
                this.addTile(tile.name);

                removeTileFromDrawer(getAvailableTileByName(tile.name));

                 */
                tile=getAvailableTileByName(tile.name);
                if (tile!=null){
                    if(this.slotList[this.activeSlot].canAdd(tile.name)){
                        removeTileFromDrawer(tile.id);

                        this.addTileByTile(tile);
                    }
                }

            }
            this.activeSlot++;

        }
        this.activeSlot=0;
        this.drawHand();
    }


    clear(){
        delete this.slotList;
        this._initSlotList();
        this.setActive(0);
        this.drawHand();
        importTiles();


    }



}






function onSlotClick(event){
    maine.setActive(event.target.id.slice(-1));
    openDrawer();
    maine.drawHand()
}
function onTileClick(event){

    let id=event.target.id;
    let tile=maine.getTile(id);

    addTileToDrawer(tile);
    maine.removeTile(id);
    maine.drawHand();

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
        //console.log("ok");
        let id2 = dropZone.id;
        if(getAvailableTile(id)==null){
            console.log("drag :" + id + " and drop :" + id2);
            swapTiles(id,id2);
        }else{
            swapDrawerToHand(id,id2);
        }

    }else{
        //console.log("id "+id);
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

    if(maine.slotList[slotId.slice(-1)].canAdd(tile.name)){
        removeTileFromDrawer(id);
        maine.setActive(slotId.slice(-1));
        maine.addTileByTile(tile);
    }
    console.log(parseInt(slotId.slice(-1))+1);

    maine.drawHand();

}


function insertTile(id1,slotId){
    let tile = maine.getTile(id1);


    if(maine.slotList[slotId.slice(-1)].canAdd(tile.name)) {
        maine.removeTile(id1);
        maine.setActive(slotId.slice(-1));
        maine.addTileByTile(tile);
    }

    maine.drawHand();

}




function canSwap(tile1,tile2){
    console.log("slot"+maine.activeSlot);

    let copie=maine.getSlotFor(tile1).copy();
    console.log(copie);

    copie.deleteTile(tile1.id);
    console.log(copie);


    let copie2=maine.getSlotFor(tile2).copy();
    copie2.deleteTile(tile2.id);



    let resultat=copie.canAdd(tile2.name) && copie2.canAdd(tile1.name);
    return resultat;

}

function swapTiles(id1, id2){
    let tile = maine.getTile(id1);
    let tile2 = maine.getTile(id2);

    console.log("can swap"+canSwap(tile,tile2));
    if(canSwap(tile,tile2)){
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
    }


    maine.drawHand();
}



function swapDrawerToHand(id1,id2){
    let tile = getAvailableTile(id1);
    let tile2 = maine.getTile(id2);
    let tile3=tile2.copy();

    if(canSwap(tile,tile2)){
        addTileToDrawer(tile3);

        tile2.id=tile.id;
        tile2.name = tile.name;
        tile2.type = tile.type;
        tile2.img = tile.img;

        removeTileFromDrawer(tile.id);
    }



    maine.drawHand();
}

function onDrawerTileClick(event){

    let id=event.target.id;

    let name=getAvailableTile(id,true).name;


    //console.log("slot full :"+main.getActiveSlot().canAdd(name));


    if(getSizeByName(name)>1 && maine.getActiveSlot().canAdd(name)){
        let tile=getAvailableTile(id);
        maine.addTileByTile(tile);
        removeTileFromDrawer(id);
        maine.drawHand();
    }

}





function importTiles(){
    availableTiles=[];
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
                e.addEventListener("click", onDrawerTileClick);

            }
        }

    }
}


let availableTiles=[];


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
        //console.log(tile.name+"   "+availableTiles[i][0].name);
        if(availableTiles[i][0].name==tile.name){
            //console.log(availableTiles[i])
            let pos=0;
            if(availableTiles[i].length>1){
                pos=availableTiles[i].length-1
            }
            // availableTiles[i].push(tile);
            // console.log("tableau : "+availableTiles);
            availableTiles[i].splice(pos, 0, tile); //add the tile at index pos
            //console.log(availableTiles[i]);
        }
    }
}





let maine = new Hand();
maine.setActive(0);
// maine.addTile("bamboo_1");
//  maine.addTile("bamboo_1");
 // maine.addTile("bamboo_2");
 // maine.addTile("bamboo_2");

importTiles();
maine.drawHand();







