package org.example.shotgungame;

public class Room implements Runnable {
    CommunicationConnection connection1;
    CommunicationConnection connection2;
    Message con1Message;
    Message con2Message;


    public Room(CommunicationConnection connection1, CommunicationConnection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }

    public void run(){
        System.out.println("room");
        try {
            this.connection1.sendMessage(new Message(2, connection2.getName()));
            this.connection2.sendMessage(new Message(2, connection1.getName()));
        }
        catch (Exception e){}

        Thread con1Thread = new Thread(this::con1In);
        Thread con2Thread = new Thread(this::con1In);
        con1Thread.start();
        con2Thread.start();

        while(true){
            while(con1Message==null||con2Message==null){}
            System.out.println("recieved a message about an action");
        }
    }

    public void con1In(){
        Message message = null;
        while(true) {
            System.out.println("con1In");
            try {
                message = (Message) connection1.getInStream().readObject();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("con1 got:"+ message);
            con1Message=message;
        }
    }

    public void con2In(){
        Message message = null;
        while(true) {
            try {
                message = (Message) connection2.getInStream().readObject();
            } catch (Exception e) {
                System.out.println(e);
            }
            con2Message=message;
        }
    }

}
