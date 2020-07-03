/*
 *@Isaac
 */
package client;

import client.model.Message;
import client.model.MyFile;
import client.model.ReadStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; 
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    ObjectOutputStream out;
    
    public String username;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       send.setDisable(true);
       message.textProperty().addListener((observable, oldValue, newValue) -> {
            send.setDisable(newValue.isEmpty());
        });
    }    
    
    public void setParameters(Socket s, String user, ObjectOutputStream o){
            this.s =s;
            this.title.setText(user+" vous etes connecté");
            this.user.setText(user);
            username = user;
            System.out.println(title);
            this.date.setText(Message.getTime());
            
            out = o;
            
            
           service = new ReadStream(s,this);
            
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
}

    void disconnect(){
         /*
              service.s.close();
              service.s.shutdownInput();
              service.s.shutdownOutput();
*/
    }
    
    public void addMessage(String u, String t,String time){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ReceivedMessage.fxml"));
            Parent root = loader.load();
            ReceivedMessageController con = loader.getController();
            con.setText(u,t,time);
            messagesBox.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void addFile(MyFile f, String u ,String time){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ReceivedFile.fxml"));
            Parent root = loader.load();
            ReceivedFileController con = loader.getController();
            con.setParameters(f,u,time);
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
    
    public void addNotification(String t,String time){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/Notification.fxml"));
            HBox root = (HBox)loader.load();
            NotificationController con = loader.getController();
            con.setText(t,time);
            messagesBox.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Message createMsg(String content){
        Message msg = new Message(content,username);
        
        return msg;
    }
    
    @FXML
    void send() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/SendedMessage.fxml"));
            HBox root = (HBox)loader.load();
            SendedMessageController con = loader.getController();
            con.setText(message.getText(),  Message.getTime());
            messagesBox.getChildren().add(root);
            messagesBox.autosize();
            
            out.writeObject(createMsg(message.getText()));
            out.flush();
            
            message.clear();
        } catch (IOException ex) {
            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void sendFile(MouseEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File");
        File file = chooser.showOpenDialog((Window)getStage(event));
            if(file!=null && file.isFile()) {
                try {
                    MyFile f =  new MyFile(getContentFile(file),file.getName(),getExtension(file),username);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/SendedFile.fxml"));
                    HBox root = (HBox)loader.load();
                    SendedFileController con = loader.getController();
                    con.setParameters(file,Message.getTime());
                    messagesBox.getChildren().add(root);
                    messagesBox.autosize();
                    
                    Thread t =  new Thread(()->{
                        try {
                            out.writeObject(f);
                            out.flush();
                        } catch (IOException ex) {
                            System.out.println("Error on Thread Wrinting in output stream failed");
                            Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    t.start();
                    
                } catch (IOException ex) {
                    Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
    
    public String getExtension(File file){
        String name = file.getName();
        return name.substring(name.lastIndexOf("."));
    }
    
    
    byte[] getContentFile(File f) throws IOException{
        FileInputStream in = new FileInputStream(f);
        byte b[] = new byte[in.available()];
        in.read(b);
        return b;
    }
    
    public Stage getStage(MouseEvent e){
        Node tmp = (Node)e.getSource();
        return (Stage)tmp.getScene().getWindow();
    }
    
}
