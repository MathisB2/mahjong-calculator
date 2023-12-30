import {HtmlAttribute} from "./HtmlAttribute.js";

export class HtmlTag{
    type;
    innerText;
    children;
    attributes;
    hasClosingTag;

    constructor(type="span") {
        this.type=type;
        this.innerText="";
        this.children=[];
        this.attributes=[];
        this.hasClosingTag=true;
    }

    toHtml(){
        let str=this.getOpeningTag();
        if(!this.hasClosingTag) return str;
        str+=this.innerText;
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
}