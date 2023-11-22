const h=document.getElementById("hand");
class Tile {
    type;
    img;
    name;
    id;
    constructor(name,id) {
        this.name=name;
        this.id=id;
        this.img="img\\tiles\\dot_4.png";
    }

    drawTile(){
        if(this.name=="emptsy"){
            return "<div class=\"emptyTile\">+</div>";
        }else{
            console.log("img")
            let tmp="<div class=\"tile\"";
            tmp+= " style=\"background-image:url('";
            tmp+=this.img;
            tmp+="')> e</div>";

            return tmp;
        }


    }

}

class Slot{
    tileList;
    constructor() {
        this.tileList = [];
    }

    drawTiles(){
        //crée une section
        let tmp="<section class=\"lineHand\">";

        for(let element of this.tileList) {
            tmp+=element.drawTile();
        }

        tmp+="</section>";
        h.innerHTML +=tmp;


    }
    addTile(tile){
        this.tileList.push(tile);
    }

}

class Hand{
    slotList;

    constructor() {
        this.slotList = [];
        for(let i=0;i<5;i++){
            this.slotList[i]=new Slot();
        }
        this.initHand();
    }
    drawHand(){
        for (let element of this.slotList){
            element.drawTiles();
        }
    }

    initHand(){
        for(let i=0;i<14;i++){
            let tile=new Tile("empty",i);
            console.log(Math.floor(i/3));
            let index=Math.floor(i/3);
            this.slotList[index].addTile(tile);
        }
    }


}

const main = new Hand();
main.drawHand();



