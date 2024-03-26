class Tile {
    #type;
    #img;
    #htmlElement;
    #filename;

    constructor(filename, type) {
        this.#img = "img/tiles/" + filename + ".png";
        this.#type = type
        this.#filename = filename

        this.#createObject();
    }
    #createObject(){
        let div = document.createElement("div");
        div.setAttribute("class", "tile");

        div.style.backgroundImage = "url('" + this.#img + "')";

        this.#htmlElement = div;
    }

    getType(){
        return this.#type;
    }

    getFilename(){
        return this.#filename;
    }

    getValue(){
        throw new Error("Method 'getValue()' must be implemented.");
    }

    toJSON(){
        throw new Error("Method 'toJSONObject()' must be implemented.");
    }

    clone(){
        return new Tile(this.#filename, "Tile");
    }

    getHtmlObject(){
        return this.#htmlElement;
    }

    clear() {
        this.#htmlElement.remove();
    }
}

export default Tile