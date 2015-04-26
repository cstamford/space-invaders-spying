package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.R;
import com.cst.spaceinvaders.game.views.GameView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class GameActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	
	// Pass a message to the view on user input
	public void onClickLeft(View view)
	{
		GameView gameView = (GameView) findViewById(R.id.gameView1);
		gameView.onClickLeft();
	}
	
	// Pass a message to the view on user input
	public void onClickFire(View view)
	{
		GameView gameView = (GameView) findViewById(R.id.gameView1);
		gameView.onClickFire();
	}
	
	// Pass a message to the view on user input
	public void onClickRight(View view)
	{
		GameView gameView = (GameView) findViewById(R.id.gameView1);
		gameView.onClickRight();
	}
	
	// Receive a message from the view that the game is over
	public void onGameOver(final int level, final int score)
	{
		Intent intent = new Intent(this, GameOverActivity.class);
		
		// Pass the new activity the level and score reached
		intent.putExtra("level", level);
		intent.putExtra("score", score);
		
		// Start the new activity
		startActivity(intent);
		
		// Finish this view, so the user returns to the main menu after the game over screen
		finish();
	}
}
