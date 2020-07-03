/*
 * @Isaac
 */
package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Isaac
 */
public class SendedMessageController implements Initializable {

    @FXML
    private Label text;

    @FXML
    private Label date;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setText(String t, String d){
        text.setText(t);
        date.setText(d);
    }
    
}
