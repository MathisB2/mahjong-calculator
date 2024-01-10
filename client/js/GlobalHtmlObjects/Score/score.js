import {storageConfig} from "../../config.js";
import {HtmlTag} from "../HtmlObjects/HtmlTag.js";
import {History} from "./History.js";

const scoreObject = document.getElementById("scoreDisplay");
const backButton = document.getElementById("scoreBackButton");
const historyObject = document.getElementById("history");


function onBackClick(){
    localStorage.removeItem(storageConfig.score);
    window.location.href = "gameSettings.html";
}

export async function startScore(){
    if(!scoreObject) return;
    let score = localStorage.getItem(storageConfig.score);
    if(!score){
        window.location.href="calculator.html";
        return;
    }


    let history = new History(historyObject);
    history.toHtml();

    history.addItem(score);


    backButton.addEventListener("click", onBackClick);


    // let span = new HtmlTag("span");
    // span.innerText = score;
    scoreObject.innerHTML = "<span>"+score+"</span>";
    scoreObject.innerText += "pt";
    if(score!=1){
        scoreObject.innerText += "s";
    }

}