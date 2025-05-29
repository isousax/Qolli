package br.com.Qolli.dto;

import java.util.Date;

public class Message {
    private String from;
    private String to;
    private String text;
    private Date timestamp;

    public Message(String from, String to, String text, Date timestamp) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
