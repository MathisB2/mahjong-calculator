import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";
import {PopupPanel} from "./popupPanel.js";

export class Popup{
    overlay;
    panel;

    #isVisible

    constructor(overlay) {
        this.overlay = overlay
        this.panel = new PopupPanel();
        this.#isVisible = false;

        this.update();
    }

    update(){
        this.overlay.innerHTML = this.panel.toHtml();
    }


    show(){
        this.#isVisible = true;
        this.overlay.style.opacity = 1;
        this.panel.setAttribute("style","transform : none");
        this.update();
    }

    hide(){
        this.#isVisible = false;
        this.overlay.style.opacity = 0;
        this.panel.setAttribute("style","transform : scale(0)");
        this.update();
    }


}