import {EmptyTile} from "./EmptyTile.js";
import {Signal} from "../../Signal/Signal.js";
import {Eye} from "./Eye.js";
export class Slot{
    #tileList;
    #htmlElement;
    #capacity;
    #emptyTile;
    #eye;

    clicked;
    changed;

    constructor(capacity = 4) {
        this.#tileList = [];
        this.#capacity = capacity;
        this.#emptyTile = new EmptyTile();
        this.#eye = new Eye();

        this.clicked = new Signal();
        this.changed = new Signal();

        this.#createHtmlElement();
        this.#connect();
        this.#update();
    }

    #createHtmlElement(){
        let element = document.createElement("section");
        element.setAttribute("class", "slot");

        this.#htmlElement = element;
    }

    #connect(){
        this.#eye.clicked.connect(() => {
            this.hide(this.#eye.isHidden());
            console.log(this.#eye.isHidden());
        })
    }

    hide(hidden){
        this.#htmlElement.style.opacity = (1 - .5 * hidden).toString();
        this.#eye.hide(hidden);
    }

    scrollIntoView(){
        this.#htmlElement.scrollIntoView({behavior:"smooth"});
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
        console.log(this.#tileList)
        let emptyElement = this.#emptyTile.getHtmlObject();

        if(this.isFull()) {
            this.#htmlElement.removeChild(emptyElement);
            emptyElement.removeEventListener("click", this.clicked.fire.bind(this.clicked));
        }
        else {
            this.#htmlElement.appendChild(emptyElement);
            emptyElement.addEventListener("click", this.clicked.fire.bind(this.clicked));
        }

        this.#htmlElement.appendChild(this.#eye.getHtmlObject());

        this.changed.fire();
    }

    has(tile){
        for(let storedTile of this.#tileList){
            if(storedTile == tile) return true;
        }

        return false;
    }

    isEmpty(){
        return this.#tileList.length == 0;
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
        let list = [];

        for(let tile of this.#tileList){
            list.push(tile.toJSON());
        }

        return {
            tileList: list,
            hidden: this.#eye.isHidden()
        };
    }

    clear(){
        this.#eye.clear();
        for (const tile of this.#tileList){
            tile.remove();
        }
    }

    destroy(){
        this.clear();
        this.clicked.clear();
        this.changed.clear();
    }

    removeHtml(){
        this.#htmlElement.remove();
    }
}