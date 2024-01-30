export class HtmlStyleProperty {
    property;
    value;

    constructor(property, value) {
        this.property=property;
        this.value=value;
    }

    toHtml(){
        if(this.value==""){
            return "";
        }
        return this.property+": "+this.value+";";
    }
}