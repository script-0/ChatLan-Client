/*
 * @Isaac
 */
package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Isaac
 */
public class Client extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/client/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
         stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
