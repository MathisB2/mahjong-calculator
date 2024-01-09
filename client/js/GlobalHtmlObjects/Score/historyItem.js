import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class HistoryItem{
    date;
    score;

    constructor(score) {
        this.date = new Date();
        this.score = score
    }


    toHtml(){
        return this.date;
    }

}