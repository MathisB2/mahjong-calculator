const backButton=document.getElementById("settingsBackButton");
const sendButton=document.getElementById("settingsSendButton");


function onBackClick(){
    window.location.href = "calcul.html";
}


function onSendClick(){
    if(true) {   //vérifier si tous les champs du formulaire sont remplis avant d'envoyer
        console.log("envoi au serveur");
        // window.location.href = "resultat.html";
    }
}


if(backButton){
    backButton.addEventListener("click", onBackClick);
}


if(sendButton){
    sendButton.addEventListener("click", onSendClick);
}