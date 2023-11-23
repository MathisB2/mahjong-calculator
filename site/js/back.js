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
    }


    drawTile(){
        if(this.name=="empty"){
            this.drawEmpty()
        }else{
            this.drawBasic()
        }

    }
    drawBasic(){

        let tmp = "<div class=\"tile\" id=\""+this.id+"\" ";
        tmp += " style=\"background-image:url('";
        tmp += this.img;
        tmp += "')\"> </div>";

        return tmp;
    }
    drawEmpty(){
        return "<div class=\"emptyTile\" id=\""+this.id+"\">+</div>";
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
        let i=0;
        let y=true;
        for(let element of this.tileList) {
            if(element.name!="empty"){
                tmp+=element.drawBasic();
                i++;
                y=true;
            }else if (i<3 || y==true && i<4){
                tmp+=element.drawEmpty();
                y=false
                i++;
            }

        }

        tmp+="</section>";
        h.innerHTML +=tmp;


    }
    addTile(tile){
        this.tileList.push(tile);
    }
    updateTile(index)
    {

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
        for(let i=0;i<50;i++){

            let tile=new Tile("empty",i);
            console.log(Math.floor(i/3));
            let index=Math.floor(i/10);
            this.slotList[index].addTile(tile);
        }
    }


}

const main = new Hand();
main.slotList[0].tileList[0].name="test";
main.slotList[0].tileList[1].name="test";
// main.slotList[0].tileList[2].name="test";
// main.slotList[0].tileList[3].name="test";
main.drawHand();



