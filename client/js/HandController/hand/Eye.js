import {Signal} from "../../Signal/Signal.js";

export class Eye {
    #htmlObject;
    #input;
    #hidden;
    clicked;

    constructor() {
        this.#hidden = false;
        this.clicked = new Signal();
        this.#htmlObject = document.createElement("label");
        this.#htmlObject.setAttribute("class", "boxContainer");

        this.#input = document.createElement("input");

        this.#htmlObject.appendChild(this.#input);

        let span1 = document.createElement("span");
        span1.setAttribute("class","checkmarkChecked");
        let span2 = document.createElement("span");
        span2.setAttribute("class","checkmarkUnchecked");

        this.#htmlObject.appendChild(span1);
        this.#htmlObject.appendChild(span2);


        this.#input.setAttribute("type", "checkbox");

        this.#input.addEventListener("change", this.clicked.fire.bind(this.clicked));
    }

    hide(hidden){
        this.#input.checked = hidden;
    }

    isHidden(){
        return this.#input.checked;
    }

    getHtmlObject(){
        return this.#htmlObject;
    }

    clear(){
        this.#htmlObject.remove();
        this.#input.removeEventListener("change", this.clicked.fire.bind(this.clicked));
    }
}