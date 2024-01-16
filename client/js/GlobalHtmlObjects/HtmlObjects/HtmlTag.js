import {HtmlAttribute} from "./HtmlAttribute.js";
import {HtmlText} from "./HtmlText.js";

export class HtmlTag{
    type;
    children;
    attributes;
    hasClosingTag;

    constructor(type="span") {
        this.type=type;
        this.children=[];
        this.attributes=[];
        this.hasClosingTag=true;
    }

    toHtml(){
        let str=this.getOpeningTag();
        if(!this.hasClosingTag) return str;
        for (let child of this.children) {
            str+=child.toHtml();
        }
        str+=this.getClosingTag();
        return str;
    }

    getOpeningTag(){
        let str="<"+this.type;
        for (let attribute of this.attributes) {
            str+=" "+attribute.toHtml();
        }
        str+=">";
        return str;
    }
    getClosingTag(){
        return "</"+this.type+">";
    }

    addAttribute(name,value){
        this.attributes.push(new HtmlAttribute(name,value));
    }

    setAttribute(name,value){
        for (let attribute of this.attributes) {
            if(attribute.name==name){
                attribute.value=value;
                return;
            }
        }
        this.addAttribute(name,value);
    }

    addChild(child){
        this.children.push(child);
    }


    addText(text){
        this.addChild(new HtmlText(text));
    }
}