import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class FooterItem{
    text;
    url;


    constructor(text, url) {
        this.text=text;
        this.url=url;
    }

    getHtmlObject(){
        let a = new HtmlTag("a");
        a.innerText = this.text;
        a.setAttribute("href", this.url);

        let li = new HtmlTag("li");
        li.addChild(a);
        return li;
    }

}