import {TileManager} from "./TileManager.js";
import {ImageManager} from "../ImageController/ImageController.js";
const drawer = document.querySelector("#drawer");



export async function startTileManager(){
    if(!drawer) return;

    const tileManager = new TileManager(drawer);

    let imageController = ImageManager.getController()


}