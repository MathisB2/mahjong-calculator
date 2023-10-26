
// envoie img 



function convertDataURIToBinary(dataURI) {
	var BASE64_MARKER = ';base64,';
	var base64Index = dataURI.indexOf(BASE64_MARKER) + BASE64_MARKER.length;
	var base64 = dataURI.substring(base64Index);
	var raw = window.atob(base64);
	var rawLength = raw.length;
	var array = new Uint8Array(new ArrayBuffer(rawLength));

	for(i = 0; i < rawLength; i++) {
		array[i] = raw.charCodeAt(i);
	}
	return array;
}



function readFile(evt) {
    var f = evt.target.files[0]; 

    if (f) {
		if ( /(jpe?g|png|gif)$/i.test(f.type) ) {
			var r = new FileReader();
			r.onload = function(e) { 
				var base64Img = e.target.result;
                connection.send(base64Img);
			}
			r.readAsDataURL(f);
		} else { 
			alert("Failed file type");
		}
    } else { 
		alert("Failed to load file");
    }
}


document.getElementById('file').addEventListener('change', readFile, false);






// vidéo

async function main () {

    
	if ( document.URL.includes("photo.html") ) {

	const videoLive = document.querySelector('#videoLive')
	
    const stream = await navigator.mediaDevices.getUserMedia({ // <1>
      video: true,
      audio: false,
    })

	videoLive.srcObject = stream

}  
}


  
  main()



// envoie message





const textBox=document.getElementById("message")
const sendButton=document.getElementById("send")

var connection = new WebSocket('ws://172.22.69.111:8080');


connection.onopen = function () {
    console.log('Connected!');
    connection.send('Ping'); // Send the message 'Ping' to the server
};

// Log errors
connection.onerror = function (error) {
    console.log('WebSocket Error ' + error);
};

// Log messages from the server
connection.onmessage = function (e) {
    console.log('Server: ' + e.data);
};


sendButton.onclick=function (){
    connection.send(textBox.value);
}




// site web





function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
  }
  
  function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
  }







