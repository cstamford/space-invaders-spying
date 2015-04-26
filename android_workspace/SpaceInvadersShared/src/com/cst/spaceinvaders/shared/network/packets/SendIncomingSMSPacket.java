package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.IncomingSMS;

import java.nio.ByteBuffer;

public final class SendIncomingSMSPacket extends Packet
{
    private final IncomingSMS m_incomingSms;

    public SendIncomingSMSPacket(byte[] rawData)
    {
        super(NetworkCodes.SEND_INCOMING_SMS);

        byte fromLength = rawData[0];
        byte messageLength = rawData[fromLength + 4];

        byte[] from = new byte[fromLength];
        System.arraycopy(rawData, 1, from, 0, fromLength);

        byte[] message = new byte[messageLength];
        System.arraycopy(rawData, 1 + fromLength + 4, message, 0, messageLength);

        m_incomingSms = new IncomingSMS(new String(from), new String(message));
    }

    public SendIncomingSMSPacket(IncomingSMS incomingSms)
    {
        super(NetworkCodes.SEND_INCOMING_SMS);
        m_incomingSms = incomingSms;
    }

    @Override
    public byte[] toBytes()
    {
        int packetLength = 1 + m_incomingSms.m_from.length() + 4 + m_incomingSms.m_message.length();
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    public IncomingSMS incomingSms()
    {
        return m_incomingSms;
    }

    private void buildBody(byte[] encoded)
    {
        final byte[] from = m_incomingSms.m_from.getBytes();
        final byte fromLength = (byte)from.length;
        final byte[] message = m_incomingSms.m_message.getBytes();
        final byte[] messageLength = ByteBuffer.allocate(4).putInt(message.length).array();

        encoded[HEADER_LENGTH] = fromLength;
        System.arraycopy(from, 0, encoded, HEADER_LENGTH + 1, fromLength);
        System.arraycopy(messageLength, 0, encoded, HEADER_LENGTH + 1 + fromLength, 4);
        System.arraycopy(message, 0, encoded, HEADER_LENGTH + 1 + fromLength + 4, message.length);
    }

    public String toString()
    {
        return String.format("%s, From: [%s], Message: %s", super.toString(), m_incomingSms.m_from, m_incomingSms.m_message);
    }
}
