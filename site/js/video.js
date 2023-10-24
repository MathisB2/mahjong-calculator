async function main () {

  
    const videoLive = document.querySelector('#videoLive')
    
  
    const stream = await navigator.mediaDevices.getUserMedia({ // <1>
      video: true,
      audio: false,
    })
    /*
    const myTracks=stream.getTracks();
    const myVideo=myTracks.filter(track=>track.kind === "video")[0];
    track.enabled=true;
  */
    videoLive.srcObject = stream
  
  }


  
  main()