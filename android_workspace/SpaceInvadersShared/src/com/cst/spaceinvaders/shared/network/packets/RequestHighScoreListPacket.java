package com.cst.spaceinvaders.shared.network.packets;

public final class RequestHighScoreListPacket extends Packet
{
	public RequestHighScoreListPacket(byte[] rawData)
	{
		super(NetworkCodes.REQUEST_HIGH_SCORE_LIST);
	}

    @Override
    public byte[] toBytes()
    {
        int packetLength = 0;
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    private void buildBody(byte[] encoded)
    {
    }

    public String toString()
    {
        return super.toString();
    }
}
