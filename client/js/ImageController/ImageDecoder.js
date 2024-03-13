export function encode(file){
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
}

export function decode(base64){
    let image = new Image();
    image.src = base64;

    return image;
}