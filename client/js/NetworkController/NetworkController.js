import {Network} from "./Network.js";

export let NetworkController = function() {
    let networks = {};
    function createInstance(name) {
        let newNetwork = new Network(name);
        return newNetwork;
    }
    function isNetworkExist(name) {
        let network = networks[name];
        return network != null;
    }

    return {
        getController: function (ip, port) {
            let name = "ws://".concat(ip, ":", port.toString());
            if (!isNetworkExist(name)) {
              this.loadController(ip, port);
            }
            return networks[name];
        },
        loadController: function (ip, port){
            let name = "ws://".concat(ip, ":", port.toString());
            let network = createInstance(name);
            networks[name] = network;
        }
    };

}();
