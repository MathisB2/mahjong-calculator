import {Popup} from "./Popup.js";
import {TileManager} from "../../HandController/TileController.js";

export class ResultPopup extends Popup{
    data;
    canvas;
    button;
    resultList;

    constructor(parent, resultData) {
        let content = document.createElement("section");
        content.setAttribute("class","popUpOverlay");
        content.setAttribute("id","resultOverlay");

        let panel = document.createElement("section");
        panel.setAttribute("class","popUp");
        panel.setAttribute("id","resultPopUp");


        let title = document.createElement("h2");
        title.textContent ="Résultats de la détéction";
        panel.appendChild(title);


        let canvas = document.createElement("canvas");
        canvas.setAttribute("id", "contourCanvas");
        panel.appendChild(canvas);

        let resultList = resultData.getResultTableObject();
        resultList.setAttribute("id","resultList");
        panel.appendChild(resultList);

        let button = resultData.getButtonObject();
        panel.appendChild(button);

        content.appendChild(panel);
        super(parent, content);

        this.data = resultData;
        this.canvas = canvas;
        this.resultList = resultList;
        this.button = button;

        this.#drawHeaderImage()
        this.#initEvents();
    }


    #initEvents(){
        this.content.firstChild.addEventListener("click", this.update.bind(this));
        this.button.addEventListener("click", this.#onButtonClick.bind(this));
    }

    #onButtonClick(){
        TileManager.get().importTiles( this.data.getClusters());

        this.hide().then();
    }

    update(){
        this.resultList.lastChild.textContent = this.data.getTotalText();
        this.button.textContent = this.data.getButtonObject().textContent;
    }

    #drawHeaderImage(){
        this.data.drawContourImage(this.canvas);
    }


}