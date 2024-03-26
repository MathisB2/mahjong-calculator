package DataServices;

public class FloatingConnection {
    private UserData data;
    private long clockCreated;
    private long lifeTimeMillis;

    public FloatingConnection(UserData data, long lifeTimeMillis) {
        this.data = data;
        this.clockCreated = System.currentTimeMillis();
        this.lifeTimeMillis = lifeTimeMillis;
    }

    public boolean isAlive(){
        return System.currentTimeMillis() - this.clockCreated < this.lifeTimeMillis;
    }

    public UserData getData() {
        return data;
    }

}
