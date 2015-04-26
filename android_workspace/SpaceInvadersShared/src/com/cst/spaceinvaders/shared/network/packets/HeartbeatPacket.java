package com.cst.spaceinvaders.shared.network.packets;

public final class HeartbeatPacket extends Packet
{
    public HeartbeatPacket(byte[] rawData)
    {
        super(NetworkCodes.HEARTBEAT);
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
