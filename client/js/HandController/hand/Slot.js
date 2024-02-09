import {EmptyTile} from "./EmptyTile.js";

export class Slot{
    #tileList;
    #htmlElement;
    #capacity;
    #emptyTile;

    constructor(capacity = 4) {
        this.#tileList = [];
        this.#capacity = capacity;
        this.#emptyTile = new EmptyTile();

        this.#createHtmlElement();
        this.#update();
    }

    #createHtmlElement(){
        let element = document.createElement("section");
        element.setAttribute("class", "slot");
        this.#htmlElement = element;
    }

    insert(tile){
        if(this.isFull() || this.has(tile)) return;

        this.#tileList.push(tile);
        this.#htmlElement.appendChild(tile.getHtmlObject());

        this.#update();
    }

    remove(tile) {
        for (let i = 0; i < this.#tileList.length; i++){
            if ( this.#tileList[i] == tile) {
                tile.clear();
                this.#tileList.splice(i, 1);
                this.#update();
                return;
            }
        }

        console.error("tile does not exist into slot");
    }

    #update(){
        if(this.isFull())
            this.#htmlElement.removeChild(this.#emptyTile.getHtmlObject());
        else
            this.#htmlElement.appendChild(this.#emptyTile.getHtmlObject());
    }

    has(tile){
        for(let storedTile of this.#tileList){
            if(storedTile == tile) return true;
        }

        return false;
    }

    isFull(){
        return this.#tileList.length >= this.#capacity;
    }

    getHtmlObject(){
        return this.#htmlElement;
    }

    enable(){
        this.#emptyTile.setActive(true);
    }

    disable(){
        this.#emptyTile.setActive(false);
    }

    clear(){
        for (const tile of this.#tileList){
            tile.remove();
        }
    }
}