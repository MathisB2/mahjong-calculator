package Signal;

public class Connection {
    private Runnable disconnectFoo;
    Connection(Runnable disconnectFoo){
        this.disconnectFoo = disconnectFoo;
    }

    public void disconnect(){
        this.disconnectFoo.run();
    }
}
