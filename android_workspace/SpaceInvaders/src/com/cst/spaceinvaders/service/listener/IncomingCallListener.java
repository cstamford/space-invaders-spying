package com.cst.spaceinvaders.service.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.cst.spaceinvaders.service.network.Handler;
import com.cst.spaceinvaders.shared.data.IncomingCall;

public class IncomingCallListener extends PhoneStateListener
{
    private Handler m_networkHandler;

    public IncomingCallListener(Handler networkHandler)
    {
        m_networkHandler = networkHandler;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber)
    {
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:
                m_networkHandler.onIncomingCall(new IncomingCall(incomingNumber));
                break;
        }
    }
}