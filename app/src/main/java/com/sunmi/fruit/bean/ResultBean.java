package com.sunmi.fruit.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultBean {
    public int code;

    public String msg;

    public static class model{
        public String name;
        public float score;

        @Override
        public String toString() {
            return "model{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", msg=" + msg +
                '}';
    }
}
