package com.cst.DataViewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cst.spaceinvaders.shared.data.Geolocation;

import java.util.List;

public class GeolocationsView extends ListView
{
    public GeolocationsView(Context context)
    {
        super(context);
    }

    public GeolocationsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GeolocationsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    // Populate the HighScoreView with the high scores
    public void populate(List<Geolocation> geolocations)
    {
        setAdapter(new ArrayAdapter<Geolocation>(getContext(), android.R.layout.simple_list_item_1, geolocations));
    }
}
