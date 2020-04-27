/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jfoenix.controls.JFXButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Isaac
 */
public class ClientViewController implements Initializable {
    
     @FXML
    public VBox messages;

    @FXML
    public Label title;

    @FXML
    public TextField textField;

    @FXML
    public JFXButton send;

    public static VBox box;
    
     BufferedReader in;
     PrintWriter out;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            send.setDisable(newValue.isEmpty());
        });
        box = messages;
         try {
                Socket s = new Socket("localhost",4321);             
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(),true);
               title.setText(in.readLine());
               // messages.getChildren().add(new Label(in.readLine()));
                
                ReadStream service = new ReadStream(s,messages,in,this);   
                service.setOnCancelled((event) -> {
                    service.cancel();
                });
                
                service.setOnFailed((event) -> {
                    service.cancel();
                });
                
                service.setOnSucceeded((event) -> {
                    service.cancel();
                });
                
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                executorService.execute(service);
                executorService.shutdown();
         } catch (IOException ex) {
             Logger.getLogger(ClientViewController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }    
    
    public void addText(String t){
         messages.getChildren().add(new Label(t));
    }
    
     @FXML
    void send() {
        messages.getChildren().add(new Label(textField.getText()));
        out.println(textField.getText());
        textField.clear();
    }
}
