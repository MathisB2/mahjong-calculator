import {NetworkController} from "./NetworkController.js";

async function main(){
    const textBox=document.getElementById("message")
    const sendButton=document.getElementById("send")


    let network = NetworkController.getController("localhost", 4444);
    let addition = network.getNetNamespace("addition");
    let multi = network.getNetNamespace("multi");

    addition.connect(function (message) {
        console.log("addition: "+message);
    })
    multi.connect(function (message){
        console.log("multi: "+message);
    })
    sendButton.onclick=function (){
        addition.send(textBox.value);
        multi.send(textBox.value);
    }
}


main();