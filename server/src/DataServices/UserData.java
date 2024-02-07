package DataServices;

import java.net.http.WebSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

public class UserData {
    private WebSocket ipUser;
    private HashMap<Date,Integer> results = new HashMap<Date,Integer>();
    private String username;
    private int id;

    public UserData(String username){
        this.username=username;
        load();
    }
    private void load(){
        try{
            loadId();
            loadScore();
        }catch (Exception e){
            System.out.println(e);
        }

    }
    private void loadId() throws Exception{
        Connection conn = DataService.get().getConn();
        String query="select id FROM USERS WHERE username='"+username+"';";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        rs.next();
        id = rs.getInt("id");
        this.id=id;

    }

    private void loadScore() throws Exception{

        Connection conn = DataService.get().getConn();
        String query="select * FROM Score WHERE id='"+id+"';";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()){
            results.put(rs.getDate("date"),rs.getInt("score"));

        }
    }

}
