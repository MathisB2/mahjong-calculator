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
            let name = "ws://".concat(ip, ":", port.toString())
            if (!isNetworkExist(name)) {
                let network = createInstance(name);
                networks[name] = network;
            }
            return networks[name];
        }
    };

}();

class Network {
    namespaces = {};
    socket;
    constructor(name) {
        this.socket = new WebSocket(name);
        this._loadConnections();
        this._setErrorNamespace();
    }

    _loadConnections(){
        const socket = this.socket;
        const namespaces = this.namespaces;
        socket.onopen = function () {
            console.log('Connected!');
        };

        socket.onerror = function (error) {
            console.log('WebSocket Error ' + error);
        };

        socket.onmessage = function (e) {
            let messageInfo = e.data;
            let strings = messageInfo.split('/');
            let nameNet = strings[0];

            namespaces[nameNet]._fireConnections(strings[1]);
        };
    }

    _setErrorNamespace(){
        const namespace = this.getNetNamespace("error");
        namespace.connect(function (message){
            console.error(message);
        })
    }

    getNetNamespace(name){
        if (this.namespaces[name] == null) {
            let network = new NetNameSpace(name, this);
            this.namespaces[name] = network;
        }
        return this.namespaces[name];
    }
    _send(message){
        this.socket.send(message);
    }

}

class NetNameSpace {
    name;
    connectedFunctions = new Array();
    network;
    constructor(name, network) {
        this.name = name;
        this.network = network;
    }

    _fireConnections(message){
        for (let foo of this.connectedFunctions) {
            foo(message);
        }
    }

    send(message){
        this.network._send(this.name+"/"+message)
    }

    connect(foo){
        this.connectedFunctions.push(foo);
        return this.connectedFunctions.length - 1;
    }

    disconnect(index){
        this.connectedFunctions.slice(index, 1)
    }
}
