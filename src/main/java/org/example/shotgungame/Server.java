package org.example.shotgungame;

import java.util.ArrayList;

public class Server {
    public static Queue theQueue = new Queue();
    static ArrayList<CommunicationConnection> allConnections = new ArrayList<>();

    public static void main(String[] args)  {
        ServerConnector myServerConnector =  new ServerConnector();
        Thread myServerConnectorThread = new Thread(myServerConnector);
        myServerConnectorThread.start();

        CommunicationOut myCommunicationOut = new CommunicationOut();
        Thread myCommunicationOutThread = new Thread(myCommunicationOut);
        myCommunicationOutThread.start();
    }
}