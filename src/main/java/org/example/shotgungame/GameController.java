package org.example.shotgungame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameController {
    public Button startButton;
    public Button shootButton;
    public Button blockButton;
    public Button loadButton;
    String name;
    String enemyName;
    Integer myScore;
    Integer enemyScore;
    public TextField nameField;
    InputStream inStream;
    CommunicationConnection gameConnection;
    int bullets;
    public Label scoreLabel;
    public Label bulletText;
    public Label waitingText;
    public Label enemyNameText;
    public Label infoLabel;
    ObjectInputStream myObjInput;
    public void initialize() throws Exception {
        startButton.setDisable(true);
        bullets = 0;
        bulletText.setText("Bullets: "+bullets+"/6");
        setButtonImage(blockButton,"src/Shield.png");
        setButtonImage(shootButton,"src/Gun.png");
        setButtonImage(loadButton,"src/Load.png");
    }

    void setButtonImage(Button button, String imagePath) throws Exception{
        ImageView myImageView = new ImageView(new Image(new FileInputStream(imagePath)));
        myImageView.setFitWidth(button.getPrefWidth());
        myImageView.setFitHeight(button.getPrefHeight());
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        button.setGraphic(myImageView);
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
                if (message.text.equals("blackMagic")){
                    Platform.runLater(()-> {
                        infoLabel.setText("Enemy Removed (win)");
                    });
                }
                else {
                    Platform.runLater(() -> {
                        infoLabel.setText("You Win!");
                    });
                }
            }
            if(message.mode==8){
                if (message.text.equals("blackMagic")){
                    Platform.runLater(()-> {
                        infoLabel.setText("you lost due to black magic");
                    });
                }
                else {
                    Platform.runLater(() -> {
                        infoLabel.setText("you got shot :(");
                    });
                }
            }
            if(message.mode==9){
                if (message.text.equals("blackMagic")){
                    Platform.runLater(()-> {
                        infoLabel.setText("draw due to black magic");
                    });
                }
                else {
                    Platform.runLater(() -> {
                        infoLabel.setText("draw (both shot)");
                    });
                }
            }
            if(message.mode==11){
                try {
                    setButtonImage(shootButton, "Gun.png");
                }
                catch (Exception e){}
                bullets = 0;
                shootButton.setDisable(true);
                blockButton.setDisable(false);
                loadButton.setDisable(false);
                enemyScore = message.num2;
                myScore = message.num1;
                Platform.runLater(() -> {
                    infoLabel.setText("new round started");
                    bulletText.setText("Bullets: " + bullets+"/6");
                    scoreLabel.setText("Score: " + myScore + "/" + enemyScore);
                });
            }
        }

    }
    public void enableButtons(){
        blockButton.setDisable(false);
        if(bullets<6) {
            loadButton.setDisable(false);
        }
        if(bullets>0){
            shootButton.setDisable(false);
        }
        if(bullets>5){
            try {
                setButtonImage(shootButton, "src/BlackMagic.png");
            }
            catch(Exception e){

            }
        }
    }

    public void disableButtons(){
        blockButton.setDisable(true);
        loadButton.setDisable(true);
        shootButton.setDisable(true);
    }
    public void shoot() throws Exception{
        if(bullets<6) {
            gameConnection.sendMessage(new Message(4, "shoot", name));
            bullets--;
        }
        else{
            gameConnection.sendMessage(new Message(10,"blackMagic",name));
            bullets=0;
        }
        System.out.println("shoot");
        infoLabel.setText("Waiting for opponent...");

        bulletText.setText("Bullets: "+bullets+"/6");
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
        bulletText.setText("Bullets: "+bullets+"/6");
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
