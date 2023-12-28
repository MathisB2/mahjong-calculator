import {NetworkController} from "../NetworkController/NetworkController.js";
import {storageConfig, networkConfig} from "../config.js";
import {Tile} from "./Tile.js";

const backButton = document.getElementById("settingsBackButton");
const sendButton = document.getElementById("settingsSendButton");

const gameWindInputs = document.getElementsByName("gameWind");
const playerWindInputs = document.getElementsByName("playerWind");
const playerFlowersInputs = document.getElementsByName("playerFlowers");
const playerSeasonsInputs = document.getElementsByName("playerSeasons");

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
        this._init();
    }

    _init(){
        let savedData = localStorage.getItem(storageConfig.hand)
        if(savedData != null) {
            let instance = JSON.parse(savedData);

            //winds
            for (let input of this.HTMLGameWindInputs) {
                input.checked=instance.gameWind.name == this._getType(input.id);
            }
            for (let input of this.HTMLPlayerWindInputs) {
                input.checked=instance.playerWind.name == this._getType(input.id);
            }

            //bonuses
            for (let input of this.HTMLPlayerFlowersInputs) {
                input.checked = false;
                let value = this._getType(input.id);

                for (let flower of instance.playerFlowers) {
                    if(flower.value == value){
                        input.checked = true;
                    }
                }
            }

            for (let input of this.HTMLPlayerSeasonsInputs) {
                input.checked = false;
                let value = this._getType(input.id);

                for (let season of instance.playerSeasons) {
                    if(season.value == value){
                        input.checked = true;
                    }
                }
            }

        }
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
        return name.replace("game","").replace("player","").replace("flower","").replace("season","");
    }

    update(){
        this._updateGameWind();
        this._updatePlayerWind();
        this._updatePlayerFlowers();
        this._updatePlayerSeasons();
    }


    gameWindToTile(){
        return this._getWindTile(this._getType(this.gameWind));
    }
    playerWindToTile(){
        return this._getWindTile(this._getType(this.playerWind));
    }
    _getWindTile(name){
        let t = new Tile(name);
        t.type="wind";
        t.value=this._bindWindtoInt(name);
        t.img=undefined;
        return t;
    }

    _bindWindtoInt(wind){
        switch (wind){
            case "North":
                return 1;
            case "South":
                return 2;
            case "East":
                return 3;
            default:
                return 5;
        }
    }

    playerFlowersToTiles(){
        let array=[];
        for (let flower of this.playerFlowers) {
            array.push(this._getBonusTile("flower",parseInt(this._getType(flower))));
        }
        return array;
    }

    playerSeasonsToTiles(){
        let array=[];
        for (let season of this.playerSeasons) {
            array.push(this._getBonusTile("season",parseInt(this._getType(season))));
        }
        return array;
    }

    _getBonusTile(name,value){
        let t = new Tile(name);
        t.type="bonus";
        t.value=value;
        t.img=undefined;
        return t;
    }
    saveToStorage(){
        let savedData = localStorage.getItem(storageConfig.hand)
        if(savedData != null) {
            let instance = JSON.parse(savedData);

            instance.gameWind = this.gameWindToTile();
            instance.playerWind = this.playerWindToTile();

            instance.playerFlowers = this.playerFlowersToTiles();
            instance.playerSeasons = this.playerSeasonsToTiles();

            localStorage.setItem(storageConfig.hand, JSON.stringify(instance));
            return true;
        }
        return false;
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
        settings.saveToStorage();
        let savedData = localStorage.getItem(storageConfig.hand)
        if(savedData != null){
            let instance = JSON.parse(savedData);
            console.log("sending to server...");

            scoreNet.call(JSON.stringify(instance)).then(function (message){
                console.log(message);
                console.log("... received from server");
                localStorage.setItem(storageConfig.score,"25");
                //window.location.href = "resultat.html";
            })
        }else{
            alert("error");
        }
    }
}

function onBackClick(){
    settings.update();
    settings.saveToStorage();
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
