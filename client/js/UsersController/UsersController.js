import {NetworkController} from "../NetworkController/NetworkController.js";
import {networkConfig} from "../config.js";

export let UsersManager = function() {
    let usersController = null;
    return {
        get: function () {
            if (usersController == null) {
                this.load();
            }
            return usersController;
        },

        load: function () {
            if(usersController != null) return;
            usersController = new UsersController();
        }
    };
}();


class UsersController{
    #loginNet;
    #loggedNet;

    constructor() {
        const network = NetworkController.getController(networkConfig.ip, networkConfig.port);
        this.#loginNet = network.getNetNamespace("loginNet");
        this.#loggedNet = network.getNetNamespace("loggedNet");
    }

    async isLogged(){
        return await this.#loggedNet.call();
    }

    log(username, password){
        if(!this.#isValid(username)) return;
        let callInfo = {
            username: username,
            password: password
        };

        this.#loginNet.call(JSON.stringify(callInfo)).then((success) => {
            console.log(success);
        });
    }

    logout(){
        if(!this.isLogged()) return;
    }

    #isValid(username){
        return username != "";
    }


    save(){
        localStorage.setItem("user",JSON.stringify(this));
    }
}