package com.sunmi.fruit.util;

import android.os.RemoteException;

import com.sunmi.fruit.MyApp;
import com.sunmi.scalelibrary.ScaleManager;
import com.sunmi.scalelibrary.ScaleResult;

public class ScaleManagerUtil {
    private static ScaleManagerUtil scaleManagerUtil = new ScaleManagerUtil();

    public static ScaleManagerUtil getInstance() {
        return scaleManagerUtil;
    }

    private ScaleManager scaleManager;
    public static int lastnet;

    public void scaleInit(ScaleResults scaleResults) {
        lastnet=0;
        if (scaleManager == null) {
            scaleManager = ScaleManager.getInstance(MyApp.mApp);
            scaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
                @Override
                public void onServiceConnected() {
                    //服务绑定
                    try {
                        scaleManager.getData(new ScaleResult() {
                            @Override
                            public void getResult(int net, int tare, boolean isStable) {
//                                LogSunmi.e("TAG", "秤 结果=  获取称量净重=" + net + "  获取称量⽪重=" + tare + "   秤稳定状态=" + isStable);
                                if(!isStable){
                                    lastnet=0;
                                }
                                if(scaleResults!=null){
                                    scaleResults.getResultRate(net,tare,isStable);
                                }
                                if (isStable&&net!=0&&net!=lastnet) {
                                    lastnet=net;

                                    if(scaleResults!=null&&net>0){
                                        scaleResults.getResult(net,tare,isStable);
                                    }
                                }
                            }

                            @Override
                            public void getStatus(boolean isLightWeight, boolean overload, boolean clearZeroErr, boolean calibrationErr) {
//                                LogSunmi.e("TAG", "秤 状态=  秤是否过轻（⼩于20E）=" + isLightWeight + "  秤是否过载=" + overload + "   秤是否清零错误=" + clearZeroErr + "   秤是否标定错误=" + calibrationErr);
                            }
                        });
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnect() {
                    //服务解绑
                }
            });
        }
    }

    public void destory(){
        try {
            if(scaleManager!=null) {
                scaleManager.cancelGetData();
                scaleManager.onDestroy();
                scaleManager = null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public interface ScaleResults{
        void getResult(int net, int tare, boolean isStable);
        void getResultRate(int net, int tare, boolean isStable);
    }
}
