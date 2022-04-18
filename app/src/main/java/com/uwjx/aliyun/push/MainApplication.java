package com.uwjx.aliyun.push;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;


import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;

public class MainApplication extends Application {

    private static final String TAG = "alipush";
    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        initCloudChannel(this);




    }
    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        this.createNotificationChannel();

        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.setLogLevel(CloudPushService.LOG_DEBUG);   //仅适用于Debug包，正式包不需要此行
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success " + pushService.getDeviceId());
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        HuaWeiRegister.register(this); // 接入华为辅助推送

        GcmRegister.register(applicationContext, "657573510125", "1:657573510125:android:a3b5e9163dc21e196905e4"); // 接入FCM/GCM初始化推送
    }

    private void createNotificationChannel() {
        //https://help.aliyun.com/document_detail/67398.html?spm=a2c4g.11186623.0.0.360b526dqXQc9P
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id。
            String id = "uwjx-push-channel";
            // 用户可以看到的通知渠道的名字。
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述。
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性。
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果Android设备支持的话）。
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果Android设备支持的话）。
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // 最后在notificationmanager中创建该通知渠道。
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

}
