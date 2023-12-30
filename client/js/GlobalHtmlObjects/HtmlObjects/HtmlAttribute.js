export class HtmlAttribute{
    name;
    value;

    constructor(name, value) {
        this.name=name;
        this.value=value;
    }

    toHtml(){
        if(this.value==""){
            return this.name;
        }
        return this.name+"=\""+this.value+"\"";
    }
}