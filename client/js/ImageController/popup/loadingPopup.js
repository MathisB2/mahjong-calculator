import {Popup} from "./popup.js";
import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class LoadingPopup extends Popup{

    constructor(overlay) {
        super(overlay);
        let title = new HtmlTag("h2");
        title.addText("Détéction des tuiles");
        this.panel.addChild(title);

        let img = new HtmlTag("img");
        img.setAttribute("class", "spinner");
        img.setAttribute("src","img/videos/spinner.svg");
        img.setAttribute("alt","loadingAnimation");
        this.panel.addChild(img);

        let p = new HtmlTag("p");
        p.addText("Veuillez patienter...");
        this.panel.addChild(p);
        this.update();
    }

}