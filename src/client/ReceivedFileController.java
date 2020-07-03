/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.model.MyFile;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author Isaac
 */
public class ReceivedFileController implements Initializable {


    public final String FOLDER_NAME = "ChatLan";
    
     @FXML
    private ImageView image;

    @FXML
    private Label user;

    @FXML
    private Label date;

    @FXML
    private Label filename;
    
    File contentFile = null;
    
    MyFile content;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
 
    public void setParameters(MyFile f, String u, String d){
        content =f;
        filename.setText(content.getNameFile());
        user.setText(u);
        date.setText(d);
    }
    
    
    @FXML
    public void openFile(){
        try {
            if(contentFile ==null){
                File tmp =  new File(System.getProperty("user.home")+"/Downloads/POO-Socket-Impl");
                tmp.mkdirs();
                try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/Downloads/"+FOLDER_NAME+"/"+this.content.getNameFile())) {
                    fos.write(content.getContent());
                    fos.flush();
                    contentFile = new File(System.getProperty("user.home")+"/Downloads/"+FOLDER_NAME+"/"+this.content.getNameFile());
                }                
            }
            Desktop.getDesktop().open(contentFile);
        } catch (IOException ex) {
            Logger.getLogger(SendedFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
