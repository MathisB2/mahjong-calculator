import {NetworkController} from "../NetworkController/NetworkController.js";
import {networkConfig} from "../config.js";

const logB = document.getElementById("logButton");
const username = document.getElementById("username").value;
const password = document.getElementById("password").value;

class Users{



    save(){
        localStorage.setItem("user",JSON.stringify(this));
    }
}

export async function startLogin(){
    if(!(logB && username && password)) return;
    logB.addEventListener("click",login);
}
