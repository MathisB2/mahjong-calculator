import {HtmlTag} from "../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";

export class Slot{
    tileList;
    slotId;
    hidden;

    constructor(id) {
        this.tileList = [];
        this.slotId=id;
        this.hidden=false;
    }

    drawSlot(active=false){
        let tmp="<section class='lineHand' id='slot"+this.slotId+"'>";

        for(let element of this.tileList) {
            if(this.hidden){
                tmp+=element.drawUnavailable(50);
            }else{
                tmp+=element.draw();
            }
        }

        if(this.tileList.length<3 || this._isTriple()) {
            if(active){
                tmp += this._drawActiveAddButton()
            }else{
                tmp += this._drawAddButton();
            }
        }

        tmp+="<label class=\"boxContainer\" >"

        if(this.hidden){
            tmp+="<input id=\"check"+this.slotId+"\" type=\"checkbox\" checked >";
        }else{
            tmp+="<input id=\"check"+this.slotId+"\" type=\"checkbox\" >";
        }

        tmp+="<span class=\"checkmarkChecked\"></span>";
        tmp+="<span class=\"checkmarkUnchecked\"></span>";
        tmp+="</label>";
        tmp+="</section>";

        return tmp;
    }

    addTile(tile){
        this.tileList.push(tile);
    }



    #getAddButton(){
        let img = new HtmlTag("img");
        img.setAttribute("src","img/icons/addTile.svg");
        img.setAttribute("alt","+");

        let div = new HtmlTag("div");
        div.setAttribute("id","bouton"+this.slotId);
        div.addChild(img);
        return div;
    }
    _drawAddButton(){
        let div=this.#getAddButton();
        div.setAttribute("class","emptyTile");
        return div.toHtml();
    }

    _drawActiveAddButton(){
        let div=this.#getAddButton();
        div.setAttribute("class","activeEmptyTile");
        return div.toHtml();
    }

    _isTriple(){
        if(this.tileList.length!=3){
            return false;
        }
        let name=this.tileList[0].name;

        for(let element of this.tileList) {
            if(element.name!=name){
                return false;
            }
        }
        return true;
    }

    deleteTile(id){
        let i=0;

        for(let element of this.tileList) {
            if(element.id==id){
                this.tileList.splice(i,1);
                return true;
            }
            i++;
        }
        return false;
    }


    find(id){
        for (let tile of this.tileList){
            if(id==tile.id){
                return true;
            }
        }
        return false;
    }

    canAdd(name){
        let triple = this._isTriple() && this.tileList[0].name == name;
        let enoughLength = this.tileList.length < 3;

        return triple || enoughLength;
    }

    copy(){
        let s = new Slot(this.slotId);

        for(let tile of this.tileList){
            s.tileList.push(tile.copy());
        }

        return s;
    }
    toggleHidden(){
        this.hidden=!this.hidden;
        console.log(this.hidden);
    }


    isEmpty(){
        return this.tileList.length==0;
    }

}