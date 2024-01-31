import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class TileMatcher{
    image;
    name;

    num;
    checkbox;

    constructor(image, name,num) {
        this.image = image;
        this.name = name;
        this.num = num;

        this.checkbox = document.createElement("input");
        this.checkbox.setAttribute("type","checkbox");
        this.checkbox.setAttribute("name","result");
        this.checkbox.setAttribute("checked","true");
        this.checkbox.setAttribute("id",this.#getId());
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
        let extractedLabel = this.#getLabel();
        extractedLabel.appendChild(extractedImg);
        tile.appendChild(extractedLabel);
        row.appendChild(tile);

        let arrow = document.createElement("td");
        arrow.textContent = "→";
        row.appendChild(arrow);

        let tileImg = this.getTileImage();
        tileImg.setAttribute("class","tileResult");
        tileImg.setAttribute("id","matchedTile"+this.num);

        let matched = document.createElement("td");
        let matchedLabel = this.#getLabel();
        matchedLabel.appendChild(tileImg);
        matched.appendChild(matchedLabel);
        row.appendChild(matched);

        let name = document.createElement("td")
        let nameLabel = this.#getLabel();
        nameLabel.textContent = this.name;
        name.appendChild(nameLabel);
        row.appendChild(name);


        let keep = document.createElement("td");
        keep.appendChild(this.checkbox);
        row.appendChild(keep);

        return row;
    }

    #getId(){
        return "result"+this.num;
    }

    #getLabel(){
        let label = document.createElement("label");
        label.setAttribute("for", this.#getId());
        return label;
    }


    getTileImage(){
        let url = "img/tiles/"+this.name+".png"
        let image = new Image();
        image.src = url;
        return image;
    }

    isChecked(){
        return this.checkbox.checked;
    }
}