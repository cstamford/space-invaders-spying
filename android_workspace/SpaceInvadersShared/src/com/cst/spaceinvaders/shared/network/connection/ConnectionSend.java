package com.cst.spaceinvaders.shared.network.connection;

import com.cst.spaceinvaders.shared.network.packets.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConnectionSend implements Runnable
{
    // How many times a second the buffer will be checked by the connection send
    // thread to ensure it is not empty.
    private static final float CHECK_BUFFER_PER_SECOND = 100.0f;

    // The duration that the thread much sleep each tick to meet the target goal of
    // CHECK_BUFFER_PER_SECOND updates per second.
    private static final float SLEEP_DELAY = 1000.0f / CHECK_BUFFER_PER_SECOND;

    private final Connection m_parent;
    private final Queue<Packet> m_sendPacketBuffer;
    private boolean m_running;
    private long m_lastSendTime;

    ConnectionSend(Connection parent)
    {
        m_parent = parent;
        m_sendPacketBuffer = new LinkedList<Packet>();
        m_running = true;
        m_lastSendTime = System.currentTimeMillis();
    }

    @Override
    public void run()
    {
        m_running = true;
        while (m_running)
        {
            try
            {
                if (!m_sendPacketBuffer.isEmpty())
                {
                    List<byte[]> rawPackets = new ArrayList<byte[]>();

                    synchronized (m_sendPacketBuffer)
                    {
                        while (!m_sendPacketBuffer.isEmpty())
                            rawPackets.add(m_sendPacketBuffer.poll().toBytes());
                    }

                    int size = 0;
                    for (byte[] packet : rawPackets)
                        size += packet.length;

                    int offset = 0;
                    byte[] encoded = new byte[size];

                    for (byte[] packet : rawPackets)
                    {
                        System.arraycopy(packet, 0, encoded, offset, packet.length);
                        offset += packet.length;
                    }

                    m_parent.socketOutput().write(encoded);
                }
                else
                {
                    try
                    {
                        // Instead of busy spinning, we should make the thread sleep.
                        // This helps to minimise power usage.
                        Thread.sleep((long)SLEEP_DELAY);
                    }
                    catch (InterruptedException ignored)
                    {
                        // Intentionally left empty.
                    }
                }
            }
            // Catch an exception caused by remote closure of the socket
            catch (IOException ex)
            {
                m_parent.safeClose();
            }
        }
    }

    public void write(Packet packet)
    {
        synchronized (m_sendPacketBuffer)
        {
            m_sendPacketBuffer.add(packet);
        }

        m_lastSendTime = System.currentTimeMillis();
    }

    public void write(Packet[] packets)
    {
        synchronized (m_sendPacketBuffer)
        {
            for (Packet packet : packets)
                m_sendPacketBuffer.add(packet);
        }

        m_lastSendTime = System.currentTimeMillis();
    }

    public void stop()
    {
        m_running = false;
    }

    public boolean running()
    {
        return m_running;
    }

    public long lastSendTime()
    {
        return m_lastSendTime;
    }
}
