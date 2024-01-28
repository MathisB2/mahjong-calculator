import {TileMatcher} from "./TileMatcher.js";
import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class ResultData{
    image;
    contours;
    matches;

    constructor(image, JSONData) {
        this.image = image;
        this.contours = [];
        this.matches = [];
        //TODO read json to get contours and matches

        for (let i = 0; i < 20; i++) {
            this.matches.push(new TileMatcher("e","bamboo",i+1));
        }
    }


    getContourImageObject(){
        //TODO : draw contours on the image and return the canvas
    }

    getResultTableObject(){
        let section = new HtmlTag("section");
        let table = new HtmlTag("table");
        let thead = this.#getTableHead();
        let tbody = new HtmlTag("tbody");

        for (let row of this.matches) {
            tbody.addChild(row.getHtmlObject());
        }

        table.addChild(thead);
        table.addChild(tbody);

        section.addChild(table)
        section.addText("Total : "+this.#getCheckCount()+"/"+this.matches.length+" "+this.#getTileText(this.matches.length));

        return section
    }


    getButtonObject(){
        let section = new HtmlTag("section");
        section.addText("Importer "+this.#getCheckCount()+" "+this.#getTileText(this.matches.length));
        section.setAttribute("id", "resultButton");
        return section;
    }

    #getTileText(count){
        let text = "tuile";
        if(count>0) text+="s";
        return text;
    }

    #getTableHead(){
        let thead = new HtmlTag("thead");
        let tr = new HtmlTag("tr");

        let num = new HtmlTag("th");
        num.addText("N°");
        tr.addChild(num);

        let img = new HtmlTag("th");
        img.addText("Img");
        tr.addChild(img);

        let empty = new HtmlTag("th");
        tr.addChild(empty);

        let tile = new HtmlTag("th");
        tile.addText("Tuile");
        tr.addChild(tile);

        let name = new HtmlTag("th");
        name.addText("Nom");
        tr.addChild(name);

        tr.addChild(empty);
        thead.addChild(tr);
        return thead;
    }

    #getCheckCount(){
        let count = 0;
        for (let match of this.matches) {
            if(match.checked) count++;
        }
        return count;
    }
}