package com.cst.spaceinvaders.server;

import java.io.IOException;

public class EntryPoint
{
	public static void main(String[] args)
	{
		try
		{
			new Server().run();
		}
		catch (IOException e)
		{
			System.out.println("Unrecoverable exception caught.");
			e.printStackTrace();
		}
	}	
}
