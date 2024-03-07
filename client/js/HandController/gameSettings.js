import {NetworkController} from "../NetworkController/NetworkController.js";
import {storageConfig, networkConfig} from "../config.js";
import {Tile} from "./hand/Tile.js";
import {SettingItem} from "./settingItem.js";

const backButton = document.getElementById("settingsBackButton");
const sendButton = document.getElementById("settingsSendButton");

const gameWindInputs = document.getElementsByName("gameWind");
const playerWindInputs = document.getElementsByName("playerWind");
const playerFlowersInputs = document.getElementsByName("playerFlowers");
const playerSeasonsInputs = document.getElementsByName("playerSeasons");
const otherSettingsSection = document.getElementById("otherSettings");

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
    HTMLOtherSettingsContainer;

    otherSettings;

    constructor(gameWindFormName,playerWindInputs,playerFlowersInputs,playerSeasonsInputs,HTMLOtherSettingsContainer) {
        this.HTMLGameWindInputs=gameWindFormName;
        this.HTMLPlayerWindInputs=playerWindInputs;
        this.HTMLPlayerFlowersInputs=playerFlowersInputs;
        this.HTMLPlayerSeasonsInputs=playerSeasonsInputs;
        this.HTMLOtherSettingsContainer = HTMLOtherSettingsContainer;
        this.otherSettings = []
        this.#init();
        this.#initSettings();
    }

    #init(){
        let savedData = localStorage.getItem(storageConfig.settings);
        if(savedData != null) {
            let settings = JSON.parse(savedData);

            //winds
            for (let input of this.HTMLGameWindInputs) {
                input.checked=settings.gameWind.name == this._getType(input.id);
            }
            for (let input of this.HTMLPlayerWindInputs) {
                input.checked=settings.playerWind.name == this._getType(input.id);
            }

            //bonuses
            for (let input of this.HTMLPlayerFlowersInputs) {
                input.checked = false;
                let value = this._getType(input.id);

                for (let flower of settings.playerFlowers) {
                    if(flower.value == value){
                        input.checked = true;
                    }
                }
            }

            for (let input of this.HTMLPlayerSeasonsInputs) {
                input.checked = false;
                let value = this._getType(input.id);

                for (let season of settings.playerSeasons) {
                    if(season.value == value){
                        input.checked = true;
                    }
                }
            }

        }
    }



    #initSettings(){
        this.buildOtherSetting("mahjongDonne","Mahjong sur la donne");
        this.buildOtherSetting("mahjongMur","Mahjong avec les tuiles du mur");
    }

    buildOtherSetting(key,text, checked = false){
        let setting = new SettingItem(key, text, checked);
        this.otherSettings.push(setting);
        setting.buildObject(this.HTMLOtherSettingsContainer);
    }

    #getOtherSettingList(){
        let list = [];
        for (let setting of this.otherSettings) {
            list.push(setting.toJSONObject());
        }
        return list;
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
                return 4;
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
        localStorage.setItem(storageConfig.settings,JSON.stringify(this.toJSONObject()));
    }



    isSet(){
        return this.HTMLGameWindInputs!=null && this.HTMLPlayerWindInputs!=null && this.HTMLPlayerSeasonsInputs!=null && this.HTMLPlayerFlowersInputs!=null
    }

    isValid(){
        return this.gameWind!=null && this.playerWind!=null;
    }

    toJSONObject(){
        let data ={};

        data.gameWind = this.gameWindToTile();
        data.playerWind = this.playerWindToTile();
        data.playerFlowers = this.playerFlowersToTiles();
        data.playerSeasons = this.playerSeasonsToTiles();
        data.otherSettings = this.#getOtherSettingList();

        return data;
    }
}


function onSendClick(){
    if(settings.isSet() && settings.isValid()){
        settings.update();
        settings.saveToStorage();
        let hand = JSON.parse(localStorage.getItem(storageConfig.hand));

        if(hand != null){
            let json = settings.toJSONObject()
            json.slotList = hand;

            console.log(json);


            scoreNet.call(JSON.stringify(json)).then(function (message){
                if(message == null){
                    alert("Échec de la connexion avec le serveur")
                    return;
                }
                localStorage.setItem(storageConfig.score, message);
                window.location.href = "score.html";
            })
        }else{
            alert("Impossible d'importer les tuiles");
        }
    }
}

function onBackClick(){
    settings.update();
    settings.saveToStorage();
    window.location.href = "calculator.html";
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
    settings = new gameSettings(gameWindInputs,playerWindInputs,playerFlowersInputs,playerSeasonsInputs,otherSettingsSection);


    document.addEventListener("mouseup",settings.update.bind(settings));
    backButton.addEventListener("click", onBackClick);
    sendButton.addEventListener("click", onSendClick);
}
