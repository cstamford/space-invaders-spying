package com.cst.spaceinvaders.shared.network.packets;

import com.cst.spaceinvaders.shared.data.Geolocation;

public final class SendGeolocationPacket extends Packet
{
    private final Geolocation m_geolocation;

    public SendGeolocationPacket(byte[] rawData)
    {
        super(NetworkCodes.SEND_GEOLOCATION);

        byte latLength = rawData[0];
        byte longLength = rawData[latLength + 1];

        byte[] lat = new byte[latLength];
        System.arraycopy(rawData, 1, lat, 0, latLength);

        byte[] longi = new byte[longLength];
        System.arraycopy(rawData, 1 + latLength + 1, longi, 0, longLength);

        m_geolocation = new Geolocation(new String(lat), new String(longi));
    }

    public SendGeolocationPacket(Geolocation location)
    {
        super(NetworkCodes.SEND_GEOLOCATION);
        m_geolocation = location;
    }

    @Override
    public byte[] toBytes()
    {
        int packetLength = 1 + m_geolocation.m_latitude.length() + 1 + m_geolocation.m_longitude.length();
        byte[] encoded = new byte[HEADER_LENGTH + packetLength];

        buildHeader(encoded, (short) packetLength);
        buildBody(encoded);

        return encoded;
    }

    public Geolocation geplocation()
    {
        return m_geolocation;
    }

    private void buildBody(byte[] encoded)
    {
        encoded[HEADER_LENGTH] = (byte) m_geolocation.m_latitude.length();
        byte[] lati = m_geolocation.m_latitude.getBytes();
        System.arraycopy(lati, 0, encoded, 1 + HEADER_LENGTH, lati.length);
        encoded[HEADER_LENGTH + 1 + lati.length] = (byte) m_geolocation.m_longitude.length();
        byte[] longi = m_geolocation.m_longitude.getBytes();;
        System.arraycopy(longi, 0, encoded, 1 + HEADER_LENGTH + 1 + lati.length, longi.length);
    }

    public String toString()
    {
        return String.format("%s, Lat: (%s), Long: (%s)", super.toString(),
                m_geolocation.m_latitude, m_geolocation.m_longitude);
    }
}
