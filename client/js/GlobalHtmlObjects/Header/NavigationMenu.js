import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class NavigationMenu{
    toHtml(){
        let span1 = new HtmlTag("span");
        span1.innerText = "Mahjong ";

        let span2 = new HtmlTag("span");
        span2.innerText = "Calculator";

        let div = new HtmlTag("div");
        div.setAttribute("class","textLogo");
        div.addChild(span1);
        div.addChild(span2);

        let label = new HtmlTag("label");
        label.setAttribute("id","openBurger");
        label.setAttribute("for","sideNavBox");

        let nav = new HtmlTag("nav");
        nav.addChild(label);
        nav.addChild(div);
        return nav.toHtml();
    }
}