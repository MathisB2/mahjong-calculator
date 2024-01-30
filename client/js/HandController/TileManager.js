import {Hand} from "./hand/Hand.js";
import {TileDrawer} from "./drawer/tileDrawer.js";
import {TileStack} from "./tileStack.js";

export class TileManager{
    hand;
    drawer;
    tiles

    constructor(drawer) {
        this.hand = new Hand()
        this.drawer = new TileDrawer(drawer);
        this.drawer = new TileDrawer(drawer);
        this.tiles = [];

        this.#initTiles();
        this.updateView();
        this.drawer.updateBounds();
    }

    #initTiles(){
        let fileNames=[
            "dot_1", "dot_2", "dot_3", "dot_4", "dot_5", "dot_6", "dot_7", "dot_8", "dot_9",
            "bamboo_1", "bamboo_2", "bamboo_3", "bamboo_4", "bamboo_5", "bamboo_6", "bamboo_7", "bamboo_8", "bamboo_9",
            "character_1", "character_2", "character_3", "character_4", "character_5", "character_6", "character_7", "character_8", "character_9",
            "wind_east", "wind_south", "wind_west", "wind_north",
            "dragon_red", "dragon_green", "dragon_white"];
        for (let fileName of fileNames) {
            this.tiles.push(new TileStack(fileName))
        }

    }


    updateView(){
        this.#updateDrawer();
        this.#updateHand();
    }

    #updateDrawer(){
        this.drawer.clear();
        for (let tile of this.tiles) {
            this.drawer.insertTile(tile);
        }
    }

    #updateHand(){
        //TODO
    }

}