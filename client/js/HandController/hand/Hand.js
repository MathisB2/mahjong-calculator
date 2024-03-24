import {Signal} from "../../Signal/Signal.js";
import {Slot} from "./Slot.js";

export class Hand{
    slotList;

    activeSlotId;
    currentId;
    tileClicked;
    htmlHand;

    slotClicked;
    constructor(htmlHand) {
        this.tileClicked = new Signal();
        this.slotClicked = new Signal();

        this.activeSlotId = 0;
        this.currentId = 0;
        this.htmlHand = htmlHand;
        this.#initSlots();
    }

    #initSlots(){
        this.slotList = [];
        for(let i = 0; i < 1; ++i){
            this.#appendSlot(i);
        }
    }

    #appendSlot(slotId) {
        let slot = new Slot();
        this.htmlHand.appendChild(slot.getHtmlObject());
        this.#setupSlotAction(slot,slotId)
        this.slotList.push(slot);
    }

    #setupSlotAction(slot, index){
        slot.clicked.clear();
        slot.clicked.connect(() => {
            this.#setActiveSlot(index);
            slot.scrollIntoView();
            this.slotClicked.fire();
        })

        slot.changed.clear();
        slot.changed.connect(() => {
            this.#setActiveSlot(index);
            this.#updateSlots();
        })
    }

    #updateSlots(){
        let i = 0;

        while (i < this.slotList.length){
            let slot = this.slotList[i];
            if(slot.isEmpty()) this.#removeSlot(slot);
            else ++i;
        }
        this.#appendSlot(i);
        this.#setActiveSlot(this.activeSlotId);
        console.log(this.slotList)
    }

    #removeSlot(slot) {

        let index = this.#getSlotIndexOf(slot);
        console.log("removed ",index)

        this.htmlHand.removeChild(slot.getHtmlObject());
        slot.destroy();
        this.slotList.splice(index, 1)


        while (index < this.slotList.length){
            this.#setupSlotAction(this.slotList[index],index);
            index++;
        }
    }

    #getSlotIndexOf(slot){
        for(let i = 0; i < this.slotList.length; ++i){
            if(this.slotList[i] == slot) return i;
        }

        console.error("slot does not exist !!!!!")
    }

    hideCurrentSlot(hidden){
        let slot = this.slotList[this.getActiveSlotId()];
        slot.hide(hidden);
    }

    nextSlot(){
        let slotId = this.getActiveSlotId();
        let relativeNextSlot = slotId + 1;
        let nextSlot = relativeNextSlot < this.slotList.length ? relativeNextSlot : 0;

        this.#setActiveSlot(nextSlot);
    }

    windUpActiveSlot(){
        this.#setActiveSlot(0);
    }

    #setActiveSlot(slotId){
        this.slotList[this.activeSlotId].disable();
        this.slotList[slotId].enable();
        this.activeSlotId = slotId;

    }

    insert(tile){
        if(!this.canInsert()) return;
        let slot = this.slotList[this.activeSlotId];
        slot.insert(tile);

        tile.getHtmlObject().addEventListener("click", (() => {
            this.tileClicked.fire(tile);
        }).bind(this));
    }

    moveToNextAvailableActiveSlot(){
        for(let i = 0; i < this.slotList.length; ++i){
            let slot = this.slotList[this.activeSlotId];
            if(!slot.isFull())return;

            this.nextSlot();
        }
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

    getActiveSlotId(){
        return this.activeSlotId;
    }


    toJSON(){
        let json = [];

        for(let slot of this.slotList){
            json.push(slot.toJSON());
        }

        return json;
    }


    clear(){
        this.htmlHand.innerHTML="";
        this.#initSlots();
    }


}