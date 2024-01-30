import {TileManager} from "./TileManager.js";
import {LoadingPopup} from "../ImageController/popup/loadingPopup.js";
import {ResultPopup} from "../ImageController/popup/resultPopup.js";
import {ResultData} from "../ImageController/resultData.js";
import {ImageManager} from "../ImageController/ImageController.js";
const drawer = document.querySelector("#drawer");



export async function startTileManager(){
    if(!drawer) return;

    const tileManager = new TileManager(drawer);

    let imageController = ImageManager.getController()
    imageController.OnTilesReceived.connect((callback) => {
    });




}