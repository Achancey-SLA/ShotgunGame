package org.example.shotgungame;

public class Room implements Runnable {
    CommunicationConnection connection1;
    CommunicationConnection connection2;


    public Room(CommunicationConnection connection1, CommunicationConnection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }

    public void run(){
        /*
        connection1.selection = 0;
        connection2.selection = 0;

         */
        System.out.println("room");
        try {
            this.connection1.sendMessage(new Message(2, connection2.getName()));
            this.connection2.sendMessage(new Message(2, connection1.getName()));
        }
        catch (Exception e){}



        while(true){
            System.out.println("room looking for stuff");
            while(connection1.selection==0||connection2.selection==0){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            System.out.println("recieved both message about an action");
            System.out.println(connection1.selection+" 2: "+connection2.selection);

            connection1.selection = 0;
            connection2.selection = 0;
        }
    }
}
