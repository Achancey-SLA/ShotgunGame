package org.example.shotgungame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameController {
    public Button startButton;
    String name;
    public TextField nameField;
    InputStream inStream;
    CommunicationConnection gameConnection;
    ObjectInputStream myObjInput;
    public void initialize() throws Exception {
        // Client MUST create InputStream BEFORE OutputStream!!!!!!
    }

    void messagesIn(){
        while(true){
            try {
                System.out.println(myObjInput.readObject());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public void pressStart(){
        name = nameField.getText();
        try {
            Socket ourSocket = new Socket("127.0.0.1", 12345);
            ObjectOutputStream myObjOutput = new ObjectOutputStream(ourSocket.getOutputStream());
            myObjInput = new ObjectInputStream(ourSocket.getInputStream());
            gameConnection = new CommunicationConnection(name, ourSocket, myObjInput, myObjOutput);

            Thread communicationInThread = new Thread(this::messagesIn);
            communicationInThread.start();
            Message startMessage = new Message(1, "hi", name);
            gameConnection.getOutStream().writeObject(startMessage);
            gameConnection.getOutStream().flush();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

}
