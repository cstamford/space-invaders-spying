package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.R;
import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.game.views.HighScoreView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HighScoresActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_scores);
		
		SpaceInvadersGame app = (SpaceInvadersGame)getApplication();
		HighScoreView view = (HighScoreView)findViewById(R.id.highScoreView2);
		view.populate(app.getHighScoreTable());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
}
