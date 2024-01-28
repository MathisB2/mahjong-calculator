import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class PopupPanel extends HtmlTag{

    constructor() {
        super("section");
        this.setAttribute("class","popUp");
    }
}