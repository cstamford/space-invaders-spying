package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.HighScoreEntry;

import java.nio.ByteBuffer;

public final class SendHighScorePacket extends Packet
{
	private final HighScoreEntry m_highScoreEntry;
	
	public SendHighScorePacket(byte[] rawData)
    {
		super(NetworkCodes.SEND_HIGH_SCORE);

        byte nameLength = rawData[0];
        byte scoreLength = 4;

		byte[] name = new byte[nameLength];
		System.arraycopy(rawData, 1, name, 0, nameLength);

		byte[] score = new byte[scoreLength];
		System.arraycopy(rawData, 1 + nameLength, score, 0, scoreLength);

        m_highScoreEntry = new HighScoreEntry(new String(name), ByteBuffer.wrap(score).getInt());
	}
	
	public SendHighScorePacket(HighScoreEntry score)
	{
		super(NetworkCodes.SEND_HIGH_SCORE);
        m_highScoreEntry = score;
	}

	@Override
	public byte[] toBytes()
	{
        // Name length -> Name -> Score
		int packetLength = 1 + m_highScoreEntry.m_name.length() + 4;
		byte[] encoded = new byte[HEADER_LENGTH + packetLength];

		buildHeader(encoded, (short) packetLength);
		buildBody(encoded);
		
		return encoded;
	}

    public HighScoreEntry highScoreEntry()
    {
     return m_highScoreEntry;
    }

    private void buildBody(byte[] encoded)
    {
        encoded[HEADER_LENGTH] = (byte) m_highScoreEntry.m_name.length();
        byte[] name = m_highScoreEntry.m_name.getBytes();
        System.arraycopy(name, 0, encoded, 1 + HEADER_LENGTH, name.length);
        byte[] score = ByteBuffer.allocate(4).putInt(m_highScoreEntry.m_score).array();
        System.arraycopy(score, 0, encoded, 1 + HEADER_LENGTH + name.length, score.length);
    }

	public String toString()
	{
		return String.format("%s, Name: %s, Score: %s", super.toString(), m_highScoreEntry.m_name, m_highScoreEntry.m_score);
	}
}
