import {TileManager} from "./TileManager.js";
const drawer = document.querySelector("#drawer");

export async function startTileManager(){
    if(!drawer) return;

    const tileManager = new TileManager(drawer);
}