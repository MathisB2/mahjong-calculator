import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class Tile {
    type;
    img;
    name;
    id;
    value;

    constructor(name,id) {
        this.name=name;
        this.id=id;
        this.img="img/tiles/"+name+".png";

        let split = name.split("_");
        this.type = split[0];
        this.value = split[1]
    }

    getHtmlObject(available = true){
        let div = new HtmlTag("div");
        div.setAttribute("class", "tile");
        div.setAttribute("id" , this.id);
        let style = "background-image:url('"+this.img+"')";
        if(!available) style += "; opacity:50%;";

        div.setAttribute("style",style);
        return div;
    }

    copy(){
        let t=new Tile(this.name, this.id);

        t.type=this.type;
        t.img=this.img;
        t.value=this.value;

        return t;
    }
}