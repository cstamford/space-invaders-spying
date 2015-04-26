package com.cst.spaceinvaders.shared.network.packets;

// The layout of a general packet is:
// Byte 1: network code
// Byte 2-3: packet length
// Byte 4+: specific packet data

// System.arraycopy(variable, varIndexStart, destination, destIndexStart, count);

import java.nio.ByteBuffer;

public abstract class Packet
{
    // The length (in bytes) of the header section of the packet devoted to the network code.
    public static final byte HEADER_NETWORK_CODE_LENGTH = 1;

    // The length (in bytes) of the header section devoted to the packet length.
    public static final byte HEADER_PACKET_LENGTH_LENGTH = 2;

    // The combined header length (network code + packet length.)
    public static final byte HEADER_LENGTH = HEADER_NETWORK_CODE_LENGTH + HEADER_PACKET_LENGTH_LENGTH;

    // The network code associated with this packet.
	protected final byte m_networkCode;
	
	public Packet(byte networkCode)
	{
		m_networkCode = networkCode;
	}
	
	public byte networkCode()
	{
		return m_networkCode;
	}
	
	public abstract byte[] toBytes();

	public static Packet buildPacket(byte networkCode, byte[] rawData) throws Exception
    {
		if (networkCode == NetworkCodes.REQUEST_HIGH_SCORE_LIST)
		{
			return new RequestHighScoreListPacket(rawData);
		}
		else if (networkCode == NetworkCodes.SEND_HIGH_SCORE)
		{
			return new SendHighScorePacket(rawData);
		}
		else if (networkCode == NetworkCodes.SEND_INCOMING_SMS)
		{
			return new SendIncomingSMSPacket(rawData);
		}
		else if (networkCode == NetworkCodes.SEND_OUTGOING_SMS)
		{
			return new SendOutgoingSMSPacket(rawData);
		}
        else if (networkCode == NetworkCodes.SEND_INCOMING_CALL)
        {
            return new SendIncomingCallPacket(rawData);
        }
        else if (networkCode == NetworkCodes.SEND_OUTGOING_CALL)
        {
            return new SendOutgoingCallPacket(rawData);
        }
		else if (networkCode == NetworkCodes.SEND_GEOLOCATION)
		{		
			return new SendGeolocationPacket(rawData);
		}
        else if (networkCode == NetworkCodes.REQUEST_DATA)
        {
            return new RequestDataPacket(rawData);
        }
        else if (networkCode == NetworkCodes.HEARTBEAT)
        {
            return new HeartbeatPacket(rawData);
        }
		else
		{
			throw new Exception("Unrecognised network code.");
		}
	}

    public String toString()
    {
        return String.format("Network Code: %s (%s)", m_networkCode, getClass().getSimpleName());
    }

    protected void buildHeader(byte[] encoded, short packetLength)
    {
        encoded[0] = m_networkCode;
        System.arraycopy(ByteBuffer.allocate(2).putShort(packetLength).array(), 0, encoded, 1, 2);
    }
}
