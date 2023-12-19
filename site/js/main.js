import {NetworkController} from "./NetworkController/NetworkController.js";
import {ImageManager} from "./ImageController/ImageController.js";
import {startBack} from "./HandController/back.js";
import {startDrawer} from "./HandController/drawer.js";
import {startSettings} from "./HandController/gameSettings.js";

function main(){
    let network = NetworkController.getController("localhost", 8080);
    let imgController = ImageManager.getController();

    startBack().then();
    startDrawer().then();
    startSettings().then();
}
main();