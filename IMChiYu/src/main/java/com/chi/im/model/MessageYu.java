package com.chi.im.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MessageYu implements Serializable {

    private Long id;
    /**
     * Not-null value.
     */
    private String from;
    /**
     * Not-null value.
     */
    private String to;
    private String body;
    private String type;
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageYu(String date, String type, String body, String to, String from) {

        this.date = date;
        this.type = type;
        this.body = body;
        this.to = to;
        this.from = from;
    }
}
