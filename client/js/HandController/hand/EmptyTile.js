export class EmptyTile{
    htmlElement;

    constructor() {
        this.#createObject();
    }

    #createObject(){
        let img = document.createElement("img");
        img.setAttribute("src","img/icons/addTile.svg");
        img.setAttribute("alt","+");

        let div = document.createElement("div");
        div.appendChild(img);
        div.setAttribute("class","emptyTile");
        this.htmlElement = div;
    }

    getHtmlObject(){
        return this.htmlElement;
    }

    setActive(enabled){
        this.htmlElement.setAttribute("class", enabled ? "activeEmptyTile" : "emptyTile");
    }

    clear() {
        this.htmlElement.remove();
    }
}