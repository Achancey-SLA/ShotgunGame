package org.example.shotgungame;

public class Room implements Runnable {
    CommunicationConnection connection1;
    CommunicationConnection connection2;


    public Room(CommunicationConnection connection1, CommunicationConnection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }

    public void run(){
        System.out.println("room");
        try {
            this.connection1.sendMessage(new Message(2, "Joined Room"));
            this.connection2.sendMessage(new Message(2, "Joined Room"));
        }
        catch (Exception e){}

    }

}
