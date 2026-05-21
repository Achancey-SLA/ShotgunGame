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
    String enemyName;
    public TextField nameField;
    InputStream inStream;
    CommunicationConnection gameConnection;
    int bullets;
    public Label bulletText;
    public Label waitingText;
    public Label enemyNameText;
    public Label infoLabel;
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
                System.out.println("read message from server");
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
            final Message m = message;
            System.out.println(message);
            if(message.mode ==1){
                waitingText.setVisible(true);
            }
            if(message.mode == 2){
                waitingText.setVisible(false);
                blockButton.setDisable(false);
                loadButton.setDisable(false);
                Platform.runLater(()->{
                    enemyNameText.setText(m.text);
                    enemyName = m.text;
                });
            }
            if(message.mode>3&&message.mode<7){
                System.out.println("recieved enemy move");
                Platform.runLater(()->{
                    enableButtons();
                    if(m.mode == 4){
                        infoLabel.setText(enemyName+" shot at you");
                    }
                    if(m.mode == 5){
                        infoLabel.setText(enemyName+" reloaded");
                    }
                    if(m.mode == 6){
                        infoLabel.setText(enemyName+" blocked");
                    }
                });

            }
            if(message.mode==7){
                Platform.runLater(()-> {
                    infoLabel.setText("you win!");
                });
            }
            if(message.mode==8){
                Platform.runLater(()-> {
                    infoLabel.setText("you got shot :(");
                });
            }
            if(message.mode==9){
                Platform.runLater(()-> {
                    infoLabel.setText("it's a tie (both shot)");
                });
            }
        }

    }
    public void enableButtons(){
        blockButton.setDisable(false);
        loadButton.setDisable(false);
        if(bullets>0){
            shootButton.setDisable(false);
        }
    }

    public void disableButtons(){
        blockButton.setDisable(true);
        loadButton.setDisable(true);
        shootButton.setDisable(true);
    }
    public void shoot() throws Exception{
        gameConnection.sendMessage(new Message(4,"shoot",name));
        System.out.println("shoot");
        infoLabel.setText("Waiting for opponent...");
        bullets--;
        bulletText.setText("Bullets: "+bullets);
        disableButtons();
    }
    public void block() throws Exception {
        gameConnection.sendMessage(new Message(6,"block",name));
        System.out.println("block");
        infoLabel.setText("Waiting for opponent...");
        disableButtons();
    }
    public void load() throws Exception {
        gameConnection.sendMessage(new Message(5,"load",name));
        System.out.println("load");
        infoLabel.setText("Waiting for opponent...");
        bullets++;
        bulletText.setText("Bullets: "+bullets);
        disableButtons();
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
