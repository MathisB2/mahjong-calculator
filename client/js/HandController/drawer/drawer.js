import {cropValue} from "../../calc.js";

export class Drawer{
    panel;
    handles;

    minHeight;
    maxHeight;

    #isDragging;
    #dragStartPos;

    #transition;


    constructor(drawerPanel, minHeight=100, maxHeight=500) {
        this.handles=[];
        this.panel = drawerPanel;
        this.#isDragging = false;
        this.#dragStartPos = 0;
        this.#initEvents();

        this.maxHeight=maxHeight;
        this.minHeight=minHeight;
        this.#transition = ""
    }

    setHandle(handles){
        this.handles = handles;
        this.#initEvents();
    }

    setTransition(transition){
        this.#transition = transition;
    }

    #initEvents(){
        for (let element of this.handles) {
            element.addEventListener("mousedown", this.#dragStart.bind(this), {passive:false});
            element.addEventListener("touchstart", this.#dragStart.bind(this), {passive:false});
        }
        document.addEventListener("mousemove", this.#dragging.bind(this), {passive:false});
        document.addEventListener("touchmove", this.#dragging.bind(this), {passive:false});

        document.addEventListener("mouseup", this.#dragStop.bind(this), {passive:false});
        document.addEventListener("touchend", this.#dragStop.bind(this), {passive:false});
    }


    #dragStart(e){
        this.panel.style.removeProperty("transition");
        this.#isDragging = true;
        let eventY = e.pageY || e.touches?.[0].pageY;
        this.#dragStartPos = eventY - this.#getBoundingRect().top;
    }

    #dragging(e){
        if(!this.#isDragging) return;
        let eventY = e.pageY || e.touches?.[0].pageY;
        let offset = this.#getBoundingRect().bottom + this.#dragStartPos - eventY;
        this.#setPanelHeight(cropValue(offset, this.minHeight, this.maxHeight));
    }

    #dragStop(){
        if(!this.#isDragging) return;
        this.#isDragging = false;
        this.roundHeight();
    }

    open(){
        if(this.isOpened()) return;
        this.panel.style.transition = this.#transition;
        this.#setPanelHeight(this.maxHeight);
    }

    close(){
        if(! this.isOpened()) return;
        this.panel.style.transition = this.#transition;
        this.#setPanelHeight(this.minHeight);
    }

    roundHeight(){
        if(this.#getBoundingRect().height - this.minHeight <=64) this.close();
    }

    isOpened(){
        return this.#getBoundingRect().height != this.minHeight;
    }

    #setPanelHeight(newHeight){
        this.panel.style.height=newHeight+"px";
    }

    #getBoundingRect(){
        return this.panel.getBoundingClientRect();
    }

}