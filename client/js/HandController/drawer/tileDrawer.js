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

        super(drawer, minHeight, window.innerHeight/2);

        this.#drawerButton = drawerButton;
        this.#drawerHandle = drawerHandle;
        this.#drawerHeader = drawerHeader;
        this.#drawerTileList = drawerTileList;

        this.setHandle([drawerHandle,drawerHeader]);
        this.close();
    }

    insertTile(tile, nbrTiles){

    }

    removeTile(tile, nbrTile){

    }

    clear(){
        this.#drawerTileList.innerHTML="";
    }
}