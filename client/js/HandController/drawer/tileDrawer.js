import {Drawer} from "./drawer.js";
import {findChildByClass, findChildById} from "../../GlobalHtmlObjects/HtmlObjects/elementFinder.js";
import {TileStack} from "../TileStack.js";
import {Signal} from "../../Signal/Signal.js";
import {Tile} from "../hand/Tile.js";

export class TileDrawer extends Drawer{
    #drawerButton;
    #drawerHandle;
    #drawerHeader;
    #drawerTrash;
    #drawerTileList;
    tiles;
    tileClicked;
    trashClicked;
    buttonClicked;


    constructor(drawer) {
        let drawerButton = findChildByClass(drawer,"drawerButton");
        let drawerHeader = findChildById(drawer,"drawerHeader");
        let drawerHandle = findChildById(drawer,"drawerHandle");
        let drawerTileList = findChildById(drawer,"drawerTileList");

        let minHeight = drawerButton.getBoundingClientRect().height
            + drawerHeader.getBoundingClientRect().height
            + drawerHandle.getBoundingClientRect().height;

        super(drawer, minHeight, .64*window.innerHeight);
        this.tileClicked = new Signal();
        this.buttonClicked = new Signal();

        this.#drawerButton = drawerButton;
        this.#drawerHandle = drawerHandle;
        this.#drawerHeader = drawerHeader;
        this.#drawerTileList = drawerTileList;

        this.trashClicked = new Signal();
        this.#drawerTrash = findChildById(findChildById(this.#drawerHeader,"drawerActionIcons"),"trashButton");

        this.#drawerTrash.addEventListener("click", (() => {
            this.trashClicked.fire();
        }).bind(this));

        this.setHandle([drawerHandle,drawerHeader]);
        this.close();
        this.setTransition(".2s ease-out");
        drawerButton.addEventListener("click", this.buttonClicked.fire.bind(this.buttonClicked))
        window.addEventListener("resize", this.updateBounds.bind(this));
        this.#initTiles();
    }

    #initTiles(){
        this.tiles = [];
        let fileNames=[
            "dot_1", "dot_2", "dot_3", "dot_4", "dot_5", "dot_6", "dot_7", "dot_8", "dot_9",
            "bamboo_1", "bamboo_2", "bamboo_3", "bamboo_4", "bamboo_5", "bamboo_6", "bamboo_7", "bamboo_8", "bamboo_9",
            "character_1", "character_2", "character_3", "character_4", "character_5", "character_6", "character_7", "character_8", "character_9",
            "wind_east", "wind_south", "wind_west", "wind_north",
            "dragon_red", "dragon_green", "dragon_white"];
        for (let fileName of fileNames) {
            this.tiles.push(new TileStack(fileName));
        }
        this.#appendHTMLTiles();
    }

    #appendHTMLTiles(){
        for(let tile of this.tiles){
            this.#drawerTileList.appendChild(tile.getHtmlObject());
            tile.getHtmlObject().addEventListener("click", (() => {
                if(tile.isEmpty()) return;
                this.tileClicked.fire(new Tile(tile.referredTile.name));
            }).bind(this));
        }
    }

    updateBounds(){
        this.minHeight = this.#drawerButton.getBoundingClientRect().height
            + this.#drawerHeader.getBoundingClientRect().height
            + this.#drawerHandle.getBoundingClientRect().height;

        let height =    this.#drawerTileList.scrollHeight +
                        this.#drawerHeader.scrollHeight +
                        this.#drawerHandle.scrollHeight + 64;
        this.maxHeight = Math.min(height,.64*window.innerHeight);
        this.close();
    }

    update(){
        for(let tile of this.tiles){
            tile.update();
        }
    }

    insertTile(tile){
        let tileStack = this.#getTileStack(tile.name);
        tileStack.increment();
    }

    removeTile(tile){
        let tileStack = this.#getTileStack(tile.name);
        tileStack.decrement();
    }

    isEmptyOf(name){
        return this.#getTileStack(name).isEmpty();
    }

    #getTileStack(name){
        for (let tileElement of this.tiles) {
            if(tileElement.referredTile.name === name){
                return tileElement;
            }
        }

        console.error(name+" tileStack does not exist");
    }



    restore(){
        for (let tile of this.tiles) {
            tile.restore();
        }
    }
}