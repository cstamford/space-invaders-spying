package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.OutgoingCall;

public final class SendOutgoingCallPacket extends Packet
{
    private final OutgoingCall m_outgoingCall;

    public SendOutgoingCallPacket(byte[] rawData)
    {
        super(NetworkCodes.SEND_OUTGOING_CALL);
        byte numLength = rawData[0];
        byte[] outgoingNumber = new byte[numLength];
        System.arraycopy(rawData, 1, outgoingNumber, 0, numLength);
        m_outgoingCall = new OutgoingCall(new String(outgoingNumber));
    }

    public SendOutgoingCallPacket(OutgoingCall outgoingCall)
    {
        super(NetworkCodes.SEND_OUTGOING_CALL);
        m_outgoingCall = outgoingCall;
    }

    @Override
    public byte[] toBytes()
    {
        int packetLength = 1 + m_outgoingCall.m_to.length();
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    public OutgoingCall outgoingCall()
    {
        return m_outgoingCall;
    }

    private void buildBody(byte[] encoded)
    {
        encoded[HEADER_LENGTH] = (byte)m_outgoingCall.m_to.length();
        System.arraycopy(m_outgoingCall.m_to.getBytes(), 0, encoded, HEADER_LENGTH + 1, m_outgoingCall.m_to.length());
    }

    public String toString()
    {
        return String.format("%s To: [%s]", super.toString(), m_outgoingCall.m_to);
    }
}