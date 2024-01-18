class Connection {
    signal;
    id;

    constructor(signal, id) {
        this.signal = signal;
        this.id = id;
    }

    disconnect(){
        delete this.signal.connections[this.id];
        this.signal.connections[this.id] = null;
    }
}
export class Signal {
    connections;
    curr_id = 0;

    constructor() {
        this.connections = {};
    }

    connect(fn){
        ++this.curr_id;
        this.connections[this.curr_id] = fn;

        return new Connection(this, this.curr_id);
    }

    fire(...args){
        for(let key in this.connections)
            this.connections[key](args);
    }

    clear(){
        this.connections = {};
    }
}