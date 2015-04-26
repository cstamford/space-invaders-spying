package com.cst.spaceinvaders.game.views;

import java.util.List;

import com.cst.spaceinvaders.shared.data.HighScoreEntry;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HighScoreView extends ListView
{
	public HighScoreView(Context context)
	{
		super(context);
	}
	
	public HighScoreView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public HighScoreView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	// Populate the HighScoreView with the high scores
	public void populate(List<HighScoreEntry> scores)
	{
		ArrayAdapter<HighScoreEntry> arrayAdapter = new ArrayAdapter<HighScoreEntry>(getContext(),
                android.R.layout.simple_list_item_1, scores);
		setAdapter(arrayAdapter);
	}
}
