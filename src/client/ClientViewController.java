/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
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
    private VBox messages;

    @FXML
    private Label title;

    @FXML
    private TextField textField;

    @FXML
    private JFXButton send;

    @FXML
    private JFXTextField ip;

    @FXML
    private JFXTextField port;

    @FXML
    private JFXButton connect;

    @FXML
    private JFXSpinner spinner;   
    
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
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            send.setDisable(newValue.isEmpty());
        });
        spinner.setVisible(false);
    }    
    
    public void addText(String t){
         messages.getChildren().add(new Label(t));
    }
    
    @FXML
    void connect() {
         try {
                spinner.setVisible(true);
                connect.setText("connecting");
                connect.setDisable(true);
                 address = InetAddress.getByName(ip.getText().replace(" ",""));
                s = new Socket(address,Integer.valueOf(port.getText()));             
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(),true);
                title.setText(in.readLine());
                
                spinner.setVisible(false);
                connect.setText("Disconnect");
                connect.setDisable(false);
                
                connect.setOnAction((event) -> {
                    disconnect();
                });
                
                service = new ReadStream(s,in,this); 
                
               /* service.setOnCancelled((event) -> {
                    service.cancel();
                });*/
                
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
             spinner.setVisible(false);
             connect.setText("Disconnect");
             connect.setDisable(false);
             Logger.getLogger(ClientViewController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    void disconnect(){
          try {
              service.s.close();
              service.s.shutdownInput();
              service.s.shutdownOutput();
          } catch (IOException ex) {
              System.out.println("Error occured during Disconnecting...");
          }finally{
              service.cancel();
              executorService.shutdownNow();
          }
          
          connect.setText("connect");
                connect.setDisable(false);
                
                connect.setOnAction((event) -> {
                    connect();
                });
    }
    
    
     @FXML
    void send() {
        messages.getChildren().add(new Label(textField.getText()));
        out.println(textField.getText());
        textField.clear();
    }
}
