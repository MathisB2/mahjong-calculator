import {TileMatcher} from "./TileMatcher.js";
import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class ResultData{
    image;
    contours;
    clusters;
    matches;

    constructor(image, clusters) {
        this.image = image;
        this.contours = [];
        this.clusters = clusters;
        this.matches = this.getTileMatcherList();
    }

    getTileMatcherList(){
        let list = [];
        let i=0;
        for (let cluster of this.clusters) {
            for(let tile of cluster){
                list.push(new TileMatcher(tile.image,tile.name,i));
                i++;
            }
        }
        return list;
    }

    drawContourImage(canvas){
        canvas.width = this.image.width;
        canvas.height = this.image.height;
        let ctx = canvas.getContext("2d");
        //TODO : draw contours on the image

        const reader = new FileReader();
        reader.onload = function (e) {
            const img = new Image();
            img.src = e.target.result;
            img.onload = function () {
                canvas.width = img.width;
                canvas.height = img.height;

                if(img.height>img.width){
                    canvas.width = img.height;
                    canvas.height = img.width;

                    let rotationAngle = 90;
                    ctx.clearRect(0, 0, canvas.width, canvas.height);
                    ctx.save();
                    ctx.translate(canvas.width / 2, canvas.height / 2);
                    ctx.rotate((rotationAngle * Math.PI) / 180);
                    ctx.drawImage(img, -canvas.height / 2, -canvas.width / 2, canvas.height, canvas.width);
                    ctx.restore();
                }else{
                    ctx.drawImage(img,0,0,canvas.width,canvas.height);
                }
            };
        };
        reader.readAsDataURL(this.image);
    }

    getResultTableObject(){
        let section = document.createElement("section");
        let table = document.createElement("table");
        let thead = this.#getTableHead();
        let tbody = document.createElement("tbody");

        for (let row of this.matches) {
            tbody.appendChild(row.getHtmlObject());
        }

        table.appendChild(thead);
        table.appendChild(tbody);

        section.appendChild(table)
        let text = document.createElement("p");
        text.textContent = this.getTotalText();
        section.appendChild(text);
        return section;
    }

    getButtonObject(){
        let section = document.createElement("section");
        section.textContent ="Importer "+this.getCheckCount()+" "+this.#getTileText(this.getCheckCount());
        section.setAttribute("id", "resultButton");
        return section;
    }

    #getTileText(count){
        let text = "tuile";
        if(count>1) text+="s";
        return text;
    }

    getTotalText(){
        return "Total : "+this.getCheckCount()+"/"+this.matches.length+" "+this.#getTileText(this.matches.length);
    }

    #getTableHead(){
        let thead = document.createElement("thead");
        let tr = document.createElement("tr");

        let num = document.createElement("th");
        num.textContent = "N°";
        tr.appendChild(num);

        let img = document.createElement("th");
        img.textContent = "Img";
        tr.appendChild(img);

        let empty = document.createElement("th");
        tr.appendChild(empty);

        let tile = document.createElement("th");
        tile.textContent = "Tuile";
        tr.appendChild(tile);

        let name = document.createElement("th");
        name.textContent = "Nom";
        tr.appendChild(name);

        let empty2 = document.createElement("th");
        tr.appendChild(empty2);
        thead.appendChild(tr);
        return thead;
    }

    getCheckCount(){
        let count = 0;
        for (let match of this.matches) {
            if(match.isChecked()) count++;
        }
        return count;
    }
}