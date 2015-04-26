package com.cst.DataViewer.controllers;

import android.app.Activity;
import android.os.Bundle;
import com.cst.DataViewer.DataViewerApp;
import com.cst.DataViewer.R;
import com.cst.DataViewer.views.OutgoingCallsView;

public class OutgoingCalls extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoingcalls);
        DataViewerApp app = (DataViewerApp)getApplication();
        OutgoingCallsView view = (OutgoingCallsView)findViewById(R.id.outgoingCallsView);
        view.populate(app.outgoingCalls());
    }
}