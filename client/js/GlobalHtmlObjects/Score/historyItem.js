import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class HistoryItem{
    date;
    score;

    constructor(score,date=new Date()) {
        this.date = date;
        this.score = score
    }

    toHtml(){


        let span=new HtmlTag("span");
        span.addText(this.#getStringDate());
        let span2=new HtmlTag("span");
        span2.addText(this.score+this.#getStringPts());

        let div=new HtmlTag("div");
        div.addChild(span);
        div.addChild(span2);

        return div.toHtml();
    }

    #getStringDate(){
        let date="";
        date+=this.date.getDate().toString().padStart(1,"0")+"/";
        date+=this.date.getMonth().toString().padStart(1,"0")+1+"/";
        date+=this.date.getFullYear()+", ";

        date+=this.date.getHours()+"h";
        date+=this.date.getMinutes();

        return date;
    }


    #getStringPts(){
        if(this.score <= 1) return " pt";
        return " pts";
    }
}