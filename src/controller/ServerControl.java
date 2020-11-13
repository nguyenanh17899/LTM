/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Request;
import model.User;

/**
 *
 * @author Nguyen Ngoc Anh
 */
public class ServerControl {
    private Connection con;
    private ServerSocket myServer = null;
    private Socket clientSocket;
    private int serverPort;
    private boolean opened;
    public ServerControl(int serverPort) throws IOException {
        getDBConnection("chatonline", "root", "");
        System.out.println("tao con thành cong");
        opened= openServer(serverPort);
        System.out.println("tao server socket thanh conh");
        while(true){
            listening();
        }
    }

    public boolean isOpened() {
        return opened;
    }
    
    private void getDBConnection(String dbName, String username, String password){
        String dbUrl = "jdbc:mysql://localhost:3307/" + dbName;
        String dbClass = "com.mysql.jdbc.Driver";
        
        try {
            Class.forName(dbClass);
            con = DriverManager.getConnection(dbUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean openServer(int serverPort) throws IOException {

        myServer = new ServerSocket(serverPort);
        if(myServer == null){
            return false;
        }
        else{
             return true;
        }
  
    }

    private void listening() {
        try {
            clientSocket = myServer.accept();
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            Object o = ois.readObject();
            if(o instanceof Request){
                Request req = (Request)o;
            
                if(req.getNum()==1){
                    User s = (User)req.getStd();
                    if(checkUser(s)){
                        User u = getUserByUsername(s.getUsername());
                        setListFriend(u);
                        updateSTT(u);
                        oos.writeObject(u);
                    }
                    else
                        oos.writeObject("false");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkUser(User s) {
         String query = "Select * FROM users WHERE username ='"+s.getUsername()+"'AND password ='"+ s.getPassword()+"'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private User getUserByUsername(String uname){
        User u = new User();
        String sql = "SELECT * FROM users where username='"+uname+"'";
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getInt("role"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }
    public void setListFriend(User u){
        String sql = "SELECT * FROM users,friends WHERE id_user = '"+u.getId()+"' AND id_friend = users.id ";
        Statement st;
        ArrayList<User> listF = new ArrayList<User>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                User fr = new User();
                fr.setId(rs.getInt("id"));
                fr.setUsername(rs.getString("username"));
                fr.setPassword(rs.getString("password"));
                fr.setRole(rs.getInt("role"));
                fr.setStt(rs.getString("stt"));
                listF.add(fr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.setListF(listF);
        
    }
    private boolean updateSTT(User u){
        String sql = "UPDATE users SET stt = 'active' WHERE id ='"+u.getId()+"'";
        Statement st;
        
        try {
            st = con.createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
