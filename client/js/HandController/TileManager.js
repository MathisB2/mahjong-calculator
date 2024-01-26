import {Hand} from "./Hand";
import {Drawer} from "./drawer/drawer";

export class TileManager{
    hand;
    drawer;
    tiles

    constructor() {
        this.hand = new Hand()
        this.drawer = new Drawer();
        this.tiles = [];

    }

}