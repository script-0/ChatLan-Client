/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Isaac
 */
public class SendedFileController implements Initializable {

    @FXML
    private Label filename;

    @FXML
    private Label date;
    
    File content;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setParameters(File f,String date){
        this.content=f;
        this.filename.setText(content.getName());
        this.date.setText(date);
    }
    
    @FXML
    public void openFile(){
        try {
            Desktop.getDesktop().open(content);
        } catch (IOException ex) {
            Logger.getLogger(SendedFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
