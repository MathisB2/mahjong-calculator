import {Signal} from "../Signal/Signal.js";
import {NetworkController} from "../NetworkController/NetworkController.js";
import {imageConfig, networkConfig} from "../config.js";
import {ResultData} from "./resultData.js";
import {ResultPopup} from "./popup/resultPopup.js";
import {LoadingPopup} from "./popup/loadingPopup.js";

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
    OnTilesReceived;
    imageNet;

    constructor() {
        const fileBTN = document.getElementById("fileInput")
        const resultOverlay = document.querySelector("#resultOverlay");
        const loadingOverlay = document.querySelector("#loadingOverlay");
        if(fileBTN == null || resultOverlay == null || loadingOverlay==null) return;

        const loadingPopup = new LoadingPopup(loadingOverlay);
        let resultPopup;

        let network = NetworkController.getController(networkConfig.ip, networkConfig.port);
        const imageNet = network.getNetNamespace("ImageNet");

        this.imageNet =  imageNet;
        this.OnTilesReceived = new Signal();

        fileBTN.addEventListener("change", async (e) => {
            const file = e.target.files[0];
            if((file.type.indexOf("image")) == -1) {
                alert("Le format du fichier n'est pas pris en charge");
                return;
            }

            loadingPopup.show();
            await new Promise((resolve) => setTimeout(resolve, 4000));

            let resized = await this.resizeImage(file,imageConfig.maxWidth);
            let base64 = await this.convertBase64(resized);

            let controller = this;

            imageNet.call(base64).then(function (callback) {
                controller.OnTilesReceived.fire(callback);

                let resultData = new ResultData(resized,callback);
                resultPopup = new ResultPopup(resultOverlay,resultData);
                loadingPopup.hide()
                resultPopup.show()
            });
        });
    }


    convertBase64 (file) {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();

            fileReader.onload = () => {
                resolve(fileReader.result);
            };
            fileReader.onerror = (error) => {
                reject(error);
            };

            fileReader.readAsDataURL(file);
        });
    };


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