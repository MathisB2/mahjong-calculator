import {Popup} from "./popup.js";
import {HtmlTag} from "../../GlobalHtmlObjects/HtmlObjects/HtmlTag.js";
import {findChildById} from "../../GlobalHtmlObjects/HtmlObjects/elementFinder.js";

export class ResultPopup extends Popup{
    data;

    constructor(overlay, resultData) {
        let panel = findChildById(overlay,"resultPopUp");
        super(overlay,panel);
        this.data = resultData;

        let title = new HtmlTag("h2");
        title.addText("Résultats de la détéction");

        let canvas = new HtmlTag("canvas");
        canvas.addAttribute("id","contourCanvas");

        let resultList = this.data.getResultTableObject();
        resultList.setAttribute("id", "resultList");

        let bottomSection = this.data.getButtonObject();

        this.panel.innerHTML= title.toHtml()+canvas.toHtml()+resultList.toHtml()+bottomSection.toHtml();

        this.drawImages()
    }


    drawImages(){
        this.#drawHeaderImage();
    }

    #drawHeaderImage(){
        let canvas = findChildById(this.panel, "contourCanvas");
        this.data.drawContourImage(canvas);
    }

}


/*
        <section class="popUp" id="resultPopUp">

            <h2>Résultats de la détéction</h2>
            <img src="img/indexBackgroundCover.jpg" alt="">
            <section id="resultList">
                <table>
                    <thead>
                        <tr>
                            <th>N°</th>
                            <th>Img</th>
                            <th></th>
                            <th>Tuile</th>
                            <th>Nom</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">bamboo</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">character1</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">bamboo</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    <tr>
                        <td>4</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">bamboo</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">bamboo</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    <tr>
                        <td>12</td>
                        <td><div class="tileResult"></div></td>
                        <td>→</td>
                        <td><div class="tileResult"></div></td>
                        <td><label for="test">bamboo</label></td>
                        <td><input type="checkbox" name="" id="test"></td>
                    </tr>
                    </tbody>
                </table>

                total : 7/12 tuiles


            </section>
            <section id="resultButton">
                Importer 7 tuiles
            </section>

        </section>
 */