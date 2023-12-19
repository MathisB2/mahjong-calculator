import {Signal} from "../Signal/Signal.js";
import {NetworkController} from "../NetworkController/NetworkController.js";

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

        const network = NetworkController.getController("localhost", 8080);
        const imageNet = network.getNetNamespace("ImageNet");

        this.imageNet =  imageNet;
        this.OnTilesReceived = new Signal();

        fileBTN.addEventListener("change", async (e) => {
            popup.style.display="flex";



            const file = e.target.files[0];
            let base64 = await this.convertBase64(file);

            imageNet.call(base64).then(function (callback) {
                this.OnTilesReceived.fire(callback);
                popup.style.display="none";
            });
        });
    }


    convertBase64 (file) {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file);

            fileReader.onload = () => {
                resolve(fileReader.result);
            };
            fileReader.onerror = (error) => {
                reject(error);
            };
        });
    };

}