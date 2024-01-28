import {TileManager} from "./TileManager.js";
import {LoadingPopup} from "../ImageController/popup/loadingPopup.js";
const drawer = document.querySelector("#drawer");
const overlay = document.querySelector(".popUpOverlay");

export async function startTileManager(){
    if(!drawer) return;

    const tileManager = new TileManager(drawer);

    const p = new LoadingPopup(overlay)
    p.show();

}