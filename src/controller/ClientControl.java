/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.Request;

/**
 *
 * @author Admin
 */
public class ClientControl {
    private Socket mySocket;
    private String serverHost;
    private int serverPort;
    
    public ClientControl(){
    }

    public ClientControl(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }
    
    public Socket openConnection(){
        try{
            mySocket = new Socket(serverHost, serverPort);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return mySocket;
    }
    
    public boolean sendData(Request req){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(req);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public Object receiveData(){
        Object rs;
        try {
            ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
            rs = ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return rs;
    }
    
    public boolean closeConnection(){
        try {
            mySocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
