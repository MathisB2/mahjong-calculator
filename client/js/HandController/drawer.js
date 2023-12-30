import {startHeader} from "../GlobalHtmlObjects/Header/header.js";

const drawer = document.querySelector("#drawer");
const drawerHandle = document.querySelector("#drawerHandle");
const drawerHeader = document.querySelector("#drawerHeader");
const drawerTileList = document.querySelector("#drawerTileList");
const drawerButton = document.querySelector(".drawerButton");

let nav=document.querySelector("nav");
const hand=document.querySelector("#hand");

const maxDrawerHeight = 80; // en %
const minDrawerHeight=20;

let isDragging=false;
let isOpened=false;
let startY;

export function openDrawer(){
    if(isOpened)return;
    drawer.style.transition=".2s ease-out"
    setDrawerHeight(.5*window.innerHeight);
    isOpened=true;
}

function closeDrawer(){
    drawer.style.transition=".2s ease-out"
    setDrawerHeight(getMinDrawerHeight());
    isOpened=false;
}

function getTileListHeight(currentDrawerHeight=drawer.offsetHeight){
    return currentDrawerHeight-(drawerHandle.offsetHeight+drawerHeader.offsetHeight+drawerButton.offsetHeight)
}

function getHandHeight(currentDrawerHeight=drawer.offsetHeight){
    return window.innerHeight-nav.offsetHeight-currentDrawerHeight;
}


function getMinDrawerHeight(){
    return drawerButton.offsetHeight+drawerHeader.offsetHeight+drawerHandle.offsetHeight;
}

function setDrawerHeight(h){
    drawer.style.removeProperty("transition")
    hand.style.height=`${getHandHeight(h)}px`;
    drawerTileList.style.height=`${getTileListHeight(h)}px`;

    if(!isDragging){
        drawer.style.transition=".2s ease-out"
    }
    drawer.style.height = `${h}px`;

}

function roundDrawerHeight(h){
    if(h<getMinDrawerHeight()*(1+(minDrawerHeight/100))){
        closeDrawer()
    }else{
        if(h<0.65*window.innerHeight){
            setDrawerHeight(.5*window.innerHeight);
        }else{
            setDrawerHeight(maxDrawerHeight/100*window.innerHeight);

        }

    }

}

function dragStart(){
    isDragging = true;
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
        }
    }
}

function dragStop(){
    isDragging = false;
    roundDrawerHeight(drawer.offsetHeight);
}

export async function startDrawer(){
    nav=document.querySelector("nav");
    if(!(drawerHandle
        && drawer
        && drawerTileList
        && drawerButton
        && drawerHeader
        && nav
        && hand)) return;


    drawerHandle.addEventListener("mousedown", dragStart);
    drawerHeader.addEventListener("mousedown", dragStart);
    document.addEventListener("mousemove", dragging);
    document.addEventListener("mouseup", dragStop);

    drawerHandle.addEventListener("touchstart", dragStart);
    drawerHeader.addEventListener("touchstart", dragStart);
    document.addEventListener("touchmove", dragging);
    document.addEventListener("touchend", dragStop);




    closeDrawer();
}