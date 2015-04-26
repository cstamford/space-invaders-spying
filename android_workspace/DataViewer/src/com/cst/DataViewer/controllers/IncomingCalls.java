package com.cst.DataViewer.controllers;

import android.app.Activity;
import android.os.Bundle;
import com.cst.DataViewer.DataViewerApp;
import com.cst.DataViewer.R;
import com.cst.DataViewer.views.IncomingCallsView;

public class IncomingCalls extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomingcalls);
        DataViewerApp app = (DataViewerApp)getApplication();
        IncomingCallsView view = (IncomingCallsView)findViewById(R.id.incomingCallsView);
        view.populate(app.incomingCalls());
    }
}