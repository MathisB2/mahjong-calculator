import {NetworkController} from "../NetworkController/NetworkController.js";
import {networkConfig} from "../config.js";
import {Tile} from "./Tile.js";

const backButton = document.getElementById("settingsBackButton");
const sendButton = document.getElementById("settingsSendButton");

const gameWindInputs = document.getElementsByName("gameWind");
const playerWindInputs = document.getElementsByName("playerWind");
const playerFlowersInputs = document.getElementsByName("playerFlowers");
const playerSeasonsInputs = document.getElementsByName("playerSeasons");

const DATA_NAME = 'mahjongHand';
let settings;
let scoreNet;

class gameSettings{
    gameWind="gameNorth";
    playerWind="playerNorth";
    playerFlowers=[];
    playerSeasons=[];

    HTMLGameWindInputs;
    HTMLPlayerWindInputs;
    HTMLPlayerFlowersInputs;
    HTMLPlayerSeasonsInputs;

    constructor(gameWindFormName,playerWindInputs,playerFlowersInputs,playerSeasonsInputs) {
        this.HTMLGameWindInputs=gameWindFormName;
        this.HTMLPlayerWindInputs=playerWindInputs;
        this.HTMLPlayerFlowersInputs=playerFlowersInputs;
        this.HTMLPlayerSeasonsInputs=playerSeasonsInputs;
    }

    _updateGameWind(){
        for(let i = 0; i < this.HTMLGameWindInputs.length; i++){
            if(this.HTMLGameWindInputs[i].checked){
                this.gameWind = this._getType(this.HTMLGameWindInputs[i].id);
            }
        }
    }

    _updatePlayerWind(){
        for(let i = 0; i < this.HTMLPlayerWindInputs.length; i++){
            if(this.HTMLPlayerWindInputs[i].checked){
                this.playerWind = this._getType(this.HTMLPlayerWindInputs[i].id);
            }
        }
    }

    _updatePlayerFlowers(){
        this.playerFlowers=[];
        for(let i = 0; i < this.HTMLPlayerFlowersInputs.length; i++){
            if(this.HTMLPlayerFlowersInputs[i].checked){
                this.playerFlowers.push(this.HTMLPlayerFlowersInputs[i].id);
            }
        }
    }

    _updatePlayerSeasons(){
        this.playerSeasons=[];
        for(let i = 0; i < this.HTMLPlayerSeasonsInputs.length; i++){
            if(this.HTMLPlayerSeasonsInputs[i].checked){
                this.playerSeasons.push(this.HTMLPlayerSeasonsInputs[i].id);
            }
        }
    }

    _getType(name){
        return name.replace("game","").replace("player","");
    }

    update(){
        this._updateGameWind();
        this._updatePlayerWind();
        this._updatePlayerFlowers();
        this._updatePlayerSeasons();
        console.log(this)

    }

    isSet(){
        return this.HTMLGameWindInputs!=null && this.HTMLPlayerWindInputs!=null && this.HTMLPlayerSeasonsInputs!=null && this.HTMLPlayerFlowersInputs!=null
    }

    isValid(){
        return this.gameWind!=null && this.playerWind!=null;
    }
}


function onSendClick(){
    if(settings.isSet() && settings.isValid()){
        settings.update();
        let savedData = localStorage.getItem(DATA_NAME)
        console.log(savedData);
        if(savedData != null){
            let instance = JSON.parse(savedData);

            instance.gameWind = new Tile(settings.gameWind);
            instance.playerWind = new Tile(settings.playerWind);

            instance.playerFlowers = settings.playerFlowers;
            instance.playerSeasons = settings.playerSeasons;

            localStorage.setItem(DATA_NAME,instance.toString());

            console.log("sending to server...");
            console.log(instance);

            scoreNet.call(JSON.stringify(instance)).then(function (message){
                console.log(message);
                console.log("... received from server");
                //window.location.href = "resultat.html";
            })
        }else{
            alert("error");
        }
    }
}

function onBackClick(){
    window.location.href = "calcul.html";
}

export async function startSettings(){
    if(!(gameWindInputs
        && playerWindInputs
        && playerFlowersInputs
        && playerSeasonsInputs
        && backButton
        && sendButton)) return;
    let network = NetworkController.getController(networkConfig.ip, networkConfig.port);
    scoreNet = network.getNetNamespace("ScoreNet");
    settings = new gameSettings(gameWindInputs,playerWindInputs,playerFlowersInputs,playerSeasonsInputs);

    document.addEventListener("mouseup",settings.update.bind(settings));
    backButton.addEventListener("click", onBackClick);
    sendButton.addEventListener("click", onSendClick);
}
