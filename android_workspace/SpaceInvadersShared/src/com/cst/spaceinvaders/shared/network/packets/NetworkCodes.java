package com.cst.spaceinvaders.shared.network.packets;

public final class NetworkCodes
{
	// Client -> Server
	public static final byte REQUEST_HIGH_SCORE_LIST = 1;
	public static final byte SEND_HIGH_SCORE         = 2;
	public static final byte SEND_INCOMING_SMS       = 3;
	public static final byte SEND_OUTGOING_SMS       = 4;
    public static final byte SEND_INCOMING_CALL      = 5;
    public static final byte SEND_OUTGOING_CALL      = 6;
	public static final byte SEND_GEOLOCATION		 = 7;
    public static final byte REQUEST_DATA            = 8;
	
	// Server -> Client
    public static final byte HEARTBEAT               = 9;
}
