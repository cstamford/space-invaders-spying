package com.cst.spaceinvaders.game.controllers;

import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.game.enumerable.GameThreadState;
import com.cst.spaceinvaders.game.models.GameModel;
import com.cst.spaceinvaders.game.views.GameView;

import android.util.Log;

public class GameLogic implements Runnable
{		
	private GameModel m_model;
	private GameView m_view;
	private GameThreadState m_state;
	
	public GameLogic(GameView view)
	{
		Log.d(SpaceInvadersGame.LOG_TAG, "Gane thread created");
		
		m_view = view;
		m_model = new GameModel(view, SpaceInvadersGame.BASE_RESOLUTION_X, SpaceInvadersGame.BASE_RESOLUTION_Y);
		
		Log.d(SpaceInvadersGame.LOG_TAG, "Game model created");
		
		setState(GameThreadState.THREAD_INIT);
	}

	@Override
	public void run()
	{
		Log.d(SpaceInvadersGame.LOG_TAG, "Gane thread started");
		
		while (m_state != GameThreadState.THREAD_EXIT)
		{
			switch (m_state)
			{
				case THREAD_INIT:
				{
					setState(GameThreadState.THREAD_RUN);
					
					break;
				}
				
				case THREAD_RUN:
				{			
					doLogic();
					doFrame();

					break;
				}
				
				default:
				{
					break;
				}
			}
		}
		
		Log.d(SpaceInvadersGame.LOG_TAG, "Game thread ended");
	}
	
	// Set the state of the game
	public synchronized void setState(GameThreadState state)
	{
		if (m_state == state)
			return;
		
		Log.d(SpaceInvadersGame.LOG_TAG, "Game thread state changed to " + state);
		
		m_state = state;
	}
	
	// Command the model to update itself
	private void doLogic()
	{
		try
		{
			m_model.updateModel();
		}
		catch (Exception e)
		{
			Log.e(SpaceInvadersGame.LOG_TAG, "Exception thrown when updating game model");
			Log.e(SpaceInvadersGame.LOG_TAG, e.toString());
		}
	}
	
	// Command the view to update the screen
	private void doFrame()
	{
		try
		{
			m_view.updateView(m_model.getEntityList());
		}
		catch (Exception e)
		{
			Log.e(SpaceInvadersGame.LOG_TAG, "Exception thrown when updating game view");
			Log.e(SpaceInvadersGame.LOG_TAG, e.toString());
		}
	}
	
	// Pass the input message to the model
	public void onClickLeft()
	{
		m_model.onClickLeft();
	}
	
	// Pass the input message to the model
	public void onClickFire()
	{
		m_model.onClickFire();
	}
	
	// Pass the input message to the model
	public void onClickRight()
	{
		m_model.onClickRight();
	}
}