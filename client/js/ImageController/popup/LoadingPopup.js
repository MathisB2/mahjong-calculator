import {Popup} from "./Popup.js";
import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";
import {findChildById} from "../../GlobalHtmlObjects/HtmlObjects/elementFinder.js";

export class LoadingPopup extends Popup{

    constructor(parent) {
        let content = document.createElement("section");
        content.setAttribute("class","popUpOverlay");
        content.setAttribute("id","loadingOverlay");

        let panel = document.createElement("section");
        panel.setAttribute("class","popUp");
        panel.setAttribute("id","loadingPopUp");

        let title = document.createElement("h2");
        title.textContent = "Détection des tuiles";
        panel.appendChild(title);

        let img = document.createElement("img");
        img.setAttribute("class", "spinner");
        img.setAttribute("src","img/videos/spinner.svg");
        img.setAttribute("alt","loadingAnimation");
        panel.appendChild(img);

        let p = document.createElement("p");
        p.textContent = "Veuillez patienter...";
        panel.appendChild(p);

        content.appendChild(panel);
        super(parent, content);
    }

}