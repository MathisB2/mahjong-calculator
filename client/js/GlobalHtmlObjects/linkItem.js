export class LinkItem{
    text;
    url;
    icon;

    constructor(text, url, icon=null) {
        this.text = text;
        this.url = url;
        this.icon = icon;
    }
}


export const globalLinks=[
    new LinkItem("Accueil","index.html","img/icons/home.svg"),
    new LinkItem("Calculatrice","calculator.html","img/icons/calculator.svg"),
    new LinkItem("Règles du jeu","","img/icons/rules.svg"),
    new LinkItem("À propos","about.html","img/icons/info.svg"),
]