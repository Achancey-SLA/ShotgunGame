package org.example.shotgungame;

import java.io.IOException;
import java.net.UnknownHostException;

public class CommunicationIn implements Runnable {
    CommunicationConnection myConnection;
    boolean isServer;

    public CommunicationIn(CommunicationConnection connection, boolean isServer) {
        this.myConnection = connection;
        this.isServer = isServer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Message inMessage = null;
            Message outMessage = null;
            try {
                System.out.println("waiting for message from: "+ myConnection.getName());
                inMessage = (Message)myConnection.getInStream().readObject();
                myConnection.setName(inMessage.from);
                System.out.println("got message"+inMessage);
            } catch (Exception ex) {
                System.out.println("CommunicationIn failed connection with:" + myConnection.getName() + ": " + ex);
                System.out.println("Automatically stopped connection due to error");
                Server.allConnections.remove(myConnection);
                break;
            }

            if(inMessage.mode == 1){
                try {
                    outMessage = new Message(1,"added to queue");
                    Server.queuedPlayers.add(myConnection);
                    myConnection.getOutStream().writeObject(outMessage);
                    myConnection.getOutStream().flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if(3<inMessage.mode&&inMessage.mode<7) {
                myConnection.selection = inMessage.getMode();
                System.out.println("set connection selection to "+myConnection.selection);
            }
        }

        System.out.println("finished connecting: " + myConnection.getName());
    }
}
