export function  cropValue(value, min, max){
    if(isNaN(value)) return max;
    if(value<=min) return min;
    if(value>=max) return max;
    return value;
}

export function mapValue (value, fromMin, fromMax, toMin, toMax){
    return ((value - fromMin) * (toMax - toMin)) / (fromMax - fromMin) + toMin;
}