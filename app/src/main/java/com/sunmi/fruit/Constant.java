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
    public static final ArrayList<String> arrayType=new ArrayList<>();
    public static final HashMap<String, String> hashName=new HashMap<String,String>();
    public static final HashMap<String, Float> hashPrice=new HashMap<String,Float>();

    static {
        arrayType.add("apple");
        arrayType.add("pear");
        arrayType.add("orange");
        arrayType.add("pitaya");
        arrayType.add("lemon");
        arrayType.add("bread");

        hashName.put("apple","苹果");
        hashName.put("pear","梨");
        hashName.put("orange","橙子");
        hashName.put("pitaya","火龙果");
        hashName.put("lemon","柠檬");
        hashName.put("bread","面包");

        hashPrice.put("apple",0.0225f);   //100g/2.25元  计算 1g 多少钱
        hashPrice.put("pear",0.0153f);         //320g/4.9元
        hashPrice.put("orange",0.0043f);     //1300g/5.6元
        hashPrice.put("pitaya",0.1234f);
        hashPrice.put("lemon",0.0234f);
        hashPrice.put("bread",1.3456f);
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
