package com.hbmcc.wangsen.netsupport.ui.fragment.other;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GrayInnerService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(12121, new Notification());
        stopForeground(true);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

