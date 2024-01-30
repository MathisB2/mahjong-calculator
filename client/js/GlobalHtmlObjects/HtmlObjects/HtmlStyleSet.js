import {HtmlAttribute} from "./HtmlAttribute.js";
import {HtmlStyleProperty} from "./HtmlStyleProperty.js";

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

    addStyle(property, value){
        this.styleProperties.push(new HtmlStyleProperty(property,value));
    }

    setStyle(property,value){
        for (let styleProperty of this.styleProperties) {
            if(styleProperty.property == property){
                styleProperty.value=value;
                return;
            }
        }
        this.addStyle(property,value);
    }

    clear(){
        this.styleProperties = [];
    }
}