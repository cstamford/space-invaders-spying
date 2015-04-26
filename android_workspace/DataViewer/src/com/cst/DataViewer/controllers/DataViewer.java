package com.cst.DataViewer.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.cst.DataViewer.R;
import com.cst.spaceinvaders.shared.data.Geolocation;
import com.cst.spaceinvaders.shared.data.IncomingCall;
import com.cst.spaceinvaders.shared.data.IncomingSMS;
import com.cst.spaceinvaders.shared.data.OutgoingCall;

import java.util.ArrayList;
import java.util.List;

public class DataViewer extends Activity
{
    private List<IncomingCall> m_incomingCalls;
    private List<OutgoingCall> m_outgoingCalls;
    private List<IncomingSMS> m_incomingSmses;
    private List<Geolocation> m_geolocations;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_incomingCalls = new ArrayList<IncomingCall>();
        m_outgoingCalls = new ArrayList<OutgoingCall>();
        m_incomingSmses = new ArrayList<IncomingSMS>();
        m_geolocations = new ArrayList<Geolocation>();
    }

    public void onClickIncomingCalls(View view)
    {
        startActivity(new Intent(this, IncomingCalls.class));
    }

    public void onClickOutgoingCalls(View view)
    {
        startActivity(new Intent(this, OutgoingCalls.class));
    }

    public void onClickIncomingSmses(View view)
    {
        startActivity(new Intent(this, IncomingSMSes.class));
    }

    public void onClickGeolocations(View view)
    {
        startActivity(new Intent(this, Geolocations.class));
    }

}
