import {HtmlAttribute} from "./HtmlAttribute.js";

export class HtmlStyleSet extends HtmlAttribute{
    styleProperties

    constructor() {
        super("style","");
        this.styleProperties = []
    }

    toHtml(){
        if(this.styleProperties.length==0){
            return "";
        }

        let str=" style=\"";
        for(let property of this.styleProperties){
            str += " "+property.toHtml();
        }
        str += "\"";
        return str;
    }

}