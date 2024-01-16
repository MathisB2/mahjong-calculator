import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class HistoryItem{
    date;
    score;
    timestamp   //time elapsed in sec

    constructor(score,date=new Date()) {
        this.date = date;
        this.score = score
        this.timestamp = this.#getSecDifference(new Date(), this.date);
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
        let date= this.#getStringDay()+", ";
        date += this.#getStringTime();
        return date;
    }




    #getStringTime(){
        let time = this.date.getHours().toString().padStart(2,"0")+"h";
        time+=this.date.getMinutes().toString().padStart(2,"0");
        return time;
    }

    #getRelativeStringTime(){
        let diff = this.#getMinDifference(new Date(), this.date)

        if(diff == 0){
            return "Maintenant";
        }else if(diff < 60){
            return "Il y a "+diff+"min";
        }else if(diff < 60*24){
            return "Il y a "+Math.floor(diff/60)+"h";
        }
    }



    getTitle(){
        if(this.timestamp< 60*60*24){
            return this.#getRelativeStringTime();
        }
        return this.#getRelativeStringDay();
    }
    #getRelativeStringDay(){
        let diff = this.#getDayDifference(new Date(), this.date)
        if(diff == 0){
            return "Aujourd'hui";
        }else if(diff == 1){
            return "Hier";
        }else if(diff < 7){
            return "Il y a "+diff+" jours";
        }
        return "Il y a plus d'une semaine";
    }



    #getStringDay(){
        let date="";
        date+=this.date.getDate().toString().padStart(1,"0")+"/";
        date+=this.date.getMonth().toString().padStart(1,"0")+1+"/";
        date+=this.date.getFullYear();
        return date;
    }


    #getStringPts(){
        if(this.score <= 1) return " pt";
        return " pts";
    }


    #getDayDifference(date1, date2){
        let one_day = 1000 * 60 * 60 * 24;
        return Math.round((date1.getTime() - date2.getTime()) / one_day);
    }

    #getMinDifference(date1, date2){
        let one_min = 1000 * 60;
        return Math.round((date1.getTime() - date2.getTime()) / one_min);
    }

    #getSecDifference(date1, date2){
        let one_sec = 1000;
        return Math.round((date1.getTime() - date2.getTime()) / one_sec);
    }
}