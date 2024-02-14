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
        for(let i = 0; i < 5; ++i){
            let slot = new Slot(4);

            this.#appendSlot(slot, i);
            this.slotList[i] = slot;
        }

        this.#setActiveSlot(0);
    }

    #appendSlot(slot, slotId) {
        this.htmlHand.appendChild(slot.getHtmlObject());

        slot.clicked.connect(() => {
            this.#setActiveSlot(slotId);
            this.slotClicked.fire();
        })

        slot.changed.connect(() => {
            this.#setActiveSlot(slotId);
        })
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