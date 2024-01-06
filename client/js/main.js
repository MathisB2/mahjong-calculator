import {startBack} from "./HandController/back.js";
import {startDrawer} from "./HandController/drawer.js";
import {startSettings} from "./HandController/gameSettings.js";
import {startHeader} from "./GlobalHtmlObjects/Header/header.js";
import {startAbout} from "./GlobalHtmlObjects/aboutInfos/about.js";
import {startAnimations} from "./GlobalHtmlObjects/animations/animations.js";
import {startFooter} from "./GlobalHtmlObjects/footer/footer.js";

function main(){
    startHeader().then(
        headerResult => startDrawer()
    );
    startBack().then();
    startSettings().then();
    startAbout().then();
    startAnimations().then();
    startFooter().then();
}
main();