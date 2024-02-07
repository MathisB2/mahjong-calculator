import {Signal} from "../../Signal/Signal.js";
import {Slot} from "./Slot.js";
import {cropValue} from "../../calc.js";
export class Hand{
    slotList;
    activeSlotId;
    currentId;
    tileClicked;
    htmlHand;

    constructor(htmlHand) {
        this.tileClicked = new Signal();

        this.activeSlotId = 0;
        this.currentId = 0;
        this.htmlHand = htmlHand;
        this.#initSlots();
    }

    #initSlots(){
        this.slotList = [];
        for(let i = 0; i < 5; ++i){
            let slot = new Slot(4);

            this.#appendSlot(slot, i);
            this.slotList[i] = slot;
        }

        this.setActiveSlot(0);
    }

    #appendSlot(slot, slotId){
        let htmlElement = slot.getHtmlObject();
        this.htmlHand.appendChild(htmlElement);

        htmlElement.addEventListener("click", (() =>{
            this.setActiveSlot(slotId);
        }).bind(this))
    }


    insert(tile){
        let slot = this.slotList[this.activeSlotId];
        slot.insert(tile);

        tile.getHtmlObject().addEventListener("click", (() => {
            this.tileClicked.fire(tile);
        }).bind(this))
    }

    canInsert(){
        let slot = this.slotList[this.activeSlotId];
        return !slot.isFull();
    }

    remove(tile){
        for(let slot of this.slotList){
            if(slot.has(tile)) slot.remove(tile);
        }
    }

    has(tile){

    }

    getActiveSlotId(){
        return this.activeSlotId;
    }

    setActiveSlot(slotId){
        slotId = cropValue(slotId, 0, this.slotList.length -1)

        this.slotList[this.activeSlotId].disable();
        this.slotList[slotId].enable();
        this.activeSlotId = slotId;
    }

    clear(){
        this.htmlHand.innerHTML="";
        this.#initSlots();
    }

    toString(){
        return JSON.stringify(this);
    }
}