if ( document.URL.includes("photo.html") ) {
    const input = document.getElementById("file");
}




async function main () {

    let network = NetworkController.getController("localhost", 8080);
    let image = network.getNetNamespace("image");

	image.connect(function (message) {
        console.log(message);
    })

    if ( document.URL.includes("photo.html") ) {
        const videoLive = document.querySelector('#videoLive')
        const stream = await navigator.mediaDevices.getUserMedia({
          video: true,
          audio: false,
        })
        videoLive.srcObject = stream
    }



	if ( document.URL.includes("index.html") ) {
        const textBox = document.getElementById("message")
        const sendButton = document.getElementById("send")

        sendButton.onclick=function (){
            image.send(textBox.value);
    }
	}

	
}


// function

if ( document.URL.includes("photo.html") ) {
const input = document.getElementById("file");
const textArea = document.getElementById("textArea");

const convertBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const fileReader = new FileReader();
    fileReader.readAsDataURL(file);

    fileReader.onload = () => {
      resolve(fileReader.result);
    };
    fileReader.onerror = (error) => {
      reject(error);
    };
  });
};


const uploadImage = async (event) => {
	let network = NetworkController.getController("localhost", 8080);
    let image = network.getNetNamespace("image");

    const file = event.target.files[0];
    var base64 = await convertBase64(file);
    textArea.innerText = base64;
    image.send(message);
};


input.addEventListener("change", (e) => {
  uploadImage(e);
});
}



function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
  }
  
function closeNav() {
	document.getElementById("mySidenav").style.width = "0";
}


// connection bien chiante
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
            let name = "ws://".concat(ip, ":", port.toString())
            if (!isNetworkExist(name)) {
                let network = createInstance(name);
                networks[name] = network;
            }
            return networks[name];
        }
    };

}();
function _getMessageFrom(strings){
    let message = strings[1];

    for(let i = 2; i < strings.length; ++i){
        message += "/"+strings[i];
    }

    return message;
}
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
            if (namespaces[nameNet] == null){console.error(nameNet + " is not defined"); return;}
            namespaces[nameNet]._fireConnections(_getMessageFrom(strings));
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
  
  main()