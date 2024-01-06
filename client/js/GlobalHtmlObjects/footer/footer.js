import {globalLinks} from "../linkItem.js";
import {FooterItem} from "./footerItem.js";
import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

const footerObject= document.querySelector("footer");
export class Footer{
    itemList;
    bottomText;

    constructor() {
        this.itemList = [];
        this.bottomText = "Copyright ©2024";
        for (let link of globalLinks) {
            this.itemList.push(new FooterItem(link.text, link.url));
        }
    }


    toHtml(){
        let ul = new HtmlTag("ul");
        for (let item of this.itemList) {
            ul.addChild(item.getHtmlObject());
        }

        let span = new HtmlTag("span");
        span.innerText = this.bottomText;

        return ul.toHtml() + span.toHtml();
    }


}



export async function startFooter(){
    if(!footerObject) return

    let footer = new Footer();
    footerObject.innerHTML = footer.toHtml();
}