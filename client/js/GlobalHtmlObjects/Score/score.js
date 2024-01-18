import {storageConfig} from "../../config.js";
import {HtmlTag} from "../HtmlObjects/HtmlTag.js";
import {History} from "./History.js";

const scoreObject = document.getElementById("scoreDisplay");
const backButton = document.getElementById("scoreBackButton");
const historyObject = document.getElementById("history");


function onBackClick(){
    localStorage.removeItem(storageConfig.score);
    localStorage.setItem("test","1");
    window.location.href = "gameSettings.html";

}

export async function startScore(){
    if(!scoreObject) return;
    let score = localStorage.getItem(storageConfig.score);
    if(!score){
        window.location.href="calculator.html";
        return;
    }


    if((scoreObject && historyObject && backButton)==null) return;


    let history = new History(historyObject);
    history.addItem(score);
    history.toHtml();


    backButton.addEventListener("click", onBackClick);


    let span = new HtmlTag("span");
    span.addText(score);
    scoreObject.innerHTML = span.toHtml() + "pt" + (score != 1 ? "s" : "");

}


export async function startHistory(){
    if(historyObject!=null && scoreObject==null){
        let history = new History(historyObject);
        history.toHtml();
    }

}