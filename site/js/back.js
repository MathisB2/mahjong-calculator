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


    draw(){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" ";
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
        return "<div class=\"emptyTile\" id=\""+this.id+"\" onclick=\"test('test')\">+</div>";
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
    }

    addTile(name){
        this.slotList[this.activeSlot].addTile(new Tile(name,this.currentId));
        this.currentId++;
    }

    setActive(i){
        this.activeSlot=i;
    }


}



function test(name){
    // main.addTile(name);
    main.drawHand();
    openDrawer();
}

const main = new Hand();
main.setActive(0);

main.drawHand();







