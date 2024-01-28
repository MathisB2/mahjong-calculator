import {Drawer} from "./drawer.js";
import {findChildByClass, findChildById} from "./elementFinder.js";

export class TileDrawer extends Drawer{
    #drawerButton;
    #drawerHandle;
    #drawerHeader;
    #drawerTileList;

    constructor(drawer) {
        let drawerButton = findChildByClass(drawer,"drawerButton");
        let drawerHeader = findChildById(drawer,"drawerHeader");
        let drawerHandle = findChildById(drawer,"drawerHandle");
        let drawerTileList = findChildById(drawer,"drawerTileList");

        let minHeight = drawerButton.getBoundingClientRect().height
            + drawerHeader.getBoundingClientRect().height
            + drawerHandle.getBoundingClientRect().height;

        super(drawer, minHeight, .64*window.innerHeight);

        this.#drawerButton = drawerButton;
        this.#drawerHandle = drawerHandle;
        this.#drawerHeader = drawerHeader;
        this.#drawerTileList = drawerTileList;

        this.setHandle([drawerHandle,drawerHeader]);
        this.close();
        this.setTransition(".2s ease-out");


        window.addEventListener("resize", this.updateBounds.bind(this));
    }

    updateBounds(){
        this.minHeight = this.#drawerButton.getBoundingClientRect().height
            + this.#drawerHeader.getBoundingClientRect().height
            + this.#drawerHandle.getBoundingClientRect().height;


        let tileWidth = Math.min((window.innerWidth - 48) / 5.5, 64);
        let height =    this.#drawerTileList.scrollHeight +
                        this.#drawerHeader.scrollHeight +
                        this.#drawerHandle.scrollHeight + 64;
        console.log(height);
        this.maxHeight = Math.min(height,.64*window.innerHeight);
        this.close();
    }


    insertTile(tile){
        this.#drawerTileList.innerHTML+=tile.getHtmlObject().toHtml();
    }

    removeTile(tile, nbrTile){

    }

    clear(){
        this.#drawerTileList.innerHTML="";
    }
}