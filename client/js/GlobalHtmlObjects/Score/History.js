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
        this.itemList.sort((a, b) => a.timestamp - b.timestamp);
        this.#saveToLocalStorage();

    }

    #saveToLocalStorage(){

        localStorage.setItem(storageConfig.history, JSON.stringify(this));
    }

    toHtml(){
        if(this.isEmpty()){
            let h4 = new HtmlTag("h4");
            h4.addText("L'historique est vide");
            this.historyObject.innerHTML = h4.toHtml();
            return;
        }


        let map = this.#computeMap();
        this.historyObject.innerHTML= "";

        map.forEach((value, key) => {
            let h3 = new HtmlTag("h3");
            h3.addText(key);
            this.historyObject.innerHTML += h3.toHtml();
            value.sort((a, b) => a.timestamp - b.timestamp)
            console.log(key);
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


    isEmpty(){
        return this.itemList.length==0;
    }

}