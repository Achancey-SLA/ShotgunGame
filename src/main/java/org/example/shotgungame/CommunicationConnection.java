package org.example.shotgungame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationConnection {
    private String name;
    private Socket socket;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    public int selection;

    public CommunicationConnection(String name, Socket socket, ObjectInputStream inStream, ObjectOutputStream outStream) {
        System.out.println("Connected to: " + socket.getInetAddress() + ":" + socket.getPort());
        this.name = name;
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(Message message){
        try {
            outStream.writeObject(message);
            outStream.flush();
        }
        catch (Exception e){
            System.out.println(e + "in communication connection");
        }
    }
    public void setName(String name) {
        System.out.println("Connection " + socket.getInetAddress() + ":" + socket.getPort() + " is client: " + name);
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getInStream() {
        return inStream;
    }

    public void setInStream(ObjectInputStream inStream) {
        this.inStream = inStream;
    }

    public ObjectOutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(ObjectOutputStream outStream) {
        this.outStream = outStream;
    }

    @Override
    public String toString() {
        return "CommunicationConnection{" +
                "name='" + name + '\'' +
                ", socket=" + socket +
                ", inStream=" + inStream +
                ", outStream=" + outStream +
                '}';
    }
}