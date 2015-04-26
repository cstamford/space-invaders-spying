package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenuActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	
	public void onClickPlayGame(View view)
	{
		startActivity(new Intent(this, GameActivity.class));
	}
	
	public void onClickHelp(View view)
	{
		startActivity(new Intent(this, HelpActivity.class));
	}
	
	public void onClickHighScores(View view)
	{
		startActivity(new Intent(this, HighScoresActivity.class));
	}
	
	public void onClickExit(View view)
	{
		this.finish();
	}
}
