package org.example.shotgungame;


public class Room implements Runnable {
    CommunicationConnection connection1;
    CommunicationConnection connection2;
    boolean roundOver = false;
    int score1;
    int score2;


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
        while(true) {
            while (!roundOver) {
                System.out.println("room looking for stuff");
                while (connection1.selection == 0 || connection2.selection == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    System.out.println("moves made: 1:" + connection1.selection + " 2: " + connection2.selection);
                    if (connection1.selection == 10) {
                        if (connection2.selection == 10) {
                            connection1.sendMessage(new Message(9, "blackMagic"));
                            connection2.sendMessage(new Message(9, "blackMagic"));
                            roundOver = true;
                        } else {
                            connection1.sendMessage(new Message(7, "blackMagic"));
                            connection2.sendMessage(new Message(8, "blackMagic"));
                            score1++;
                            roundOver = true;
                        }
                    } else if (connection2.selection == 10) {
                        connection1.sendMessage(new Message(8, "blackMagic"));
                        connection2.sendMessage(new Message(7, "blackMagic"));
                        roundOver = true;
                        score2++;

                    } else if (connection1.selection == 4) {
                        if (connection2.selection == 4) {
                            connection2.sendMessage(new Message(9, "shot"));
                            connection1.sendMessage(new Message(9, "shot"));
                            roundOver = true;
                        } else if (connection2.selection == 5) {
                            connection2.sendMessage(new Message(8, "shot"));
                            connection1.sendMessage(new Message(7, "shot"));
                            score1++;
                            roundOver = true;
                        } else {
                            connection1.sendMessage(new Message(connection2.selection, "opponent's choice"));
                            connection2.sendMessage(new Message(connection1.selection, "opponent's choice"));
                        }
                    } else if (connection2.selection == 4) {
                        if (connection1.selection == 5) {
                            connection1.sendMessage(new Message(8, "shot"));
                            connection2.sendMessage(new Message(7, "shot"));
                            score2++;
                            roundOver = true;
                        } else {
                            connection1.sendMessage(new Message(connection2.selection, "opponent's choice"));
                            connection2.sendMessage(new Message(connection1.selection, "opponent's choice"));
                        }
                    } else {
                        connection1.sendMessage(new Message(connection2.selection, "opponent's choice"));
                        connection2.sendMessage(new Message(connection1.selection, "opponent's choice"));
                    }

                    connection1.selection = 0;
                    connection2.selection = 0;
                } catch (Exception e) {
                    System.out.println("something went wrong with game logic");
                }


                System.out.println("recieved both message about an action");
                System.out.println(connection1.selection + " 2: " + connection2.selection);
                connection1.selection = 0;
                connection2.selection = 0;
            }
            System.out.println("round over");
            try {Thread.sleep(5000);} catch (InterruptedException e) {}
            roundOver = false;
            connection1.sendMessage(new Message(11,score1,score2));
            connection2.sendMessage(new Message(11,score2,score1));
        }
    }

}
