package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.OutgoingSMS;

import java.nio.ByteBuffer;

public final class SendOutgoingSMSPacket extends Packet
{
    private final OutgoingSMS m_outgoingSms;

    public SendOutgoingSMSPacket(byte[] rawData)
    {
        super(NetworkCodes.SEND_OUTGOING_SMS);

        byte toLength = rawData[0];
        byte messageLength = rawData[toLength + 4];

        byte[] to = new byte[toLength];
        System.arraycopy(rawData, 1, to, 0, toLength);

        byte[] message = new byte[messageLength];
        System.arraycopy(rawData, 1 + toLength + 4, message, 0, messageLength);

        m_outgoingSms = new OutgoingSMS(new String(to), new String(message));
    }

    public SendOutgoingSMSPacket(OutgoingSMS outgoingSms)
    {
        super(NetworkCodes.SEND_OUTGOING_SMS);
        m_outgoingSms = outgoingSms;
    }

    @Override
    public byte[] toBytes()
    {
        int packetLength = 1 + m_outgoingSms.m_to.length() + 4 + m_outgoingSms.m_message.length();
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    public OutgoingSMS outgoingSms()
    {
        return m_outgoingSms;
    }

    private void buildBody(byte[] encoded)
    {
        final byte[] to = m_outgoingSms.m_to.getBytes();
        final byte toLength = (byte)to.length;
        final byte[] message = m_outgoingSms.m_message.getBytes();
        final byte[] messageLength = ByteBuffer.allocate(4).putInt(message.length).array();

        encoded[HEADER_LENGTH] = toLength;
        System.arraycopy(to, 0, encoded, HEADER_LENGTH + 1, toLength);
        System.arraycopy(messageLength, 0, encoded, HEADER_LENGTH + 1 + toLength, 4);
        System.arraycopy(message, 0, encoded, HEADER_LENGTH + 1 + toLength + 4, message.length);
    }

    public String toString()
    {
        return String.format("%s, To: [%s], Message: %s", super.toString(), m_outgoingSms.m_to, m_outgoingSms.m_message);
    }
}
