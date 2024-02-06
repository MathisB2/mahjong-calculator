import {Hand} from "./hand/Hand.js";
import {TileDrawer} from "./drawer/tileDrawer.js";
import {TileStack} from "./tileStack.js";

export class TileManager{
    hand;
    drawer;

    constructor(drawer) {
        this.hand = new Hand()
        this.drawer = new TileDrawer(drawer);

        this.updateView();
        this.drawer.updateBounds();

        this.#connectHand();
    }

    #connectHand(){
        const tileManager = this;
        this.hand.tileClicked.connect((tile) => {
            tileManager.hand.remove(tile);
            tileManager.drawer.insertTile(tile);
        })
    }




    updateView(){
        this.#updateHand();
    }

    #updateHand(){
        //TODO
    }

}