package com.cst.spaceinvaders.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cst.spaceinvaders.base.Loggable;
import com.cst.spaceinvaders.shared.data.Globals;
import com.cst.spaceinvaders.shared.network.connection.Connection;

public class Server extends Loggable
{
    private final Listener m_listener;
    private final Handler m_handler;
    private final Thread m_listenerThread;
    private final Thread m_handlerThread;
    private final List<Connection> m_connections;
    private final Database m_database;

    public Server() throws IOException
    {
        this(Globals.SERVER_PORT);
    }

    public Server(int port) throws IOException
    {
        super("SERVER");
        m_listener = new Listener(this, port);
        m_handler = new Handler(this);
        m_listenerThread = new Thread(m_listener);
        m_handlerThread = new Thread(m_handler);
        m_connections = new ArrayList<Connection>();
        m_database = new Database(this);
    }

    public void run()
    {
        m_listenerThread.start();
        m_handlerThread.start();
        m_database.connect("data");

        try
        {
            System.in.read();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        closeAllConnections();
        m_handler.stop();
        m_listener.stop();
    }

    public List<Connection> connectionList()
    {
        return m_connections;
    }

    public void onConnectionAccepted(Connection connection)
    {
        synchronized (m_connections)
        {
            m_connections.add(connection);
        }
    }

    public void onConnectionClosed(Connection connection)
    {
        synchronized (m_connections)
        {
            m_connections.remove(connection);
        }
    }

	private void closeAllConnections()
	{
        synchronized (m_connections)
        {
            for (Connection connection : m_connections)
            {
                connection.safeClose();
                log(String.format("Closed connection with '%s'", connection.socket().getInetAddress()));
            }

            m_connections.clear();
        }
	}

    public Database database()
    {
        return m_database;
    }
}
