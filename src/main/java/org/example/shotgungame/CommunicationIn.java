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
        boolean stayConnected = true;
        while (stayConnected && !Thread.currentThread().isInterrupted()) {
            Message inMessage = null;
            Message outMessage = null;
            try {
                inMessage = (Message)myConnection.getInStream().readObject();
                myConnection.setName(inMessage.from);
                System.out.println("got message"+inMessage);
            } catch (Exception ex) {
                System.out.println("CommunicationIn failed connection with:" + myConnection.getName() + ": " + ex);
                System.out.println("Automatically stopped connection due to error");
                Server.allConnections.remove(myConnection);
                stayConnected = false;
                break;
            }

            if(inMessage.mode == 1){
                try {
                    outMessage = new Message(1,"added to queue");
                    Server.queuedPlayers.add(myConnection);
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                myConnection.getOutStream().writeObject(outMessage);
                myConnection.getOutStream().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        System.out.println("CommunicationIn bye: " + myConnection.getName());
    }
}
