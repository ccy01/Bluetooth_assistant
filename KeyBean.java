package com.example.ccy.assistant;

/**
 * Created by ccy on 2017/5/15.
 */

public class KeyBean {

    private boolean rb_char_p;
    private boolean rb_char_u;

    public boolean isRb_char_u() {
        return rb_char_u;
    }

    public void setRb_char_u(boolean rb_char_u) {
        this.rb_char_u = rb_char_u;
    }

    private String et_name;
    private String et_p_msg;
    private String et_u_msg;

    public boolean isRb_char_p() {
        return rb_char_p;
    }

    public void setRb_char_p(boolean rb_char_p) {
        this.rb_char_p = rb_char_p;
    }



    public String getEt_name() {
        return et_name;
    }

    public void setEt_name(String et_name) {
        this.et_name = et_name;
    }

    public String getEt_p_msg() {
        return et_p_msg;
    }

    public void setEt_p_msg(String et_p_msg) {
        this.et_p_msg = et_p_msg;
    }

    public String getEt_u_msg() {
        return et_u_msg;
    }

    public void setEt_u_msg(String et_u_msg) {
        this.et_u_msg = et_u_msg;
    }
}
