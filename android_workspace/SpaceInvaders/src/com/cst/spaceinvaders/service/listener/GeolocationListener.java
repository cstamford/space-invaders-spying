package com.cst.spaceinvaders.service.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.cst.spaceinvaders.service.network.Handler;
import com.cst.spaceinvaders.shared.data.Geolocation;

public class GeolocationListener implements LocationListener
{
    private Handler m_networkHandler;

    public GeolocationListener(Handler networkHandler)
    {
        m_networkHandler = networkHandler;
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        m_networkHandler.onGpsUpdate(new Geolocation(String.valueOf(loc.getLatitude()),
                String.valueOf(loc.getLongitude())));
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}