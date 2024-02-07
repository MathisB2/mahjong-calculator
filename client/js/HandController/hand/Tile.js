export class Tile {
    type;
    img;
    name;
    value;

    htmlElement;

    constructor(name) {
        this.name = name;
        this.img = "img/tiles/" + name + ".png";

        let split = name.split("_");
        this.type = split[0];
        this.value = split[1];

        this.#createObject();
    }

    #createObject(){
        let div = document.createElement("div");
        div.setAttribute("class", "tile");

        div.style.backgroundImage = "url('"+this.img+"')";

        this.htmlElement = div;
    }


    getHtmlObject(){
        return this.htmlElement;
    }

    clear() {
        this.htmlElement.remove();
    }
}