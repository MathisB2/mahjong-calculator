import {HistoryItem} from "./historyItem.js";
import {storageConfig} from "../../config.js";

export class History{
    itemList;
    historyObject;

    constructor(historyObject) {
        this.itemList = [];
        this.historyObject = historyObject;

        let history = localStorage.getItem(storageConfig.history);
        if (history==null) return;

        history = JSON.parse(history);

        for (let historyElement of history.itemList) {
            this.itemList.push(new HistoryItem(historyElement.score,new Date(historyElement.date)));
        }
    }

    addItem(score){
        this.itemList.push(new HistoryItem(score));
        this.#saveToLocalStorage();

    }

    #saveToLocalStorage(){

        localStorage.setItem(storageConfig.history, JSON.stringify(this));
    }

    toHtml(){
        this.historyObject.innerHTML= "";
        for (let item of this.itemList.reverse()) {
            this.historyObject.innerHTML += item.toHtml();
        }
    }
}