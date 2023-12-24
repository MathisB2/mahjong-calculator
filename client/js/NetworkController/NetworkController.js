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
            console.log(name);
            if (!isNetworkExist(name)) {
                let network = createInstance(name);
                networks[name] = network;
            }
            return networks[name];
        }
    };

}();
