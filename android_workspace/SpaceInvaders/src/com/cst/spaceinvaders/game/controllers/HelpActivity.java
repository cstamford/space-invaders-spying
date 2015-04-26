package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HelpActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
}
