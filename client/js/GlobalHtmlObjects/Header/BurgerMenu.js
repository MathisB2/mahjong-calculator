import {HtmlTag} from "../HtmlObjects/HtmlTag.js";
import {BurgerMenuItem} from "./BurgerMenuItem.js";
import {projectConfig} from "../../config.js";

export class BurgerMenu{

    closeText;
    itemsList;


    constructor() {

        this.closeText="Fermer";
        this.itemsList=[];

        this.itemsList.push(new BurgerMenuItem("Accueil","index.html","img/icons/home.svg"))
        this.itemsList.push(new BurgerMenuItem("Calculatrice","calculator.html","img/icons/calculator.svg"))
        this.itemsList.push(new BurgerMenuItem("Règles du jeu","","img/icons/rules.svg"))
        this.itemsList.push(new BurgerMenuItem("À propos","","img/icons/info.svg"))

    }



    #getOverlay(){
        let div = new HtmlTag("div");
        div.setAttribute("id","sideNavOverlay");

        let label = new HtmlTag("label");
        label.setAttribute("for","sideNavBox");
        label.addChild(div);
        return label;
    }


    #getCloseSection(){
        let span1 = new HtmlTag("span");
        span1.innerText = this.closeText;

        let span2 = new HtmlTag("span");
        span2.innerText = "×";

        let label = new HtmlTag("label");
        label.setAttribute("for","sideNavBox");
        label.addChild(span1);
        label.addChild(span2);
        return label;
    }


    #getMainSection(){
        let ul = new HtmlTag("ul");
        for (let item of this.itemsList) {
            ul.addChild(item.getHtmlObject());
        }
        return ul;
    }

    #getBottomSection(){
        let a=new HtmlTag("a");
        a.innerText = projectConfig.name+" V"+projectConfig.version;
        a.setAttribute("id","sideNavMenuBottomInfo");
        a.setAttribute("href","https://iutbg-gitlab.iutbourg.univ-lyon1.fr/2023-2024-sae-but2/mahjong-calculator");
        return a;
    }

    #getAside(){
        let aside=new HtmlTag("aside");
        aside.setAttribute("id","sideNavMenu");
        aside.addChild(this.#getCloseSection());
        aside.addChild(this.#getMainSection());
        aside.addChild(this.#getBottomSection());

        return aside;
    }

    toHtml(){
        let div = new HtmlTag("div");
        div.setAttribute("id","sideNav");
        div.addChild(this.#getOverlay());
        div.addChild(this.#getAside())
        return div.toHtml();
    }
}