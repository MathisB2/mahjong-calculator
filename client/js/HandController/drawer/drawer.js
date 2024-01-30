import {cropValue} from "../../calc.js";

export class Drawer{
    panel;
    handles;

    minHeight;
    maxHeight;

    #isDragging;
    #dragStartPos;


    constructor(drawerPanel, minHeight=100, maxHeight=500) {
        this.handles=[];
        this.panel = drawerPanel;
        this.#isDragging = false;
        this.#dragStartPos = 0;
        this.#initEvents();

        this.maxHeight=maxHeight;
        this.minHeight=minHeight;
    }

    setHandle(handles){
        this.handles = handles;
        this.#initEvents();
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
        console.log("dragstart");
        this.#isDragging = true;
        this.#dragStartPos = e.pageY - this.#getBoundingRect().top;
    }

    #dragging(e){
        if(!this.#isDragging) return;
        let offset = this.#getBoundingRect().bottom + this.#dragStartPos - e.pageY;

        this.#setPanelHeight(cropValue(offset, this.minHeight, this.maxHeight));
    }

    #dragStop(){
        if(!this.#isDragging) return;
        this.#isDragging = false;
        console.log("dragStop");

    }

    open(){
        this.#setPanelHeight(this.maxHeight);
    }

    close(){
        this.#setPanelHeight(this.minHeight)
    }

    #setPanelHeight(newHeight){
        this.panel.style.height=newHeight+"px";
    }

    #getBoundingRect(){
        return this.panel.getBoundingClientRect();
    }

}