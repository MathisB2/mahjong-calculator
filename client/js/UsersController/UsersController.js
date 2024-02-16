import {NetworkController} from "../NetworkController/NetworkController";
import {networkConfig} from "../config";

export let UsersManager = function() {
    let usersController = null;
    return {
        getController: function () {

            if (!usersController) {
                usersController = new UsersController();
            }
            usersController
            return usersController;
        }
    };

}();



class UsersController{
    username;
    password;


    constructor() {
        const logB = document.getElementById("logButton");
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        if(!(logB && username && password)) return;
        logB.addEventListener("click",this.login);
    }




    login(){
        let network = NetworkController.getController(networkConfig.ip, networkConfig.port);
        let nameSpace = network.getNetNamespace("loginNet")
        let callInfo = {
            username: this.username,
            password: this.password
        }

        nameSpace.call(JSON.stringify(callInfo)).then((success) => {
            if(success){
                let user = new Users(username,password);
                user.save();
            }
        })
    }

    save(){
        localStorage.setItem("user",JSON.stringify(this));
    }
}