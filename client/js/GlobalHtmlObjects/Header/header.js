import {NavigationMenu} from "./NavigationMenu.js";
import {BurgerMenu} from "./BurgerMenu.js";
import {HtmlTag} from "../HtmlObjects/HtmlTag.js";
const headerObject=document.querySelector("header")




class Header{
    navigationMenu;
    burgerMenu;

    constructor(enableBurger=true) {
        this.navigationMenu = new NavigationMenu();
        if(enableBurger){
            this.burgerMenu = new BurgerMenu();
        }else{
            this.burgerMenu=""
        }

    }

    toHtml(){
        if(this.burgerMenu==""){
            return this.navigationMenu.toHtml();
        }

        let input = new HtmlTag("input");
        input.hasClosingTag=false;
        input.setAttribute("type","checkbox");
        input.setAttribute("id","sideNavBox");
        return input.toHtml()
            + this.navigationMenu.toHtml()
            + this.burgerMenu.toHtml()
    }
}



export async function startHeader(){
    if(!headerObject) return

    let header=new Header();
    headerObject.innerHTML = header.toHtml();
}