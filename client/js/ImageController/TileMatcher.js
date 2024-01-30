import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class TileMatcher{
    image;
    name;

    num
    checked

    imageWidth;
    imageHeight;
    constructor(image, name,num) {
        this.image = image;
        this.name = name;
        this.num = num;
        this.checked = true;

        this.imageWidth = 280;
        this.imageHeight = 370;
    }


    getHtmlObject(){
        let row = document.createElement("tr");
        let num = document.createElement("td");
        num.textContent = this.num;
        row.appendChild(num);

        let extractedImg = this.image;
        extractedImg.setAttribute("class", "tileResult");
        extractedImg.setAttribute("id", "extractedTile"+this.name);
        let tile = document.createElement("td");
        tile.appendChild(extractedImg);
        row.appendChild(tile);

        let arrow = document.createElement("td");
        arrow.textContent = "→";
        row.appendChild(arrow);

        let tileImg = this.getTileImage();
        tileImg.setAttribute("class","tileResult");
        tileImg.setAttribute("id","matchedTile"+this.num);


        let matched = document.createElement("td");

        matched.appendChild(tileImg);
        row.appendChild(matched);

        let label = document.createElement("label");
        label.setAttribute("for", this.#getId());
        label.textContent = this.name;
        let name = document.createElement("td")
        name.appendChild(label);
        row.appendChild(name);

        let input = document.createElement("input");
        // input.hasClosingTag = false;
        input.setAttribute("type","checkbox");
        input.setAttribute("name","result");
        if(this.checked) input.setAttribute("checked","true");
        input.setAttribute("id",this.#getId());
        let keep = document.createElement("td");
        keep.appendChild(input);
        row.appendChild(keep);

        return row;
    }

    #getId(){
        return "result"+this.num;
    }


    getTileImage(){
        let url = "img/tiles/"+this.name+".png"
        let image = new Image();
        image.src = url;
        return image;
    }
}