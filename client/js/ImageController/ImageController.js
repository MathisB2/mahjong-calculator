import {NetworkController} from "../NetworkController/NetworkController.js";
import {imageConfig, networkConfig} from "../config.js";
import {ResultData} from "./resultData.js";
import {ResultPopup} from "./popup/resultPopup.js";
import {LoadingPopup} from "./popup/loadingPopup.js";
import {decode, encode} from "./imageDecoder.js";

export let ImageManager = function() {
    let imageController = null;
    return {
        getController: function () {

            if (!imageController) {
                imageController = new ImageController();
            }
            return imageController;
        }
    };

}();

class ImageController{
    imageNet;

    constructor() {
        const fileBTN = document.getElementById("fileInput")
        const main = document.querySelector(".calculator");
        if(fileBTN == null || main == null) return;

        const loadingPopup = new LoadingPopup(main);

        let network = NetworkController.getController(networkConfig.ip, networkConfig.port);
        const imageNet = network.getNetNamespace("ImageNet");

        this.imageNet =  imageNet;

        fileBTN.addEventListener("change", async (e) => {
            const file = e.target.files[0];
            if((file.type.indexOf("image")) == -1) {
                alert("Le format du fichier n'est pas pris en charge");
                return;
            }

            loadingPopup.show();

            let resized = await this.resizeImage(file, imageConfig.maxWidth);
            let base64 = await encode(resized);

            const controller = this

            imageNet.call(base64).then(async function (callback) {

                let clusterOfTiles = controller.#decodeCallback(callback);

                let resultData = new ResultData(resized, clusterOfTiles);

                let resultPopup = new ResultPopup(main, resultData);

                resultPopup.show()
                loadingPopup.hide();
            });
        });
    }

    #decodeCallback(callBack){
        let decodedCallBack = JSON.parse(callBack);

        for (let cluster of decodedCallBack) {
            for(let tile of cluster){
                tile.image = decode(tile.image);
            }
        }

        return decodedCallBack;
    }

    resizeImage(file, maxSize) {
        return new Promise((resolve) => {
            const reader = new FileReader();

            reader.onload = (readerEvent) => {
                const image = new Image();

                image.onload = () => {
                    const canvas = document.createElement('canvas');
                    const context = canvas.getContext('2d');

                    const [width, height] = this.#getSize(image.width, image.height, maxSize);
                    canvas.width = width;
                    canvas.height = height;

                    context.drawImage(image, 0, 0, width, height);

                    canvas.toBlob((blob) => {
                        resolve(blob);
                    }, file.type);
                };

                image.src = readerEvent.target.result;
            };
            reader.readAsDataURL(file);
        });
    }

    #getSize(width, height, maxSize){
        if(width < maxSize && height < maxSize) {
            return [width,height];
        }

        if (width > height && width > maxSize) {
            return [ maxSize, height * (maxSize / width)];
        }

        return  [width * (maxSize / height), maxSize];
    }
}