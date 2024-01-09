import {HistoryItem} from "./historyItem.js";
import {storageConfig} from "../../config.js";

export class History{
    itemList;

    constructor() {
        this.itemList = [];
        let history = localStorage.getItem(storageConfig.history);
        if (history==null) return;

        

        this.itemList.push(new HistoryItem(12));
    }


    toHtml(){
        for (let item of this.itemList) {
            console.log(item.toHtml());
        }
    }

}