package com.cst.spaceinvaders.shared.data;

public final class Geolocation
{
    public String m_latitude;
    public String m_longitude;

    public Geolocation(String lat, String longi)
    {
        m_latitude = lat;
        m_longitude = longi;
    }

    @Override
    public String toString()
    {
        return String.format("Lat: %s, Long: %s", m_latitude, m_longitude);
    }
}
