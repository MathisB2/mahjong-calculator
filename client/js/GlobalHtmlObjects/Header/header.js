import {NavigationMenu} from "./NavigationMenu.js";
import {BurgerMenu} from "./BurgerMenu.js";
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
        return "<input type=\"checkbox\" id=\"sideNavBox\">"
            + this.navigationMenu.toHtml()
            + this.burgerMenu.toHtml()
    }
}







export async function startHeader(){
    if(!headerObject) return

    let header=new Header();
    headerObject.innerHTML = header.toHtml();
}