package com.cst.DataViewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cst.spaceinvaders.shared.data.OutgoingCall;

import java.util.List;

public class OutgoingCallsView extends ListView
{
    public OutgoingCallsView(Context context)
    {
        super(context);
    }

    public OutgoingCallsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OutgoingCallsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void populate(List<OutgoingCall> calls)
    {
        setAdapter(new ArrayAdapter<OutgoingCall>(getContext(), android.R.layout.simple_list_item_1, calls));
    }
}
