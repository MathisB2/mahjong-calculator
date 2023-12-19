export class Tile {
    type;
    img;
    name;
    id;
    shortenName;
    value;

    constructor(name,id) {
        this.name=name;
        this.id=id;
        this.img="img/tiles/"+name+".png";

        let split = name.split("_");
        this.shortenName = split[0];

        this.value = split[split.length-1]
    }

    draw(){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" draggable=\"true\" ";

        tmp += " style=\"background-image:url('";
        tmp += this.img;
        tmp += "')\"> </div>";

        return tmp;
    }

    drawUnavailable(opacity){
        let tmp = "<div class=\"tile\" id=\""+this.id+"\" draggable=\"true\"";

        tmp += " style=\"opacity:"+opacity+"%; background-image:url('";
        tmp += this.img;
        tmp += "')\"> </div>";

        return tmp;
    }

    copy(){
        let t=new Tile(this.name, this.id);

        t.type=this.type;
        t.img=this.img;
        t.shortenName=this.shortenName;
        t.value=this.value;

        return t;
    }
}