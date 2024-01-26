import {TileDrawer} from "./tileDrawer.js";
const drawer = document.querySelector("#drawer");

export async function startDrawer(){
    if(!drawer) return;

    const drawerObject = new TileDrawer(drawer);
}