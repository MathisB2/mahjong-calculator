import {startBack} from "./HandController/back.js";
import {startDrawer} from "./HandController/drawer.js";
import {startSettings} from "./HandController/gameSettings.js";
import {startHeader} from "./GlobalHtmlObjects/Header/header.js";

function main(){
    startHeader().then();
    startBack().then();
    startDrawer().then();
    startSettings().then();
}
main();