export class Popup{
    parent;
    content;

    #isVisible
    #transition
    #isCreated;

    constructor(parent, content) {
        this.parent = parent;
        this.content = content;
        this.#isVisible = false;
        this.#transition = 200;//ms
        this.#isCreated = false;

        this.createHtml();
        this.hide(false).then();
    }


    async show(create = false) {
        if (create || !this.#isCreated) this.createHtml();
        await sleep(this.#transition);
        this.#isVisible = true;
        this.content.style.opacity = "1";
        this.content.style.pointerEvents = "auto";
        this.content.firstChild.style.transform = "none";
    }

    async hide(destroy = true) {
        this.#isVisible = false;
        this.content.style.opacity = "0";
        this.content.style.pointerEvents = "none";
        this.content.firstChild.style.transform = "scale(0)";
        await sleep(this.#transition);
        if (destroy) this.destroyHtml();
    }

    createHtml(){
        this.parent.appendChild(this.content);
        this.#isCreated = true;
    }

    destroyHtml(){
        this.parent.removeChild(this.content);
        this.#isCreated = false;
    }
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}