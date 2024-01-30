export class Popup{
    overlay;
    panel;


    #isVisible

    constructor(overlay, panel) {
        this.overlay = overlay;
        this.panel = panel;
        this.#isVisible = false;
        this.hide();
    }


    show(){
        this.#isVisible = true;
        this.overlay.style.opacity = "1";
        this.overlay.style.pointerEvents = "auto"
        this.panel.style.transform = "none";
    }

    hide(){
        this.#isVisible = false;
        this.overlay.style.opacity = "0";
        this.overlay.style.pointerEvents = "none";
        this.panel.style.transform = "scale(0)";
    }
}