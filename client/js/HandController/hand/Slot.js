import {EmptyTile} from "./EmptyTile.js";
import {Signal} from "../../Signal/Signal.js";

export class Slot{
    #tileList;
    #htmlElement;
    #capacity;
    #emptyTile;
    clicked;
    changed;

    constructor(capacity = 4) {
        this.#tileList = [];
        this.#capacity = capacity;
        this.#emptyTile = new EmptyTile();
        this.clicked = new Signal();
        this.changed = new Signal();

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
        this.#htmlElement.scrollIntoView({behavior: "smooth"});

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
        let emptyElement = this.#emptyTile.getHtmlObject();
        if(this.isFull()) {
            this.#htmlElement.removeChild(emptyElement);
            emptyElement.removeEventListener("click", this.clicked.fire.bind(this.clicked));
        }
        else {
            this.#htmlElement.appendChild(emptyElement);
            emptyElement.addEventListener("click", this.clicked.fire.bind(this.clicked));
        }

        this.changed.fire();
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

    toJSON(){
        return this.#tileList;
    }

    clear(){
        for (const tile of this.#tileList){
            tile.remove();
        }
    }
}