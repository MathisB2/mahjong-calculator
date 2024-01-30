import {Popup} from "./popup.js";
import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";
import {findChildById} from "../../GlobalHtmlObjects/HtmlObjects/elementFinder.js";

export class ResultPopup extends Popup{
    data;
    button;

    constructor(overlay, resultData) {
        let panel = findChildById(overlay,"resultPopUp");
        super(overlay,panel);
        this.data = resultData;
        console.log(resultData);

        let title = document.createElement("h2");
        title.textContent ="Résultats de la détéction";

        let canvas = document.createElement("canvas");
        canvas.setAttribute("id", "contourCanvas");

        let resultList = this.data.getResultTableObject();
        resultList.setAttribute("id", "resultList");

        this.button = this.data.getButtonObject();


        this.panel.appendChild(title);
        this.panel.appendChild(canvas);
        this.panel.appendChild(resultList);
        this.panel.appendChild(this.button);
        this.#drawHeaderImage()
        this.#initEvents();
    }


    #initEvents(){
        document.addEventListener("click", this.#updateModel.bind(this));
        this.button.addEventListener("click", this.#onButtonClick.bind(this));
    }

    #onButtonClick(){
        console.log("ok");
    }

    #updateModel(){
        console.log(this.data.getCheckCount());

    }

    #drawHeaderImage(){
        let canvas = findChildById(this.panel, "contourCanvas");
        this.data.drawContourImage(canvas);
    }


}