package com.sunmi.fruit.util;


import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunmi.fruit.MyApp;
import com.sunmi.fruit.bean.ResultBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private final static String TAG = HttpUtil.class.getSimpleName();

    public static HttpUtil httpUtil = new HttpUtil();

    public static HttpUtil getInstance() {
        return httpUtil;
    }

    private String url = "http://expo-fruit-det.test.sunmi.com/v1/fruit/recognition";
    OkHttpClient okHttpClient;

    public HttpUtil() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(new LoggingInterceptor());
        okHttpClient = builder.build();
//        final Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写
//                .build();
    }

    public void post(String requestBody, HttpResults httpResults) {
        if (oneSecondAction()) {
            return;
        }
        url=SharedPreferencesUtil.getString(MyApp.mApp,"mock_env");
        if(TextUtils.isEmpty(url)){
            url = "http://expo-fruit-det.test.sunmi.com/v1/fruit/recognition";
        }
        RequestBody body = new FormBody.Builder()
                .add("image", requestBody)
                .add("image_type", "BASE64").build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogSunmi.d("http", "onFailure: ");
                if (httpResults != null) {
                    httpResults.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.body() != null) {
                    String s1 = response.body().string();
                    LogSunmi.d("http", "onResponse: " + s1);
                    try {
                        ResultBean resultBean = new Gson().fromJson(s1, ResultBean.class);
                        Type type = new TypeToken<List<ResultBean.model>>() {
                        }.getType();
                        List<ResultBean.model> models = new Gson().fromJson(resultBean.msg, type);
                        if (models != null && models.size() > 0) {
                            ResultBean.model model = models.get(0);
                            if (model != null) {
                                String name = model.name;
                                if (httpResults != null) {
                                    httpResults.onResponse(name);
                                }
                            } else {
                                if (httpResults != null) {
                                    httpResults.onFailure();
                                }
                            }
                        } else {
                            if (httpResults != null) {
                                httpResults.onFailure();
                            }
                        }
                        LogSunmi.d("http", "onResponse: " + models.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (httpResults != null) {
                            httpResults.onFailure();
                        }
                    }

                } else {
                    if (httpResults != null) {
                        httpResults.onFailure();
                    }
                }

            }
        });
    }

    public interface HttpResults {
        void onResponse(String name);

        void onFailure();
    }

    private static long time = 0;

    public static boolean oneSecondAction() {
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - time) >= 600) {
            time = currentTime;
            return false;
        }
        return true;
    }
}
