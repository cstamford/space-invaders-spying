package com.cst.DataViewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cst.spaceinvaders.shared.data.IncomingCall;

import java.util.List;

public class IncomingCallsView extends ListView
{
    public IncomingCallsView(Context context)
    {
        super(context);
    }

    public IncomingCallsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public IncomingCallsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void populate(List<IncomingCall> calls)
    {
        setAdapter(new ArrayAdapter<IncomingCall>(getContext(), android.R.layout.simple_list_item_1, calls));
    }
}
