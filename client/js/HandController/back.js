import {ImageManager} from "../ImageController/ImageController.js";

import {storageConfig} from "../config.js";

const h = document.getElementById("hand");
const htmlDrawerTileList = document.getElementById("drawerTileList");
const trashButton = document.getElementById("trashButton");
const nextButton = document.getElementById("drawerNextButton");

let mahjongHand;



function onTrashClick(){
    mahjongHand.clear();
}

function onNextClick(){
    //if(mahjongHand.isValid()){
        console.log(mahjongHand.toString());
        localStorage.setItem(storageConfig.hand,mahjongHand.toString());
        window.location.href = "gameSettings.html";
    //}else{
    //    alert("Veuillez saisir une main valide");
    //}
}

export async function startBack(){
    if((h && htmlDrawerTileList && trashButton && nextButton) == null) return;
    return;

    trashButton.addEventListener("click", onTrashClick);
    nextButton.addEventListener("click", onNextClick);

    mahjongHand = new Hand();
    mahjongHand.setActive(0);

    importTiles();
    mahjongHand.drawHand();

    let imageController = ImageManager.getController()
    imageController.OnTilesReceived.connect((callback) => {
        mahjongHand.importDetectionResults(callback);
    });
}