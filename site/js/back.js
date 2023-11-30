const h=document.getElementById("hand");

class Tile {
    type;
    img;
    name;
    id;
    constructor(name,id) {
        this.name=name;
        this.id=id;
        this.img="img/tiles/dot_4.png";
        if(id==0){
            this.img="img/tiles/dot_3.png";
        }
        if(id==1){
            this.img="img/tiles/dot_4.png";
        }
        if(id==2){
            this.img="img/tiles/dot_2.png";
        }
    }


    draw(){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" draggable=\"true\" ";
        tmp += " style=\"background-image:url('";
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
        return "<div class=\"emptyTile\" id=\"bouton"+this.slotId+"\"  onclick=\"test('test')\">+</div>";
    }

    _isTriple(){
        let name=this.tileList[0].name;
        if(this.tileList.length!=3){
            return false;
        }
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
                console.log("test"+this.tileList);
                this.tileList.splice(i,1);

                console.log("test2"+this.tileList);
            }
            i++;
        }
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

        this.activeSlot=-1;
        this.currentId=0;
    }


    drawHand(){

        h.innerHTML="";
        for (let element of this.slotList){
            element.drawSlot();
            h.innerHTML+=element.drawSlot()

        }
        for (let element of this.slotList){
            for (let ele of element.tileList){
                let e=document.getElementById(ele.id);

                e.addEventListener("dragstart",drag);
                e.addEventListener("dragover",allowDrop);
                e.addEventListener("drop",drop);

            }
        }
        for (let i=0;i<5;i++){
            let e=document.getElementById("bouton"+i);
            if(e){
                e.addEventListener("dragover",allowDrop);
                e.addEventListener("drop",drop);
            }

        }

    }

    addTile(name){
        this.slotList[this.activeSlot].addTile(new Tile(name,this.currentId));
        ++this.currentId;
    }
    addTileByTile(tile){
        this.slotList[this.activeSlot].addTile(tile);
    }

    setActive(id){
        this.activeSlot=id;
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

    if(dropZone.className=="tile"){
        let id2 = dropZone.id;
        console.log("drag :" + id + " and drop :" + id2);
        swapTiles(id,id2);
    }else{
        let slot = dropZone.id;
        console.log("drag :"+id+" and drop on slot :"+slot);
        insertTile(id,slot)
    }

}
function insertTile(id1,slotId){
    let tile = main.getTile(id1);


    console.log(id1);
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

    main.drawHand();
}

const main = new Hand();
main.setActive(0);
main.addTile("feaff");
main.addTile("feaff");
main.setActive(1);
main.addTile("feaff");
main.addTile("feaff");


main.setActive(2);
main.addTile("feaff");
main.addTile("feaff");
main.addTile("feaff");

main.setActive(3);
main.addTile("feaff");
main.addTile("feaff");
main.addTile("feaff");
main.addTile("feaff");
main.setActive(4);
main.addTile("feaff");
main.addTile("feaff");
main.addTile("feaff");
main.addTile("feaff");
main.drawHand();
//main.removeTile(0);
main.drawHand();







