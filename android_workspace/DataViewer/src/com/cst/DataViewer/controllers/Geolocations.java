package com.cst.DataViewer.controllers;

import android.app.Activity;
import android.os.Bundle;
import com.cst.DataViewer.DataViewerApp;
import com.cst.DataViewer.R;
import com.cst.DataViewer.views.GeolocationsView;

public class Geolocations extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocations);
        DataViewerApp app = (DataViewerApp)getApplication();
        GeolocationsView view = (GeolocationsView)findViewById(R.id.geolocationsView);
        view.populate(app.geolocations());
    }
}