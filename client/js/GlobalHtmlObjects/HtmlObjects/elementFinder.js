export function findChildById(element, id){
    for (const child of element.children) {
        if (child.id == id) return child;
    }
    console.error(id + " does not exist into " + element);
}

export function findChildByClass(element, className){
    for (const child of element.children) {
        if (child.className == className) return child;
    }
    console.error(className + " does not exist into " + element);
}