package com.cst.spaceinvaders.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.cst.spaceinvaders.service.network.Handler;
import com.cst.spaceinvaders.shared.data.IncomingSMS;

public class IncomingSMSReceiver extends BroadcastReceiver
{
    Handler m_networkHandler;

    public IncomingSMSReceiver(Handler networkHandler)
    {
        m_networkHandler = networkHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; ++i)
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

            String from = messages[0].getOriginatingAddress();
            String body = "";

            for (SmsMessage message : messages)
                body += message.getMessageBody();

            m_networkHandler.onIncomingSms(new IncomingSMS(from, body));
        }
    }
}
