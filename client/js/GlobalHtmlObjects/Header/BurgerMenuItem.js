import {HtmlTag} from "../HtmlObjects/HtmlTag.js";

export class BurgerMenuItem{
    text;
    url;
    img;
    activeImageKeyWord;

    constructor(text, url, img) {
        this.text=text;
        this.url=url;
        this.img=img;
        this.activeImageKeyWord="Active"

        this.getActiveImgUrl();
    }

    getHtmlObject(){
        let a = new HtmlTag("a");

        let img = new HtmlTag("img");
        if(this.isActive()){
            img.setAttribute("src",this.getActiveImgUrl());
            a.setAttribute("class","activeLink");
        }else{
            img.setAttribute("src",this.img);
        }
        img.setAttribute("alt","icon");

        let span = new HtmlTag("span");
        span.innerText=this.text;

        a.setAttribute("href",this.url);
        a.addChild(img);
        a.addChild(span);

        let li = new HtmlTag("li");
        li.addChild(a);
        return li;
    }



    isActive(){
        let path = window.location.pathname;
        let page = path.split("/").pop();
        return this.url==page;
    }

    getActiveImgUrl(){
        let ext=this.img.split('.').pop();

        return this.img.slice(0, -(ext.length+1))+this.activeImageKeyWord+"."+ext;
    }

}