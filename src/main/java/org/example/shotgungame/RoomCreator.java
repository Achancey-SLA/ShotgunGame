package org.example.shotgungame;

public class RoomCreator implements Runnable {
    public void run() {
        // GET
        while (!Thread.currentThread().isInterrupted()) {
            if(Server.queuedPlayers.size()>=2){
                Room room = new Room(Server.queuedPlayers.get(0),Server.queuedPlayers.get(1));
                Server.queuedPlayers.remove(0);
                Server.queuedPlayers.remove(0);
                Thread roomThread = new Thread(room);
                roomThread.start();
            }
        }
    }
}