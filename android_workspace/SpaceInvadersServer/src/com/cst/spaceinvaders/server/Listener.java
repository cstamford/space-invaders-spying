package com.cst.spaceinvaders.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.cst.spaceinvaders.base.Loggable;
import com.cst.spaceinvaders.shared.network.connection.Connection;

public class Listener extends Loggable implements Runnable
{
	private final Server m_server;
	private final ServerSocket m_socket;
	private boolean m_running;
	
	public Listener(Server server, int port) throws IOException
	{
		super("LISTENER");
		m_server = server;
		m_socket = new ServerSocket(port);
	}
	
	@Override
	public void run()
	{
        log("Now listening for new connections.");
		m_running = true;
		
		while (m_running)
		{
			try
			{
				Connection newConnection = new Connection(m_socket.accept());
				log(String.format("Accepted new connection with '%s'", newConnection.socket().getInetAddress()));
				m_server.onConnectionAccepted(newConnection);
			}
			catch (IOException ignored)
			{
				// Intentionally left empty -- this only occurs if a) connection attempt fails, or b)
                // socket has already been closed. In both cases, nothing special must happen.
			}
		}	
	}

	public void stop()
	{
		m_running = false;
		
		try
		{
			m_socket.close();
		}
		catch (IOException ex)
		{
			log(String.format("'%s': Failed to close the server socket", ex.getMessage()));
		}
	}
}
