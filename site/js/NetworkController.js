let NetworkController = function() {
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

class Network {
    namespaces = {};
    queue = {};
    socket;
    constructor(name) {
        this.socket = new WebSocket(name);
        this._loadConnections();
        this._setFataErrorNamespace();
    }
    _loadConnections(){
        const socket = this.socket;
        const namespaces = this.namespaces;
        const network = this;
        socket.onopen = function () {
            console.log('Connected!');
        };

        socket.onerror = function (error) {
            console.log('WebSocket Error ' + error);
        };

        socket.onmessage = function (e) {
            const serverCall = JSON.parse(e.data);
            let nameNet = serverCall.name;
            if (namespaces[nameNet] == null){console.error(nameNet + " is not defined"); return;}

            network._addToQueue(serverCall.messageId, serverCall.message);
            if(!serverCall.ended) return;
            serverCall.message = network._getToQueue(serverCall.messageId);

            if(serverCall.callId != "none"){
                namespaces[nameNet].resumeCall(serverCall.callId, serverCall.message, serverCall.error === true);
            }else{
                namespaces[nameNet]._fireConnections(serverCall.message);
            }
        };
    }

    _addToQueue(messageId, message){
        let waitingMessage = this.queue[messageId] || "";
        this.queue[messageId] = waitingMessage + message;
    }

    _getToQueue(messageId){
        const message = this.queue[messageId];
        this.queue[messageId] = null;
        return message;
    }
    _setFataErrorNamespace(){
        const fatalError = this.getNetNamespace("FatalError")
        fatalError.connect(function (message){
            console.error(message);
            this.namespaces = {};
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
    connectedFunctions = {};
    calls = {};
    network;
    constructor(name, network) {
        this.name = name;
        this.network = network;
    }

    _fireConnections(message){
        for (const key in this.connectedFunctions) {
            this.connectedFunctions[key](message);
        }
    }

    resumeCall(callId, message, error){
        const call = this.calls[callId]
        if(call == null) console.error("call expired !");
        this.calls[callId] = null;

        if(error){
            call.reject(message);
        }else{
            call.resolve(message);
        }
    }

    send(message){
       this._send(message, "none")
    }

    call(message){
        const callId = this._generateUUID()
        let calls = this.calls;
        return new Promise((resolve, reject) => {;
            this.calls[callId] = {
                resolve: resolve,
                reject: reject
            };
            setTimeout(reject, 30000);
            this._send(message, callId);
        }).catch(function (message){
            console.error(message);
            if(calls[callId]) calls[callId] = null;
        });
    }
    _send(message, callId){
        const messageId = this._generateUUID();
        const limitLength = 30000;
        const nbrSplit = Math.ceil(message.length/limitLength);
        console.log(message.length);
        for(let i = 0; i < nbrSplit; ++i){
            const pack = message.substring(i*limitLength, (i+1)*limitLength);

            this.network._send(JSON.stringify({
                callId: callId,
                messageId: messageId,
                ended: i+1 == nbrSplit,
                name: this.name,
                message: pack,
            }))
        }
    }

    connect(foo){
        let functionId = this._generateUUID();
        this.connectedFunctions[functionId] = foo;
        return functionId;
    }

    disconnect(functionId){
        this.connectedFunctions[functionId] = null;
    }
    _generateUUID() { // Public Domain/MIT
        let d = new Date().getTime();//Timestamp
        let d2 = ((typeof performance !== 'undefined') && performance.now && (performance.now()*1000)) || 0;//Time in microseconds since page-load or 0 if unsupported
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            let r = Math.random() * 16;
            if(d > 0){
                r = (d + r)%16 | 0;
                d = Math.floor(d/16);
            } else {
                r = (d2 + r)%16 | 0;
                d2 = Math.floor(d2/16);
            }
            return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    }
}
