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

    frames;
    frameStart;
    frameEnd;
    #currentFrame;

    #enabled;


    constructor(canvas, startOffset=0, endOffset=0) {
        this.#canvas=canvas;
        this.#context=this.#canvas.getContext('2d');
        this.startOffset=startOffset;
        this.endOffset=endOffset;
        this.frames=[];
        this.minScrollRange=100;
        this.#enabled=false

        document.addEventListener("scroll", this.update.bind(this));
        window.addEventListener("resize", this.#computeSize.bind(this));

    }


    #computeSize(){
        if(this.frames.length==0) return;
        this.#updateCanvasSize();
        const rect = this.#canvas.getBoundingClientRect();

        let scrollTop = document.documentElement.scrollTop;
        this.width=rect.width;

        this.height=rect.height;
        console.log("h",this.height);
        this.#scrollEnd=scrollTop+rect.top;
        this.#scrollStart=this.#scrollEnd - window.innerHeight + this.height;
        this.updateStatus();
    }

    #updateCanvasSize(){
        let frame = this.frames[0];
        let ratio = frame.height / frame.width;
        console.log(frame.naturalHeight);
        this.#canvas.height = ratio * this.#canvas.width;

    }

    updateStatus(){
        if(this.getScrollRange()<this.minScrollRange){
            this.disable();
        }else{
            this.enable();
        }
    }


    loadFolder(folderPath, fileExtension, frameEnd, frameStart=1, baseName="", padSize=4){
        this.frameStart=frameStart;
        this.frameEnd= frameEnd;
        for(let i=0; i<=frameEnd-frameStart; i++){
            let id=String(i+frameStart).padStart(padSize,"0");
            let path=folderPath+"/"+baseName+id+"."+fileExtension;
            let img=new Image();
            img.src=path;
            this.frames.push(img);

            if(i==0) {
                //ensure the first image is fully loaded before computing the size
                let self=this;
                img.onload = function() {
                    self.#computeSize();
                };
            }



        }
        this.update();
    }



    #mapValue (value, fromMin, fromMax, toMin, toMax){
        return ((value - fromMin) * (toMax - toMin)) / (fromMax - fromMin) + toMin;
    }

    #cropValue(value, min, max){
        if(isNaN(value)) return max;
        if(value<=min) return min;
        if(value>=max) return max;
        return value;
    }


    getScrollRange(){
        return this.#scrollEnd+this.endOffset-this.#scrollStart-this.startOffset;
    }



    #getAnimProgress(){
        if(window.scrollY <= this.#scrollStart+this.startOffset) return 0;
        if(window.scrollY >= this.#scrollEnd+this.endOffset) return 1;
        return this.#cropValue((this.#scrollStart + this.startOffset - window.scrollY) / (this.#scrollStart + this.startOffset - this.#scrollEnd - this.endOffset),0,1);

    }

    #getFrameIndex(progress){
        let index = Math.round (this.#mapValue(progress,0,1,this.frameStart,this.frameEnd));

        if(index <= this.frameStart) return 0;
        if(index >= this.frameEnd) return this.frameEnd-this.frameStart;

        return index;

    }


    update(){
        console.log("log",this.#getAnimProgress());
        let index;
        if(this.#enabled){
            index = this.#getFrameIndex(this.#getAnimProgress());
        }else{
            index = this.frameEnd-this.frameStart;
        }
        if(index == this.#currentFrame) return;

        this.#currentFrame = index;


        this.#context.clearRect(0, 0, this.#canvas.width, this.#canvas.height);
        this.#context.drawImage(this.frames[index], 0, 0, this.#canvas.width, this.#canvas.height);

    }


    enable(){
        this.#enabled=true;
        console.log("animation enabled");
    }

    disable(){
        this.#enabled=false;
        console.log("animation disabled");
    }


}







export async function startAnimations(){
    if(uiCanvas){
        let uiAnimation=new ScrollAnimation(uiCanvas,0,0);
        uiAnimation.loadFolder("img/animations/uiAnimation","png",48,);
        // let height=uiAnimation.height;
        //
        // uiAnimation.startOffset=-height/2;
        // uiAnimation.minScrollRange = 240;   // 1frame/5px


    }
}