const backButton=document.getElementById("settingsBackButton");
const sendButton=document.getElementById("settingsSendButton");

const gameWindInputs=document.getElementsByName("gameWind");
const playerWindInputs=document.getElementsByName("playerWind");
const playerFlowersInputs=document.getElementsByName("playerFlowers");
const playerSeasonsInputs=document.getElementsByName("playerSeasons");



function onBackClick(){
    window.location.href = "calcul.html";
}
function onSendClick(){
    if(settings.isSet() && settings.isValid()){
        settings.update();
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





class gameSettings{
    gameWind="gameNorth"
    playerWind="playerNorth"
    playerFlowers=[];
    playerSeasons=[];

    HTMLgameWindInputs;
    HTMLplayerWindInputs;
    HTMLplayerFlowersInputs;
    HTMLplayerSeasonsInputs;

    constructor(gameWindFormName,playerWindInputs,playerFlowersInputs,playerSeasonsInputs) {
        this.HTMLgameWindInputs=gameWindFormName;
        this.HTMLplayerWindInputs=playerWindInputs;
        this.HTMLplayerFlowersInputs=playerFlowersInputs;
        this.HTMLplayerSeasonsInputs=playerSeasonsInputs;
    }



    _updateGameWind(){
        for(let i = 0; i < this.HTMLgameWindInputs.length; i++){
            if(this.HTMLgameWindInputs[i].checked){
                this.gameWind = this.HTMLgameWindInputs[i].id;
            }
        }
    }

    _updatePlayerWind(){
        for(let i = 0; i < this.HTMLplayerWindInputs.length; i++){
            if(this.HTMLplayerWindInputs[i].checked){
                this.playerWind = this.HTMLplayerWindInputs[i].id;
            }
        }
    }

    _updatePlayerFlowers(){
        this.playerFlowers=[];
        for(let i = 0; i < this.HTMLplayerFlowersInputs.length; i++){
            if(this.HTMLplayerFlowersInputs[i].checked){
                this.playerFlowers.push(this.HTMLplayerFlowersInputs[i].id);
            }
        }
    }

    _updatePlayerSeasons(){
        this.playerSeasons=[];
        for(let i = 0; i < this.HTMLplayerSeasonsInputs.length; i++){
            if(this.HTMLplayerSeasonsInputs[i].checked){
                this.playerSeasons.push(this.HTMLplayerSeasonsInputs[i].id);
            }
        }
    }

    update(){
        this._updateGameWind();
        this._updatePlayerWind();
        this._updatePlayerFlowers();
        this._updatePlayerSeasons();
        console.log(this)

    }


    isSet(){
        return this.HTMLgameWindInputs!=null && this.HTMLplayerWindInputs!=null && this.HTMLplayerSeasonsInputs!=null && this.HTMLplayerFlowersInputs!=null
    }

    isValid(){
        return this.gameWind!=null && this.playerWind!=null;
    }
}


let settings;
if(gameWindInputs && playerWindInputs && playerFlowersInputs && playerSeasonsInputs){
    settings=new gameSettings(gameWindInputs,playerWindInputs,playerFlowersInputs,playerSeasonsInputs);
    document.addEventListener("mouseup",settings.update.bind(settings));
}
