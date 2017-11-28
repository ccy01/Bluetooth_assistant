package com.example.ccy.assistant;

/**
 * Created by ccy on 2017/5/15.
 */

public class ToggleBean {

    private String on;
    private String off;
    private String msg_on;
    private String msg_off;
    private boolean isCharCheckOn;
    private boolean isCharCheckOff;

    public boolean isCharCheckOn() {
        return isCharCheckOn;
    }

    public void setCharCheckOn(boolean charCheckOn) {
        isCharCheckOn = charCheckOn;
    }

    public boolean isCharCheckOff() {
        return isCharCheckOff;
    }

    public void setCharCheckOff(boolean charCheckOff) {
        isCharCheckOff = charCheckOff;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public void setMsg_on(String msg_on) {
        this.msg_on = msg_on;
    }

    public void setMsg_off(String msg_off) {
        this.msg_off = msg_off;
    }

    public String getOn() {
        return on;
    }

    public String getOff() {
        return off;
    }

    public String getMsg_on() {
        return msg_on;
    }

    public String getMsg_off() {
        return msg_off;
    }

}
