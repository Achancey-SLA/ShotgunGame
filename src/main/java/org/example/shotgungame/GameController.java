package org.example.shotgungame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameController {
    public Button startButton;
    public Button shootButton;
    public Button blockButton;
    public Button loadButton;
    String name;
    public TextField nameField;
    InputStream inStream;
    CommunicationConnection gameConnection;
    int bullets;
    public Label bulletText;
    public Label waitingText;
    public Label enemyNameText;
    ObjectInputStream myObjInput;
    public void initialize() throws Exception {
        startButton.setDisable(true);
        bullets = 0;
        bulletText.setText("bullets: "+bullets);
    }

    void messagesIn(){
        Message message = null;
        while(true){
            try {
                message = (Message) myObjInput.readObject();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(message);
            if(message.mode ==1){
                waitingText.setVisible(true);
            }
            if(message.mode == 2){
                waitingText.setVisible(false);
                blockButton.setDisable(false);
                loadButton.setDisable(false);
                final Message m = message;
                Platform.runLater(()->{
                    enemyNameText.setText(m.text);
                });
            }
        }
    }
    public void shoot() throws Exception{
        gameConnection.sendMessage(new Message(4,"",name));
    }
    public void block() throws Exception {
        Message blockMessage = new Message(6,"e",name);
        gameConnection.sendMessage(blockMessage);
    }
    public void load() throws Exception {
        gameConnection.sendMessage(new Message(5,"",name));
    }
    public void nameTyped(){
        startButton.setDisable(false);
    }
    public void pressStart(){
        name = nameField.getText();
        startButton.setDisable(true);
        startButton.setVisible(false);
        try {
            Socket ourSocket = new Socket("127.0.0.1", 12345);
            ObjectOutputStream myObjOutput = new ObjectOutputStream(ourSocket.getOutputStream());
            myObjInput = new ObjectInputStream(ourSocket.getInputStream());
            gameConnection = new CommunicationConnection(name, ourSocket, myObjInput, myObjOutput);

            Thread communicationInThread = new Thread(this::messagesIn);
            communicationInThread.start();
            Message startMessage = new Message(1, "hi", name);
            gameConnection.sendMessage(startMessage);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

}
