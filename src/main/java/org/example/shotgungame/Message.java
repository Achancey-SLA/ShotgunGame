package org.example.shotgungame;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message implements Serializable {
    private static final long serialVersionUID = 1234567L;
    Integer version = 1;
    Integer mode;
    // 1: START
    // 2: confirm room joined
    // 3: STOP
    //4: shoot
    //5: reload

    //6: block (server sends these messages back to say what the opponent did)

    //7: win game
    //8: lose game
    //9: draw game
    String text;
    String from;

    //LocalDate timeStamp;
    //Media voice;
    //Image picture;

    public Message( Integer mode, String text,String from) throws UnknownHostException {
        this.mode = mode;
        this.text = text;
        this.from = from;
    }


    public Message(Integer mode, String text) throws UnknownHostException {
        this.mode = mode;
        this.text = text;
        this.from = "SERVER";
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "version=" + version +
                ", mode=" + mode +
                ", text='" + text + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}