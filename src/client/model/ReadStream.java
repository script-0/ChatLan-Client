/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

import client.MessagesViewController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author Isaac
 */
public class ReadStream extends Task<Void>{
    public Socket s ;
    public ObjectInputStream in ;
    public Object tmp;
    public MessagesViewController con;
    
    public ReadStream(Socket s, MessagesViewController con) {
        try {
            this.s = s;
            this.con = con; 
            in = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
    
    public void updateView(Object t){
        if(t instanceof Message ){
            Message message = (Message) t;
            
            Platform.runLater(() -> {
                con.addMessage(message.getEmetteur(),
                               message.getContent(),
                               message.getDate());
            });
            
            /*if(message.getType().equalsIgnoreCase("NOTIFICATION")){//Cas d'une notification
                Platform.runLater(() -> {
                    con.addNotification(message.getContent(),message.getDate());
                });
            }else{
                if(message.getType().equalsIgnoreCase("USER")){//Cas d'un User
                    Platform.runLater(() -> {
                        con.addUser(message.getContent());
                    });
                }else{//Cas d'un message
                    Platform.runLater(() -> {
                         con.addMessage(message.getEmetteur(),
                                        message.getContent(),
                                        message.getDate());
                    });
                }
            }*/
        }else if (t instanceof MyFile ){
            MyFile file = (MyFile) t;
            Platform.runLater(() -> {
                         con.addFile(file,
                                     file.getEmetteur(), 
                                     file.getDate());
                    });
        }else if(t instanceof String){
            String ss = (String)t;
            String[] part = ss.split(":",2);
            if(part[0].equalsIgnoreCase("n")){
                Platform.runLater(() -> {
                    con.addNotification(part[1]+" est connecté",Message.getTime());
                    con.addUser(part[1]);
                });
            }else{
                Platform.runLater(() -> {
                        con.addUser(part[1]);
                    });
            }
        }
    }
    
    @Override
    protected Void call(){
            while(!s.isClosed()){
                try {
                    tmp = in.readObject();
                    
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
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return null;
    }
    
}
