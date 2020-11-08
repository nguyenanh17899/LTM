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
import java.sql.Statement;
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
                        oos.writeObject("ok");
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
         String query = "Select * FROM tbluser WHERE username ='"+s.getUsername()+"'AND password ='"+ s.getPassword()+"'";
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
    
    
    
}
