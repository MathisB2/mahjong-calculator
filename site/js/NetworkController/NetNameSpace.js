export class NetNameSpace {
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