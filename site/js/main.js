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


function sleep(ms) {
    var unixtime_ms = new Date().getTime();
    while(new Date().getTime() < unixtime_ms + ms) {}
}
async function main(){
    // let network = NetworkController.getController("172.22.69.111", 8080);
    // let image = network.getNetNamespace("ImageNet");
    //
    //  const fileBTN=document.getElementById("fileInput")
    const popup=document.querySelector(".popUp");

    fileBTN.addEventListener("change", async (e) => {
        popup.style.display="block";



        const file = e.target.files[0];
        let base64 = await convertBase64(file);

        image.call(base64).then(function (callback) {
            console.log(callback+" "+i);
        });
        popup.style.display="none";
    });
}




main();