package com.sunmi.fruit;


import android.text.TextUtils;

import com.sunmi.fruit.util.LogSunmi;
import com.sunmi.fruit.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 存放一些全局的常量
 */
public class Constant {

    public static String locName="zh";
    public static final ArrayList<String> arrayBreadType=new ArrayList<>();
    public static final ArrayList<String> arrayType=new ArrayList<>();
    public static final HashMap<String, String> hashName=new HashMap<String,String>();
    public static final HashMap<String, String> hashNameEn=new HashMap<String,String>();
    public static final HashMap<String, Float> hashPrice=new HashMap<String,Float>();
    public static final HashMap<String, Integer> hasBreadPrice=new HashMap<String,Integer>();
    public static final HashMap<String, Integer> hasfruitWeight=new HashMap<String,Integer>();

    static {
        arrayBreadType.add("bread3");
        arrayBreadType.add("bread2");
        arrayBreadType.add("bread4");

        arrayType.add("apple");
        arrayType.add("green-apple");
        arrayType.add("pear");
        arrayType.add("pitaya");
        arrayType.add("lemon");

        hashName.put("apple","红苹果");
        hashName.put("green-apple","青苹果");
        hashName.put("pear","梨");
        hashName.put("pitaya","火龙果");
        hashName.put("lemon","柠檬");

        hashName.put("bread2","吐司");
        hashName.put("bread3","甜甜圈");
        hashName.put("bread4","小面包");

        hashNameEn.put("apple","apples");
        hashNameEn.put("green-apple","green apples");
        hashNameEn.put("pear","pears");
        hashNameEn.put("pitaya","pitayas");
        hashNameEn.put("lemon","lemons");

        hashNameEn.put("bread2","toasts");
        hashNameEn.put("bread3","donuts");
        hashNameEn.put("bread4","buns");

        hashPrice.put("apple",0.01f);
        hashPrice.put("green-apple",0.03f);
        hashPrice.put("pear",0.005f);
        hashPrice.put("pitaya",0.02f);
        hashPrice.put("lemon",0.025f);

        hashPrice.put("bread2",0.11f);
        hashPrice.put("bread3",0.125f);
        hashPrice.put("bread4",0.165f);


        hasBreadPrice.put("bread2",10);
        hasBreadPrice.put("bread3",16);
        hasBreadPrice.put("bread4",12);

        hasBreadPrice.put("apple",9);
        hasBreadPrice.put("green-apple",12);
        hasBreadPrice.put("pear",5);
        hasBreadPrice.put("pitaya",12);
        hasBreadPrice.put("lemon",8);


        hasfruitWeight.put("apple",800);
        hasfruitWeight.put("green-apple",500);
        hasfruitWeight.put("pear",600);
        hasfruitWeight.put("pitaya",1000);
        hasfruitWeight.put("lemon",300);

        hasfruitWeight.put("bread2",-1);
        hasfruitWeight.put("bread3",-1);
        hasfruitWeight.put("bread4",-1);


    }

    // 服务器地址
    public static String SERVER_ADDRESS;

    // 函数加密的密钥
    public static String IS_ENCRYPTED;  // 是否加密 (0：不加密  1：加密)
    public static String DELIVER_KEY;   // MD5密钥
    public static String DES_KEY;       // des加密的key
    public static String DES_VI;        // des的vi
    public static String LANGUAGE;      // 语言


    static {
        switch (BuildConfig.Environment) {
            case 1:
                // 发布版本
                SERVER_ADDRESS = "https://api.sunmi.com/v2";
                DELIVER_KEY = "Jihewobox15";
                DES_VI = "98765432";
                DES_KEY = "d38f5dba03944f5fbd83edbb15a85806";
                IS_ENCRYPTED = "1";
                break;
            case 2:
                // UAT环境
                SERVER_ADDRESS = "https://api.uat.sunmi.com/v2";
                DELIVER_KEY = "Jihewobox15";
                DES_VI = "98765432";
                DES_KEY = "d38f5dba03944f5fbd83edbb15a85806";
                IS_ENCRYPTED = "1";
                break;
            case 3:
                // 测试环境
                SERVER_ADDRESS = "https://api.test.sunmi.com/v2";
                DELIVER_KEY = "Woyouxinxi666";
                DES_KEY = "69fd1caee4a34186a8b1d096bbdac707";
                DES_VI = "12345678";
                IS_ENCRYPTED = "1";
                break;
            case 4:
                // 开发环境
                SERVER_ADDRESS = "https://api.dev.sunmi.com/";
                DELIVER_KEY = "Woyouxinxi666";
                DES_KEY = "69fd1caee4a34186a8b1d096bbdac707";
                DES_VI = "12345678";
                IS_ENCRYPTED = "0";
                break;
            case 5:
                // CTE环境
                SERVER_ADDRESS = "http://cte.api.sunmi.com:80/";
                DELIVER_KEY = "Woyouxinxi666";
                DES_KEY = "69fd1caee4a34186a8b1d096bbdac707";
                DES_VI = "12345678";
                IS_ENCRYPTED = "0";
                break;
            default:
                break;
        }
    }


}
