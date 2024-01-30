import {Popup} from "./popup.js";
import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";
import {findChildById} from "../../GlobalHtmlObjects/HtmlObjects/elementFinder.js";

export class LoadingPopup extends Popup{

    constructor(overlay) {
        let panel = findChildById(overlay,"loadingPopUp");
        super(overlay,panel);
        let title = new HtmlTag("h2");
        title.addText("Détéction des tuiles");

        let img = new HtmlTag("img");
        img.setAttribute("class", "spinner");
        img.setAttribute("src","img/videos/spinner.svg");
        img.setAttribute("alt","loadingAnimation");

        let p = new HtmlTag("p");
        p.addText("Veuillez patienter...");

        this.panel.innerHTML= title.toHtml()+img.toHtml()+p.toHtml();
    }

}