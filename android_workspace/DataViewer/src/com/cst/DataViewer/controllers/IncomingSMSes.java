package com.cst.DataViewer.controllers;

import android.app.Activity;
import android.os.Bundle;
import com.cst.DataViewer.DataViewerApp;
import com.cst.DataViewer.R;
import com.cst.DataViewer.views.IncomingSMSesView;

public class IncomingSMSes extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomingsmses);
        DataViewerApp app = (DataViewerApp)getApplication();
        IncomingSMSesView view = (IncomingSMSesView)findViewById(R.id.incomingSmsesView);
        view.populate(app.incomingSmses());
    }
}