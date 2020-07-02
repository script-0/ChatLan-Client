/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Isaac
 */
public class LoginController implements Initializable {
    @FXML
    private JFXTextField ip;

    @FXML
    private JFXTextField port;
    
    @FXML
    private JFXTextField username;

    @FXML
    private JFXButton connect;

    @FXML
    private JFXSpinner spinner;
    
    Socket s;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spinner.setVisible(false);
    }
    
    InetAddress address;
    ObjectOutputStream out;
    
    public Stage getStage(ActionEvent e){
        return (Stage)((JFXButton)e.getSource()).getScene().getWindow();
    }
    
    @FXML
    void connect(ActionEvent e) {
         try {
                spinner.setVisible(true);
                connect.setText("connecting");
                connect.setDisable(true);
                 address = InetAddress.getByName(ip.getText().replace(" ",""));
                
                if(address.isReachable(2000)){
                     System.out.println("Server reachable");
                 }
                s = new Socket(address,Integer.valueOf(port.getText()));
                out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(username.getText());
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/MessagesView.fxml"));
                Parent root = loader.load();
                MessagesViewController con = loader.getController();
                con.setParameters(s, username.getText(),out);
                Scene scene = new Scene(root);

                getStage(e).setScene(scene);
                
         } catch (IOException ex) {
             //When connection failed
             Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
