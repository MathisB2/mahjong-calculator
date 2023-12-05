const convertBase64 = (file) => {
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
async function main(){
    let network = NetworkController.getController("localhost", 8080);
    let image = network.getNetNamespace("ImageNet");

    const fileBTN=document.getElementById("file")

    fileBTN.addEventListener("change", async (e) => {
        const file = e.target.files[0];
        let base64 = await convertBase64(file);

        image.call(base64).then(function (callback) {
            console.log(callback);
        });
    });
}


main();