package org.example.shotgungame;

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
            Message newMessage = null;
            try {
                newMessage = (Message)myConnection.getInStream().readObject();
            } catch (Exception ex) {
                System.out.println("CommunicationIn failed connection with:" + myConnection.getName() + ": " + ex);
                System.out.println("Automatically stopped connection due to error");
                try {
                    newMessage = new Message(3,"stop");
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }

            if (newMessage != null) {
                if (!isServer) {
                    System.out.println("CommunicationIn: " + newMessage);
                    continue;
                }
                System.out.println("CommunicationIn from: " + myConnection.getName() + ": " + newMessage);
                if (newMessage.mode == 1) {
                    // START
                    // associate FROM name with its socket
                    myConnection.setName(newMessage.from);
                    try {
                        newMessage = new Message(1,"Welcome: " + newMessage.from);
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                } else if (newMessage.mode == 2) {
                    // COMMUNICATE
                    newMessage = newMessage;
                } else if (newMessage.mode == 3) {
                    // STOP
                    try {
                        newMessage = new Message(1,"Goodbye: " + newMessage.from);
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    stayConnected = false;
                }
                boolean putSuccess  = Server.theQueue.put(newMessage);
                while (!putSuccess) {
                    putSuccess  = Server.theQueue.put(newMessage);
                }
            }
        }

        System.out.println("CommunicationIn bye: " + myConnection.getName());
    }
}
