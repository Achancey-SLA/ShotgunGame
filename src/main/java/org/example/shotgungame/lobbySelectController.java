package org.example.shotgungame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class lobbySelectController {
    public void initialize() throws Exception {
        Socket ourSocket = new Socket("127.0.0.1", 12345);

        // Client MUST create InputStream BEFORE OutputStream!!!!!!
        ObjectOutputStream myObjOutput = new ObjectOutputStream(ourSocket.getOutputStream());
        ObjectInputStream myObjInput = new ObjectInputStream(ourSocket.getInputStream());
        CommunicationConnection newConnection = new CommunicationConnection("Mr. H",ourSocket,myObjInput,myObjOutput);
        CommunicationIn myCommunicationIn = new CommunicationIn(newConnection, false);
        Thread communicationInThread = new Thread(myCommunicationIn);
        communicationInThread.start();
    }
}
