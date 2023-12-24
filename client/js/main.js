import {startBack} from "./HandController/back.js";
import {startDrawer} from "./HandController/drawer.js";
import {startSettings} from "./HandController/gameSettings.js";

function main(){
    startBack().then();
    startDrawer().then();
    startSettings().then();
}
main();