export class AnimationFolder{
    path;
    extension;
    baseName;
    padSize;

    #frames;
    frameStart;
    frameEnd;

    width;
    height;
    constructor(folderPath, fileExtension, width, height, frameEnd, frameStart=1, baseName="", padSize=4) {
        this.path=folderPath;
        this.extension=fileExtension;
        this.baseName=baseName;
        this.padSize=padSize;

        this.#frames=[];
        this.frameStart=frameStart;
        this.frameEnd=frameEnd;

        this.width=width;
        this.height=height;

    }


    loadFrames() {

        for (let i = 0; i <= this.frameEnd - this.frameStart; i++) {
            let id = String(i + this.frameStart).padStart(this.padSize, "0");
            let path = this.path + "/" + this.baseName + id + "." + this.extension;
            let img = new Image();
            img.src = path;
            this.#frames.push(img);

            if (i == 0) {
                //ensure the first image is fully loaded before computing the size
                let self = this;
                img.onload = function () {
                    self.width = img.width;
                    self.height = img.height;
                };
            }
        }
    }


    getFrame(progress){
        return this.#frames[this.#getFrameIndex(progress)];
    }

    getFrameCount(){
        return this.frameEnd-this.frameStart+1;
    }

    #getFrameIndex(progress){
        let index = Math.round (mapValue(progress,0,1,0,this.getFrameCount()));

        if(index <= this.frameStart) return 0;
        if(index >= this.frameEnd) return this.frameEnd-this.frameStart;

        return index;
    }

}





export function  cropValue(value, min, max){
    if(isNaN(value)) return max;
    if(value<=min) return min;
    if(value>=max) return max;
    return value;
}

export function mapValue (value, fromMin, fromMax, toMin, toMax){
    return ((value - fromMin) * (toMax - toMin)) / (fromMax - fromMin) + toMin;
}