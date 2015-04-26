package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.R;
import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.game.views.HighScoreView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GameOverActivity extends Activity
{
	String m_name;
	int m_score;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		int level = getIntent().getIntExtra("level", 0);
		m_score = getIntent().getIntExtra("score", 0);
		
		TextView levelView = (TextView)findViewById(R.id.textView4);
		TextView scoreView = (TextView)findViewById(R.id.textView5);
		
		levelView.setText("Level: " + level);
		scoreView.setText("Score: " + m_score);
		
		SpaceInvadersGame app = (SpaceInvadersGame)getApplication();
		HighScoreView view = (HighScoreView)findViewById(R.id.highScoreView1);
		
		view.populate(app.getHighScoreTable());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	
	public void onSubmit(View view)
	{
		SpaceInvadersGame app = (SpaceInvadersGame)getApplication();
		EditText input = (EditText)findViewById(R.id.editText1);
		
		// Get the name from the edittext box
		m_name = input.getText().toString();
		
		// Register this score with the application
		app.registerScore(m_name, m_score);
		finish();
	}
	
	public void onCancel(View view)
	{
		finish();
	}
}
