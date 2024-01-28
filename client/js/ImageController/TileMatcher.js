import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class TileMatcher{
    image;
    name;

    num
    checked

    imageWidth;
    imageHeight;
    constructor(image64, name,num) {
        this.image = image64;
        this.name = name;
        this.num = num;
        this.checked = true;

        this.imageWidth = 280;
        this.imageHeight = 370;
    }
    getHtmlObject(){
        let row = new HtmlTag("tr");

        let num = new HtmlTag("td");
        num.addText(this.num);
        row.addChild(num);

        let tileCanvas = new HtmlTag("canvas");
        tileCanvas.addAttribute("class","tileResult");
        tileCanvas.addAttribute("id","extractedTile"+this.num);
        let tile = new HtmlTag("td");
        tile.addChild(tileCanvas)
        row.addChild(tile);

        let arrow = new HtmlTag("td");
        arrow.addText("→");
        row.addChild(arrow);

        let matchedCanvas = new HtmlTag("canvas");
        matchedCanvas.addAttribute("class","tileResult");
        matchedCanvas.addAttribute("id","matchedTile"+this.num);
        let matched = new HtmlTag("td");
        matched.addChild(matchedCanvas)
        row.addChild(matched);

        let label = new HtmlTag("label");
        label.addAttribute("for",this.#getId());
        label.addText(this.name);
        let name = new HtmlTag("td")
        name.addChild(label);
        row.addChild(name);

        let input = new HtmlTag("input");
        input.hasClosingTag = false;
        input.addAttribute("type","checkbox");
        input.addAttribute("name","result");
        if(this.checked) input.addAttribute("checked","true");
        input.addAttribute("id",this.#getId());
        let keep = new HtmlTag("td");
        keep.addChild(input);
        row.addChild(keep);

        return row;
    }

    #getId(){
        return "result"+this.num;
    }


    getImageAsBlob(){
        //TODO return image as blob
        return new Blob();
    }

    getTileAsBlob(){
        let url = "img/tiles/"+this.name+".png"
        //TODO : read the url and return the image as blob
        //tile size may be 600x800
        return new Blob();
    }
}