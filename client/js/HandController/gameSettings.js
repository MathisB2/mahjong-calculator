import {NetworkController} from "../NetworkController/NetworkController.js";
import {storageConfig, networkConfig} from "../config.js";
import {Tile} from "./hand/Tile.js";
import {SettingItem} from "./settingItem.js";
import {BonusTile} from "./hand/BonusTile.js";
import {tileDirection, tileType} from "./hand/tileDirection.js";

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
    HTMLGameWindInputs;
    HTMLPlayerWindInputs;
    HTMLPlayerFlowersInputs;
    HTMLPlayerSeasonsInputs;
    HTMLOtherSettingsContainer;

    settings;
    #savedSettings

    constructor(gameWindFormName,playerWindInputs,playerFlowersInputs,playerSeasonsInputs,HTMLOtherSettingsContainer) {
        this.HTMLGameWindInputs=gameWindFormName;
        this.HTMLPlayerWindInputs=playerWindInputs;
        this.HTMLPlayerFlowersInputs=playerFlowersInputs;
        this.HTMLPlayerSeasonsInputs=playerSeasonsInputs;
        this.HTMLOtherSettingsContainer = HTMLOtherSettingsContainer;
        this.settings = []
        this.#savedSettings = [];
        this.loadFromStorage();
        this.#initSettings();
    }

    loadFromStorage(){
        let savedData = localStorage.getItem(storageConfig.settings);
        if(savedData != null) {
            let settings = JSON.parse(savedData);

            //winds
            for (let input of this.HTMLGameWindInputs) {
                input.checked = "game"+settings.gameWind.direction == input.id;
            }
            for (let input of this.HTMLPlayerWindInputs) {
                input.checked = "player"+settings.playerWind.direction == input.id;
            }
            for (let input of this.HTMLPlayerFlowersInputs) {
                input.checked = false;
                for (let flower of settings.playerFlowers) {
                    if("flower"+flower.direction == input.id){
                        input.checked = true;
                    }
                }
            }
            for (let input of this.HTMLPlayerSeasonsInputs) {
                input.checked = false;
                for (let season of settings.playerSeasons) {
                    if("season"+season.direction == input.id){
                        input.checked = true;
                    }
                }
            }
        }
    }



    #initSettings(){
        this.buildSetting("dealHand","Mahjong sur la donne");
        this.buildSetting("wallHand","Mahjong avec les tuiles du mur");
    }



    buildSetting(key, text){
        let setting = new SettingItem(key, text, this.#savedSettings.includes(key));
        this.settings.push(setting);
        setting.buildObject(this.HTMLOtherSettingsContainer);
    }

    #getSettingsList(){
        let list = [];
        for (let setting of this.settings) {
            if(!setting.isChecked()) continue;
            list.push(setting.toJSONObject());
        }
        return list;
    }

    getGameWind(){
        let checked = null;
        for (let element of this.HTMLGameWindInputs) {
            if (element.checked) checked = element.id;
        }

        let direction = tileDirection.default;
        if(checked != null)  direction = checked.replace("game","").toLowerCase();

        let tile = new BonusTile(tileType.wind, direction);
        return tile;
    }

    getPlayerWind(){
        let checked = null;
        for (let element of this.HTMLPlayerWindInputs) {
            if (element.checked) checked = element.id;
        }
        let direction = tileDirection.default;
        if(checked != null)  direction = checked.replace("player","").toLowerCase();

        let tile = new BonusTile(tileType.wind, direction);
        return tile;
    }

    getPlayerFlowers(){
        let flowers = [];
        for (let element of this.HTMLPlayerFlowersInputs) {
            if (element.checked)
                flowers.push(new BonusTile(tileType.flower, element.id.replace("flower","").toLowerCase()));
        }
        return flowers;
    }

    getPlayerSeasons(){
        let seasons = [];
        for (let element of this.HTMLPlayerSeasonsInputs) {
            if (element.checked)
                seasons.push(new BonusTile(tileType.season, element.id.replace("season","").toLowerCase()));
        }
        return seasons;
    }



    saveToStorage(){
        localStorage.setItem(storageConfig.settings,JSON.stringify(this.toJSONObject()));
    }

    isValid(){
        return this.HTMLGameWindInputs!=null && this.HTMLPlayerWindInputs!=null && this.HTMLPlayerSeasonsInputs!=null && this.HTMLPlayerFlowersInputs!=null
    }


    toJSONObject(){
        let data ={};

        data.gameWind = this.getGameWind();
        data.playerWind = this.getPlayerWind();
        data.playerFlowers = this.getPlayerFlowers();
        data.playerSeasons = this.getPlayerSeasons();
        data.settings = this.#getSettingsList();

        return data;
    }
}


function onSendClick(){
    if(settings.isValid()){
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

    backButton.addEventListener("click", onBackClick);
    sendButton.addEventListener("click", onSendClick);
}
