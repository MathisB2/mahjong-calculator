import {ImageManager} from "../ImageController/ImageController.js";
import {Hand} from "./Hand/Hand.js";
import {TileDrawer} from "./Drawer/TileDrawer.js";
import {storageConfig} from "../config.js";
import TileFactory from "./Tiles/TileFactory.js";
import {getTileConfig} from "./Tiles/TilesConfig.js";
const drawer = document.querySelector("#drawer");
const hand = document.querySelector("#hand");

class TileController {
    hand;
    drawer;

    constructor(drawer,hand) {
        this.hand = new Hand(hand);
        this.drawer = new TileDrawer(drawer);

        let savedData = localStorage.getItem(storageConfig.hand);
        if (savedData!=null){
            this.importTiles(JSON.parse(savedData));
        }

        this.drawer.updateBounds();

        this.#connectHand();
        this.#connectDrawer();
        this.#connectTrash();
    }

    #connectHand(){
        this.hand.tileClicked.connect((tile) => {
            this.hand.remove(tile);
            this.drawer.insertTile(tile);
        })
        this.hand.slotClicked.connect(() => {
            this.drawer.open();
        })
    }

    #connectDrawer(){
        this.drawer.tileClicked.connect((tile) => {
            if(!this.hand.canInsert()) return;
            this.drawer.removeTile(tile);
            this.hand.insert(tile);

            this.hand.moveToNextAvailableActiveSlot();
        })

        this.drawer.buttonClicked.connect(() => {
            localStorage.setItem(storageConfig.hand,JSON.stringify(this.hand.toJSON()));
            window.location.href = "gameSettings.html";
        })
    }

    #connectTrash(){
        this.drawer.trashClicked.connect(this.#reset.bind(this));
    }


    importTiles(clusters){
        this.#reset();
        this.hand.windUpActiveSlot();

        for (let cluster of clusters) {
            let tileList = cluster.tileList == null ? cluster : cluster.tileList;

            for (let dataTile of  tileList) {
                let tile = TileFactory.get(getTileConfig(dataTile.name));
                if(this.drawer.isEmptyOf(dataTile.name) || !this.hand.canInsert()) continue;

                this.hand.insert(tile);
                this.drawer.removeTile(tile);
            }
            this.hand.hideCurrentSlot(cluster.hidden);
            this.hand.nextSlot();
        }
    }

    #reset(){
        this.hand.clear();
        this.drawer.restore();
    }
}

export let TileManager = function() {
    let tileController = null;
    return {
        get: function () {
            if (!tileController) {
                tileController = new TileController();
            }
            return tileController;
        },
        load: function (){
            if(!(drawer && hand)) return;

            tileController = new TileController(drawer,hand);
            ImageManager.getController();
        }
    };
}();
