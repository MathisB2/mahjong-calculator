import {HistoryItem} from "./historyItem.js";
import {storageConfig} from "../../config.js";
import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

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
        this.itemList.sort((a, b) => a.timestamp - b.timestamp);
    }

    addItem(score){
        this.itemList.push(new HistoryItem(score));
        this.#saveToLocalStorage();

    }

    #saveToLocalStorage(){

        localStorage.setItem(storageConfig.history, JSON.stringify(this));
    }

    toHtml(){
        let map = this.#computeMap();
        this.historyObject.innerHTML= "";

        map.forEach((value, key) => {
            console.log(key)
            let h3 = new HtmlTag("h3");
            h3.addText(key);
            this.historyObject.innerHTML += h3.toHtml();

            for (let item of value) {
                this.historyObject.innerHTML += item.toHtml();
            }
        });
    }


    #computeMap(){
        let map = new Map();
        for (let item of this.itemList) {
            let title = item.getTitle();
            if(map.get(title) == undefined){
                map.set(title,[]);
            }
            let array = map.get(title);
            array.push(item);
            map.set(title, array);
        }
        return map;
    }

}