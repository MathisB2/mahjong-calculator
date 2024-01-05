import {AnimationFolder, cropValue} from "./animationFolder.js";

const uiCanvas = document.getElementById("indexUiAnimation");

class ScrollAnimation{
    #canvas;
    #context;
    startOffset; //offset in px from the top of the img
    endOffset;  //offset in px from the bottom of the img

    #scrollStart;
    #scrollEnd;
    minScrollRange;
    width;
    height;

    #folder;
    #enabled;


    constructor(canvas, folder, startOffset=0, endOffset=0) {
        this.#canvas=canvas;
        this.#context=this.#canvas.getContext('2d');
        this.#folder=folder;
        this.startOffset=startOffset;
        this.endOffset=endOffset;
        this.minScrollRange=100;
        this.#enabled=false

        this.#computeSize();
        document.addEventListener("scroll", this.update.bind(this));
        window.addEventListener("resize", this.#computeSize.bind(this));
    }


    #computeSize(){
        if(this.#folder.getFrameCount()==0) return;
        this.#updateCanvasSize();
        const rect = this.#canvas.getBoundingClientRect();

        let scrollTop = document.documentElement.scrollTop;
        this.width=rect.width;

        this.height=rect.height;
        this.#scrollEnd=scrollTop+rect.top;
        this.#scrollStart=this.#scrollEnd - window.innerHeight + this.height;
        this.updateStatus();
    }

    #updateCanvasSize(){
        let ratio = this.#folder.height / this.#folder.width;
        this.#canvas.height = ratio * this.#canvas.width;

    }

    updateStatus(){
        if(this.getScrollRange()<this.minScrollRange){
            this.disable();
        }else{
            this.enable();
        }
    }



    getScrollRange(){
        return this.#scrollEnd+this.endOffset-this.#scrollStart-this.startOffset;
    }



    #getAnimProgress(){
        if(window.scrollY <= this.#scrollStart+this.startOffset) return 0;
        if(window.scrollY >= this.#scrollEnd+this.endOffset) return 1;
        return cropValue((this.#scrollStart + this.startOffset - window.scrollY) / (this.#scrollStart + this.startOffset - this.#scrollEnd - this.endOffset),0,1);
    }


    update(){
        let progress = 1;
        if(this.#enabled)
            progress = this.#getAnimProgress();

        let frame = this.#folder.getFrame(progress);

        // if(frame == this.#currentFrame) return;

        this.#context.clearRect(0, 0, this.#canvas.width, this.#canvas.height);
        this.#context.drawImage(frame, 0, 0, this.#canvas.width, this.#canvas.height);

    }



    enable(){
        this.#enabled=true;
        this.update();
        console.log("animation enabled");
    }

    disable(){
        this.#enabled=false;
        console.log("animation disabled");
    }


}







export async function startAnimations(){
    if(uiCanvas){
        let uiFolder = new AnimationFolder("img/animations/uiAnimation","png", 540, 960,48,);
        await uiFolder.loadFrames();

        let uiAnimation=new ScrollAnimation(uiCanvas,uiFolder,0,0);
        let height=uiAnimation.height;
        uiAnimation.startOffset=-height/2;
        uiAnimation.minScrollRange = 240;   // 1frame/5px
    }
}