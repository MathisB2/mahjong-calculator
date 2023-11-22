const drawer = document.querySelector("#drawer");
const drawerHandle = document.querySelector("#drawerHandle");
const drawerHeader = document.querySelector("#drawerHeader");
const drawerTileList = document.querySelector("#drawerTileList");
const drawerButton = document.querySelector("#drawerValidateButton");

const nav=document.querySelector("nav");
const hand=document.querySelector("#hand");

const maxDrawerHeight=80 //80%

let isDragging=false;
let startY;


console.log(getTileListHeight());


setDrawerHeight(getMinDrawerHeight());


function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
}


function getTileListHeight(){
    return drawer.offsetHeight-(drawerHandle.offsetHeight+drawerHeader.offsetHeight+drawerButton.offsetHeight)
}

function getHandHeight(){
    return window.innerHeight-(nav.offsetHeight+drawer.offsetHeight);
}


function getMinDrawerHeight(){
    return drawerButton.offsetHeight+drawerHeader.offsetHeight+drawerHandle.offsetHeight;
}



function setDrawerHeight(h){
    drawer.style.height = `${h}px`;
    hand.style.height=`${getHandHeight()}px`
    drawerTileList.style.height=`${getTileListHeight()}px`
}


function roundDrawerHeight(h){
    if(h<256){
        setDrawerHeight(getMinDrawerHeight());
    }else{
        if(h<0.65*window.innerHeight){
            setDrawerHeight(.5*window.innerHeight);
        }else{
            setDrawerHeight(maxDrawerHeight/100*window.innerHeight);

    }

    }

}


function dragStart(e){
    drawer.style.removeProperty("transition")
    isDragging=true;
}


function dragging(e){
    let newHeight;
    let newRelativeHeight;
    if (isDragging) {
        startY = e.pageY || e.touches?.[0].pageY;
        newHeight = window.innerHeight -startY;
        newRelativeHeight = 100-(startY / window.innerHeight * 100);

        if(newRelativeHeight<maxDrawerHeight && newHeight>getMinDrawerHeight()){
            setDrawerHeight(newHeight);
            console.log(window.innerHeight-startY);
        }


    }

}





function dragStop(e){
    isDragging=false
    drawer.style.transition=".2s ease-out"
    roundDrawerHeight(drawer.offsetHeight);
}


drawerHandle.addEventListener("mousedown", dragStart);
document.addEventListener("mousemove", dragging);
document.addEventListener("mouseup", dragStop);

drawerHandle.addEventListener("touchstart", dragStart);
document.addEventListener("touchmove", dragging);
document.addEventListener("touchend", dragStop);
