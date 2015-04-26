package com.cst.spaceinvaders.debugclient;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.cst.spaceinvaders.shared.data.Globals;
import com.cst.spaceinvaders.shared.network.connection.Connection;
import com.cst.spaceinvaders.shared.network.packets.*;

public class EntryPoint
{
	
	public static void main(String[] args)
	{
		try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(Globals.SERVER_IP, Globals.SERVER_PORT));
            Connection connection = new Connection(socket);
            connection.write(new RequestDataPacket(null));

            while (true && connection.connected())
            {
                if (connection.readable())
                    printPacket(connection.read());
                else
                    Thread.sleep(50);
            }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

    private static void printPacket(Packet packet)
    {
        byte[] raw = packet.toBytes();

        for (int i = 0; i < raw.length; ++i)
        {
            if (i == 0)
                System.out.print("[HEADER]");
            else if (i == 3)
                System.out.print("[BODY]");

            System.out.print(" " + raw[i] + " ");
        }

        System.out.println();
    }
}
