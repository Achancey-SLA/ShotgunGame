package org.example.shotgungame;

import java.io.IOException;
import java.util.ArrayList;

public class CommunicationOut implements Runnable {
    public void run() {
        // GET
        while (!Thread.currentThread().isInterrupted()) {
            Message message = Server.theQueue.get();
            while (message == null) {
                message = Server.theQueue.get();
            }
            ArrayList<CommunicationConnection> connectionsToDisconnect = new ArrayList<>();
            // WRITE TO SOCKET
            for (CommunicationConnection eachConnection : Server.allConnections) {
                try {
                    // check if message.to matches socket's name
                    if (message.to.equalsIgnoreCase("ALL") || message.to.equalsIgnoreCase(eachConnection.getName())) {
                        eachConnection.getOutStream().writeObject(message);
                        eachConnection.getOutStream().flush();
                        System.out.println("CommunicationOut  to: " + eachConnection.getName() + ": " + message);
                    }
                    if (message.to.equalsIgnoreCase(eachConnection.getName()) && message.getMode() == 3){
                        connectionsToDisconnect.add(eachConnection);
                    }
                } catch (IOException e) {
                    System.out.println("CommunicationOut failed connection to: " + eachConnection.getName() + ": " + e);
                }
            }
            for (CommunicationConnection disconnectedConnection : connectionsToDisconnect) {
                Server.allConnections.remove(disconnectedConnection);
            }
        }
    }
}