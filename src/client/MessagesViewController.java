/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Isaac
 */
public class MessagesViewController implements Initializable {
        
     @FXML
    private ScrollPane messagesPane;

    @FXML
    private VBox userBox;

    @FXML
    private Label user;

    @FXML
    private VBox messagesBox;

    @FXML
    private Label title;

    @FXML
    private Label date;

    @FXML
    private TextField message;

    @FXML
    private ImageView send;

    @FXML
    private ImageView attach;
    
    
    
    ReadStream service;
    
    ExecutorService executorService;
    
    InetAddress address;
    Socket s;
    
    BufferedReader in;
    PrintWriter out;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       send.setDisable(true);
       message.textProperty().addListener((observable, oldValue, newValue) -> {
            send.setDisable(newValue.isEmpty());
        });
     /*  messagesPane.widthProperty().addListener((observable, oldValue, newValue) -> {
          messagesBox.setPrefWidth(newValue.doubleValue()+200); 
       });
       
       messagesPane.heightProperty().addListener((observable, oldValue, newValue) -> {
          messagesBox.setPrefHeight(newValue.doubleValue()-50); 
       });*/
    }    
    
    public void setParameters(Socket s, String user,String title, BufferedReader b){
        try {
            this.s =s;
            this.title.setText(user+" vous etes connecté");
            this.user.setText(user);
            System.out.println(title);
            this.date.setText(getTime());
            
            in = b;//new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(),true);
            
           service = new ReadStream(s,in,this);
            
            service.setOnCancelled((event) -> {
                service.cancel();
            });
            
            service.setOnFailed((event) -> {
                service.cancel();
            });
            
            service.setOnSucceeded((event) -> {
                service.cancel();
            });
            
            executorService = Executors.newFixedThreadPool(1);
            executorService.execute(service);
            executorService.shutdown();
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    void disconnect(){
         /*
              service.s.close();
              service.s.shutdownInput();
              service.s.shutdownOutput();
*/
    }
    
     public void addMessage(String u, String t){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ReceivedMessage.fxml"));
            Parent root = loader.load();
            ReceivedMessageController con = loader.getController();
            con.setText(u,t,getTime());
            messagesBox.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
      public void addUser(String u){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/NewClient.fxml"));
            Parent root = loader.load();
            NewClientController con = loader.getController();
            con.setText(u);
            userBox.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addNotification(String t){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/Notification.fxml"));
            HBox root = (HBox)loader.load();
            NotificationController con = loader.getController();
            con.setText(t,getTime());
            messagesBox.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    Calendar c = GregorianCalendar.getInstance();
    
    public String getTime(){
         c.setTime(new Date());
         return ""+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }
    
     @FXML
    void send() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/SendedMessage.fxml"));
            HBox root = (HBox)loader.load();
            SendedMessageController con = loader.getController();
            con.setText(message.getText(),getTime());
            messagesBox.getChildren().add(root);
            messagesBox.autosize();
            out.println(message.getText());
            message.clear();
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
