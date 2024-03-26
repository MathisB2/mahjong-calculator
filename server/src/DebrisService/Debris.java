package DebrisService;

import DataServices.UserData;

public class Debris<T> {
    private T data;
    private long clockCreated;
    private long lifeTimeMillis;

    public Debris(T data, long lifeTimeMillis) {
        this.data = data;
        this.lifeTimeMillis = lifeTimeMillis;
        this.clockCreated = System.currentTimeMillis();
    }

    public boolean isAlive(){
        return System.currentTimeMillis() - this.clockCreated < this.lifeTimeMillis;
    }

    public T getData() {
        return data;
    }
}
