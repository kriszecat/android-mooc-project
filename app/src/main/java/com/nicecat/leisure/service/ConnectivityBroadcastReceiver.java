package com.nicecat.leisure.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

@EBean
public class ConnectivityBroadcastReceiver extends BroadcastReceiver {
    public final static int CONNECTIVITY_NOTIFICATION_ID =
            ConnectivityBroadcastReceiver.class.getSimpleName().hashCode();

    @SystemService
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // TODO update connectivity notification
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                NetworkInfo otherNetworkInfo = (NetworkInfo) intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
                String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);

                boolean isFailover = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_IS_FAILOVER, false);

                String contentText = String.format(
                        "NetworkInfo=%s\nOtherNetworkInfo=%s\nconnectivity=%b\nreason=%s",
                        networkInfo.toString(),
                        (otherNetworkInfo == null)? "[none]": otherNetworkInfo.toString(),
                        noConnectivity, reason.toString());
            }
        }
    }
};