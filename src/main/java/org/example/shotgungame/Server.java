package org.example.shotgungame;

import java.util.ArrayList;

public class Server {
    static ArrayList<CommunicationConnection> allConnections = new ArrayList<>();
    static ArrayList<CommunicationConnection> queuedPlayers= new ArrayList<>();

    public static void main(String[] args)  {
        ServerConnector myServerConnector =  new ServerConnector();
        Thread myServerConnectorThread = new Thread(myServerConnector);
        myServerConnectorThread.start();

        RoomCreator myRoomCreator = new RoomCreator();
        Thread myCommunicationOutThread = new Thread(myRoomCreator);
        myCommunicationOutThread.start();
    }
}