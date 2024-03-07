export class SettingItem{
    key;
    text;
    #checkedDefaultValue;

    #htmlCheckbox;
    #htmlLabel;

    constructor(key, text, checked = false) {
        this.key = key;
        this.text = text;
        this.checkedDefaultValue = checked;
    }

    getHtmlObject(){
        this.#htmlCheckbox = document.createElement("input");
        this.#htmlCheckbox.setAttribute("type","checkbox");
        this.#htmlCheckbox.setAttribute("id",this.key);
        if(this.checkedDefaultValue) this.#htmlCheckbox.setAttribute("checked", "true");

        this.#htmlLabel = document.createElement("label");
        this.#htmlLabel.setAttribute("for",this.key);
        this.#htmlLabel.textContent = this.text;
        let div = document.createElement("div");
        div.appendChild(this.#htmlCheckbox);
        div.appendChild(this.#htmlLabel);

        return div;
    }

    buildObject(parent){
        parent.appendChild(this.getHtmlObject());
    }

    isChecked(){
        return this.#htmlCheckbox.checked;
    }

    toJSONObject(){
        let data ={};
        data.key = this.key;
        data.value = this.isChecked();
        return data
    }
}