import {UsersManager} from "./UsersController.js";

const username = document.getElementById("username");
const password = document.getElementById("password");
const form = document.getElementById("logForm");

export async function startLogin(){
    let usersController = UsersManager.get();
    if(!(form && username && password)) return;

    form.addEventListener("submit",(event) =>{
        event.preventDefault();
        usersController.log(username.value, password.value);
    });
}