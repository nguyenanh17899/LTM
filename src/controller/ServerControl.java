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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Ngoc Anh
 */
public class ServerControl {
    private ServerSocket myServer = null;
    private Socket clientSocket;
    private int serverPort;
    private boolean opened;
    public ServerControl(int serverPort) throws IOException {
        opened= openServer(serverPort);
        while(true){
            listening();
        }
    }

    public boolean isOpened() {
        return opened;
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
        } catch (IOException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
