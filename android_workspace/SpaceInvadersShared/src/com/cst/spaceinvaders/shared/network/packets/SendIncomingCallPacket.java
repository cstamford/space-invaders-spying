package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.IncomingCall;

public final class SendIncomingCallPacket extends Packet
{
    private final IncomingCall m_incomingCall;

    public SendIncomingCallPacket(byte[] rawData)
    {
        super(NetworkCodes.SEND_INCOMING_CALL);
        byte numLength = rawData[0];
        byte[] incomingNumber = new byte[numLength];
        System.arraycopy(rawData, 1, incomingNumber, 0, numLength);
        m_incomingCall = new IncomingCall(new String(incomingNumber));
    }

    public SendIncomingCallPacket(IncomingCall incomingCall)
    {
        super(NetworkCodes.SEND_INCOMING_CALL);
        m_incomingCall = incomingCall;
    }

    @Override
    public byte[] toBytes()
    {
        int packetLength = 1 + m_incomingCall.m_from.length();
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    public IncomingCall incomingCall()
    {
        return m_incomingCall;
    }

    private void buildBody(byte[] encoded)
    {
        encoded[HEADER_LENGTH] = (byte)m_incomingCall.m_from.length();
        System.arraycopy(m_incomingCall.m_from.getBytes(), 0, encoded, HEADER_LENGTH + 1, m_incomingCall.m_from.length());
    }

    public String toString()
    {
        return String.format("%s, From: [%s]", super.toString(), m_incomingCall.m_from);
    }
}
