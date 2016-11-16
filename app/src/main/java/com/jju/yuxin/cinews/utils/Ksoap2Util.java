package com.jju.yuxin.cinews.utils;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;



public class Ksoap2Util {
    private static String path = "http://www.jxci.cn/WebService.asmx";
    private static String namespace = "http://tempuri.org/";

    public static void doBackgroud(final Handler handler, final int what, final String name, final Map<String, Object> oMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String info = null;
                info = connect(name, oMap);
                Message message = handler.obtainMessage();
                //TODO
                //message.obj = JsonUtil.getInstance().StringToJson(info, what);
                message.obj = info;
//                MyLogger.zLog().e(info+"!!!!!!!!!!!!!!!!!");
                message.what = what;
                handler.sendMessage(message);

            }
        }).start();

    }

    public static String connect(final String name, final Map<String, Object> oMap) {
        String info = null;
        try {

            //1. 指定WebService的命名空间和调用的方法名
            SoapObject soapObject = new SoapObject(namespace, name);

            //2. 设置调用方法的参数值，这一步是可选的，如果方法没有参数，可以省略这一步。
            for (Map.Entry<String, Object> entry : oMap.entrySet()) {
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }

            //3.封装数据，序列化，设置访问soap协议的版本号
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = soapObject;//设置所传递的数据类型
            envelope.setOutputSoapObject(soapObject); //表示将传递将传递给服务端的数据进行序列化
            envelope.dotNet = true;//允许访问网络

            //4. 创建HttpTransportSE对象。
            HttpTransportSE hts = new HttpTransportSE(path, 10000);
            hts.debug = true; //打印日志文件
            hts.call(namespace + name, envelope);//执行webservice操作

            //5.使用getResponse方法获得WebService方法的返回结果
            info = envelope.getResponse().toString();
            MyLogger.zLog().e(info+"$$$$$$$$$$$$$$$$");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

}

