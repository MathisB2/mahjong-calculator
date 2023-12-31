import {startBack} from "./HandController/back.js";
import {startDrawer} from "./HandController/drawer.js";
import {startSettings} from "./HandController/gameSettings.js";
import {startHeader} from "./GlobalHtmlObjects/Header/header.js";
import {startAbout} from "./GlobalHtmlObjects/aboutInfos/about.js";

function main(){
    startHeader().then(
        headerResult => startDrawer()
    );
    startBack().then();
    startSettings().then();
    startAbout().then();
}
main();