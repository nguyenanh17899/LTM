/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Request implements Serializable{
    private Object std;
    private int num;
    private static final long serialVersionUID = 7489805921998898508L;

    public Request() {
    }

    public Request(Object std, int num) {
        this.std = std;
        this.num = num;
    }

    public Object getStd() {
        return std;
    }

    public void setStd(Object std) {
        this.std = std;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
