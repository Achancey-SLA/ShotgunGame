package org.example.shotgungame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnector implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket myServerSocket = new ServerSocket(12345);
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Server ready at port: " + myServerSocket.getLocalPort());
                Socket newSocket = myServerSocket.accept();

                ObjectInputStream myObjInput = new ObjectInputStream(newSocket.getInputStream());
                // InputStream MUST be created BEFORE OutputStream!!!!!!
                ObjectOutputStream myObjOutput = new ObjectOutputStream(newSocket.getOutputStream());
                CommunicationConnection newConnection = new CommunicationConnection(null,newSocket,myObjInput,myObjOutput);
                Server.allConnections.add(newConnection);

                CommunicationIn newClient = new CommunicationIn(newConnection, true);
                Thread perClientThread = new Thread(newClient);
                perClientThread.start();
            }
        } catch (IOException ex) {
            System.out.println("ServerConnector broke: " + ex );
        }
    }
}