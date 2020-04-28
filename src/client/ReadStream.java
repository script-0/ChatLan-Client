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
    public ClientViewController con;
    
    public ReadStream(Socket s, BufferedReader in, ClientViewController con) {
        this.s = s;
        this.in = in;
        this.con = con;
    }      
    
    @Override
    protected Void call(){
            while(!s.isClosed()){
                try {
                    tmp = in.readLine();
                    System.out.println("Recu:"+tmp);

                     Platform.runLater(() -> {
                         con.addText("Recu:"+tmp);
                    });

                } catch (IOException ex) {
                    Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        s.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ReadStream.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        return null;
    }
    
}
