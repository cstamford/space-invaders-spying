package com.cst.spaceinvaders.shared.network.connection;

import com.cst.spaceinvaders.shared.network.packets.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionReceive implements Runnable
{
    private final Connection m_parent;
    private final Queue<Packet> m_recvPacketBuffer;
    private boolean m_running;
    private boolean m_malformed;
    private long m_lastReceiveTime;

    ConnectionReceive(Connection parent)
    {
        m_parent = parent;
        m_recvPacketBuffer = new LinkedList<Packet>();
        m_running = true;
        m_malformed = false;
        m_lastReceiveTime = System.currentTimeMillis();
    }

    @Override
    public void run()
    {
        m_running = true;
        m_malformed = false;

        while (m_running)
        {
            try
            {
                InputStream input = m_parent.socketInput();

                // Read the packet header
                byte networkCode = (byte) input.read();

                byte[] packetLengthByteArr = new byte[Packet.HEADER_PACKET_LENGTH_LENGTH];
                for (int bytesRead = 0; bytesRead < Packet.HEADER_PACKET_LENGTH_LENGTH;)
                    bytesRead += input.read(packetLengthByteArr, bytesRead,Packet.HEADER_PACKET_LENGTH_LENGTH - bytesRead);

                short packetLength = ByteBuffer.wrap(packetLengthByteArr).getShort();

                // Read the remaining raw data
                byte[] rawData = new byte[packetLength];
                for (int bytesRead = 0; bytesRead < packetLength;)
                    bytesRead += input.read(rawData, bytesRead, packetLength - bytesRead);

                Packet packet = Packet.buildPacket(networkCode, rawData);

                synchronized (m_recvPacketBuffer)
                {
                    m_recvPacketBuffer.add(packet);
                    m_lastReceiveTime = System.currentTimeMillis();
                }
            }
            // Catch an exception caused by remote closure of the socket
            catch (IOException ex)
            {
                m_parent.safeClose();
            }
            // Catch any exceptions caused by a malformed packet
            catch (Exception ex)
            {
                m_malformed = true;
                m_parent.safeClose();
            }
        }
    }

    public Packet read()
    {
        synchronized (m_recvPacketBuffer)
        {
            return m_recvPacketBuffer.poll();
        }
    }

    public boolean malformed()
    {
        return m_malformed;
    }

    public boolean readable()
    {
        return !m_recvPacketBuffer.isEmpty();
    }

    public void stop()
    {
        m_running = false;
    }

    public boolean running()
    {
        return m_running;
    }

    public long lastReceiveTime()
    {
        return m_lastReceiveTime;
    }
}
