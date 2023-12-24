import {NetNameSpace} from "./NetNameSpace.js";

export class Network {
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