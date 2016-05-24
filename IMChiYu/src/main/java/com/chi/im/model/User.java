package com.chi.im.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class User implements Serializable {
    String  name;//james
    String  user;//james@192.168.1.134

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
