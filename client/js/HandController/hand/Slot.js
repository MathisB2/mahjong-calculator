export class Slot{
    tileList;
    htmlElement;

    constructor() {
        this.tileList = [];

        this.#createHtmlElement();
    }

    #createHtmlElement(){
        let element = document.createElement("div");

        this.htmlElement = element;
    }

    insert(tile){
        console.assert(!this.isFull(), "slot is full !!!!");
        console.assert(!this.has(tile), "tile already exist into slot");

        this.tileList.append(tile);
        this.htmlElement.addChild(tile.getHTMLObject());
    }


    remove(tile) {
        for (let i = 0; i < this.tileList.length; i++){
            if ( this.tileList[i] == tile) {
                this.tileList.splice(i, 1);
                return;
            }
        }

        console.error("tile does not exist into slot");
    }

    has(tile){
        for(let storedTile of this.tileList){
            if(storedTile == tile) return true;
        }

        return false;
    }

    isFull(){
        return this.tileList.length >= 4;
    }

    getHtmlObject(){
        return this.htmlElement;
    }

    clear(){
        for (const tile of this.tileList){
            tile.remove();
        }
    }
}