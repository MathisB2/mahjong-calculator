import {Signal} from "../Signal/Signal.js";
import {NetworkController} from "../NetworkController/NetworkController.js";
import {imageConfig, networkConfig} from "../config.js";

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
        const popup = document.querySelector(".popUp");
        if(fileBTN == null || popup == null) return;

        let network = NetworkController.getController(networkConfig.ip, networkConfig.port);
        const imageNet = network.getNetNamespace("ImageNet");

        this.imageNet =  imageNet;
        this.OnTilesReceived = new Signal();

        fileBTN.addEventListener("change", async (e) => {
            popup.style.display="flex";

            const file = e.target.files[0];
            let resized = await this.resizeImage(file,imageConfig.maxWidth);
            let base64 = await this.convertBase64(resized);
            console.log(base64);

            imageNet.call(base64).then(function (callback) {
                this.OnTilesReceived.fire(callback);
                popup.style.display="none";
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

                    let width = image.width;
                    let height = image.height;

                    if (width > height) {
                        if (width > maxSize) {
                            height *= maxSize / width;
                            width = maxSize;
                        }
                    } else {
                        if (height > maxSize) {
                            width *= maxSize / height;
                            height = maxSize;
                        }
                    }

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


}