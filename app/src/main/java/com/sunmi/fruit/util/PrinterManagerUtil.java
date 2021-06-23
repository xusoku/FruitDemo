package com.sunmi.fruit.util;

import android.os.RemoteException;
import android.provider.Settings;

import com.sunmi.fruit.MyApp;
import com.sunmi.fruit.R;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import static com.sunmi.fruit.util.HttpUtil.oneSecondAction;

public class PrinterManagerUtil {

    private static final String TAG="PrinterManagerUtil";
    private static PrinterManagerUtil printerManagerUtil = new PrinterManagerUtil();

    public static PrinterManagerUtil getInstance() {
        return printerManagerUtil;
    }

    private InnerPrinterCallback innerPrinterCallback;

    private SunmiPrinterService services;
    private PrintResults printResults;


    public void print(PrintResults printResults){
        this.printResults=printResults;
        int isPrinter = Settings.Global.getInt(MyApp.mApp.getContentResolver(), "sunmi_printer", 0);
        if(isPrinter==0){
            LogSunmi.e(TAG, isPrinter + "  没有打印机");
            return;
        }
        if(services==null){
            printInit(true);
        }else{
            printF(services);
        }
    }


    public void printInit()  {
        printInit(false);
    }
    private void printInit(boolean isPrinter)  {
        innerPrinterCallback = new InnerPrinterCallback() {
            @Override
            protected void onConnected(SunmiPrinterService service) {
                //这⾥里里即获取到绑定服务成功连接后的远程服务接⼝口句句柄
                // 可以通过service调⽤用⽀支持的打印⽅方法
                LogSunmi.e(TAG,  "打印初始化成功");
                services=service;
                if(isPrinter){
                    printF(services);
                }
            }
            @Override
            protected void onDisconnected() {
                //当服务异常断开后，会回调此⽅方法，建议在此做重连策略略
                LogSunmi.e(TAG,  "打印服务断开");
            }
        };
        boolean result = false;
        try {
            result = InnerPrinterManager.getInstance().bindService(MyApp.mApp, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        LogSunmi.e(TAG,  "打印服务="+result);

    }

    public void printF(SunmiPrinterService service) {
        if(oneSecondAction()){
            return;
        }
        try {
            int i= SharedPreferencesUtil.getInt(MyApp.mApp,"printN");
             i+=1;
            SharedPreferencesUtil.setInt(MyApp.mApp,i,"printN");
            String str=MyApp.mApp.getResources().getText(R.string.tip_print1).toString();

            service.setAlignment(1,null);
            service.printBitmap(Utils.rawToBitmap(),new InnerResultCallback(){
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                    LogSunmi.e(TAG,  "打印结果="+isSuccess);
//                    if(printResults!=null){
//                        printResults.getResult();
//                    }
                }

                @Override
                public void onReturnString(String result) throws RemoteException {

                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {

                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {

                }
            });
            service.printText(String.format(str,i), new InnerResultCallback() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                    //返回接⼝口执⾏行行的情况(并⾮非真实打印):成功或失败
                    LogSunmi.e(TAG,  "打印结果="+isSuccess);
                    if(printResults!=null){
                        printResults.getResult();
                    }

                }

                @Override
                public void onReturnString(String result) throws RemoteException {
                    //部分接⼝口会异步返回查询数据
                    LogSunmi.e(TAG,  "打印结果1="+result);
                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {
                    //接⼝口执⾏行行失败时，返回的异常状态说明
                    LogSunmi.e(TAG,  "打印异常="+code+ "  结果="+msg);

                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {
                    //事务模式下真实的打印结果返回
                    LogSunmi.e(TAG,  "打印结果2="+code+ "  结果="+msg);
                }
            });

            service.lineWrap(3,null);

            service.cutPaper(new InnerResultCallback() {
                @Override
                public void onRunResult(boolean isSuccess) throws RemoteException {
                    LogSunmi.e(TAG,  "切刀  onRunResult="+isSuccess);
                }

                @Override
                public void onReturnString(String result) throws RemoteException {
                    LogSunmi.e(TAG,  "切刀  onReturnString="+result);
                }

                @Override
                public void onRaiseException(int code, String msg) throws RemoteException {
                    LogSunmi.e(TAG,  "切刀 onRaiseException="+code+ "  结果="+msg);
                }

                @Override
                public void onPrintResult(int code, String msg) throws RemoteException {
                    LogSunmi.e(TAG,  "切刀 onPrintResult="+code+ "  结果="+msg);
                }
            });
        } catch (RemoteException e) {
            LogSunmi.e(TAG,  "切刀异常1="+e.toString());
        }
    }


    public void onDestroy() {
        try {
            InnerPrinterManager.getInstance().unBindService(MyApp.mApp, innerPrinterCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PrintResults{
        void getResult();
    }


    private String ss="" +
            "                     _ooOoo_ \n" +
            "                    o8888888o \n" +
            "                    88\" . \"88 \n" +
            "                    (| -_- |) \n" +
            "                    O\\  =  /O \n" +
            "                 ____/`---'\\____ \n" +
            "               .'  \\\\|     |//  `. \n" +
            "              /  \\\\|||  :  |||//  \\ \n" +
            "             /  _||||| -:- |||||-  \\ \n" +
            "             |   | \\\\\\  -  /// |   | \n" +
            "             | \\_|  ''\\---/''  |   | \n" +
            "             \\  .-\\__  `-`  ___/-. / \n" +
            "           ___`. .'  /--.--\\  `. . __ \n" +
            "        .\"\" '<  `.___\\_<|>_/___.'  >'\"\". \n" +
            "       | | :  `- \\`.;`\\ _ /`;.`/ - ` : | | \n" +
            "       \\  \\ `-.   \\_ __\\ /__ _/   .-` /  / \n" +
            "  ======`-.____`-.___\\_____/___.-`____.-'====== \n" +
            "                     `=---=' \n" +
            "  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ \n" +
            "                       佛祖保佑    " +
            "    \n\n" +
            "           永无BUG佛曰: \n \n" +

            "           写字楼里写字间，写字间里程序员； \n" +
            "           程序人员写程序，又拿程序换酒钱。 \n" +
            "           酒醒只在网上坐，酒醉还来网下眠； \n" +
            "           酒醉酒醒日复日，网上网下年复年。 \n" +
            "           但愿老死电脑间，不愿鞠躬老板前； \n" +
            "           奔驰宝马贵者趣，公交自行程序员。 \n" +
            "           别人笑我忒疯癫，我笑自己命太贱； \n" +
            "           不见满街漂亮妹，哪个归得程序员？ \n" +
            "";
}
