package com.cst.spaceinvaders.shared.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Stack;

import com.cst.spaceinvaders.shared.network.packets.Packet;

public class Connection
{
	private final Socket m_socket;
    private final ConnectionReceive m_recv;
    private final ConnectionSend m_send;
    private final Thread m_recvThread;
    private final Thread m_sendThread;

	public Connection(Socket socket)
	{
		m_socket = socket;
        m_recv = new ConnectionReceive(this);
        m_send = new ConnectionSend(this);
        m_recvThread = new Thread(m_recv);
        m_sendThread = new Thread(m_send);
        m_sendThread.start();
        m_recvThread.start();
    }

	public void close() throws IOException
	{
        m_send.stop();
        m_recv.stop();
        socketOutput().close();
        socketInput().close();
		m_socket.close();
	}

    public void safeClose()
    {
        try
        {
            close();
        }
        catch (IOException ignored)
        {
            // Intentionally left empty -- if this exception is caught, the
            // socket is already closed, so it doesn't matter.
        }
    }
	
	public Socket socket()
	{
		return m_socket;
	}
	
	public InputStream socketInput() throws IOException
	{
		return m_socket.getInputStream();
	}
	
	public OutputStream socketOutput() throws IOException
	{
		return m_socket.getOutputStream();
	}
	
	public boolean readable()
	{
		return m_recv.readable();
	}
	
	public boolean connected()
	{
		return m_recv.running() && m_send.running();
	}

    public boolean malformed()
    {
        return m_recv.malformed();
    }
	
	public Packet read()
	{
		return m_recv.read();
	}

    public void write(Packet packet)
    {
        m_send.write(packet);
    }

    public void write(Packet[] packet)
    {
        m_send.write(packet);
    }

    public long lastSendTime()
    {
        return m_send.lastSendTime();
    }

    public long lastReceiveTime()
    {
        return m_recv.lastReceiveTime();
    }

}
