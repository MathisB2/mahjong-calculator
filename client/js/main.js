import {startDrawer} from "./HandController/drawer/drawerController.js";
import {startSettings} from "./HandController/gameSettings.js";
import {startHeader} from "./GlobalHtmlObjects/Header/header.js";
import {startAbout} from "./GlobalHtmlObjects/aboutInfos/about.js";
import {startAnimations} from "./GlobalHtmlObjects/animations/animations.js";
import {startFooter} from "./GlobalHtmlObjects/footer/footer.js";
import {startHistory, startScore} from "./GlobalHtmlObjects/Score/score.js";

function main(){
    startHeader().then(
        headerResult => startDrawer()
    );
    startSettings().then();
    startAbout().then();
    startAnimations().then();
    startFooter().then();
    startScore().then();
    startHistory().then();
}
main();