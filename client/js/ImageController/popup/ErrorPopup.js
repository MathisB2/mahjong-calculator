import {Popup} from "./Popup.js";

export class ErrorPopup extends Popup{
    constructor(parent, errorMessage) {
        let content = document.createElement("section");
        content.setAttribute("class","popUpOverlay");

        let panel = document.createElement("section");
        panel.setAttribute("class","popUp");

        let errorLabel = document.createElement("h2");
        errorLabel.textContent = errorMessage;

        let closeButton = document.createElement("div");
        closeButton.setAttribute("class", "close-button");
        closeButton.textContent = "×";

        panel.appendChild(closeButton);
        panel.appendChild(errorLabel);
        content.appendChild(panel);
        super(parent, content);

        closeButton.onclick = this.hide.bind(this);
    }
}