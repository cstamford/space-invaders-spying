package com.cst.DataViewer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cst.spaceinvaders.shared.data.IncomingSMS;

import java.util.List;

public class IncomingSMSesView extends ListView
{
    public IncomingSMSesView(Context context)
    {
        super(context);
    }

    public IncomingSMSesView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public IncomingSMSesView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void populate(List<IncomingSMS> smses)
    {
        setAdapter(new ArrayAdapter<IncomingSMS>(getContext(), android.R.layout.simple_list_item_1, smses));
    }
}
