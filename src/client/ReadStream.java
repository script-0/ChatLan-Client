/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;

/**
 *
 * @author Isaac
 */
public class ReadStream extends Task<Void>{
    public Socket s ;
    public BufferedReader in ;
    public String tmp;
    public MessagesViewController con;
    
    public ReadStream(Socket s, BufferedReader in, MessagesViewController con) {
        this.s = s;
        this.in = in;
        this.con = con;
    }      
    
    public void updateView(String t){
        String[] tmp2 = t.split(":",2);
        if(tmp2[0].equals("n")){//Cas d'une notification
            Platform.runLater(() -> {
                con.addNotification(tmp2[1].replace(":",""));
            });
            
            String[] tmp3 = tmp2[1].split(":",2);
            Platform.runLater(() -> {
                con.addUser(tmp3[0]);
            });
                    
        }else{
         String[] tmp3 = tmp2[1].split(":",2);
         Platform.runLater(() -> {
                         con.addMessage(tmp3[0],tmp3[1]);
                    });
        }
    }
    
    @Override
    protected Void call(){
            while(!s.isClosed()){
                try {
                    tmp = in.readLine();
                    System.out.println("Recu:"+tmp);

                    updateView(tmp);

                } catch (IOException ex) {
                    Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        s.close();                        
                    } catch (IOException ex1) {
                        Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex1);
                    }finally{
                        this.cancel();
                    }
                }
            }
        return null;
    }
    
}
