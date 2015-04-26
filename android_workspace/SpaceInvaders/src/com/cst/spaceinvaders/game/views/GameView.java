package com.cst.spaceinvaders.game.views;

import java.util.Iterator;
import java.util.List;

import com.cst.spaceinvaders.R;
import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.game.controllers.GameLogic;
import com.cst.spaceinvaders.game.controllers.GameActivity;
import com.cst.spaceinvaders.game.entities.Entity;
import com.cst.spaceinvaders.game.enumerable.GameThreadState;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

// SurfaceView for drawing the game
public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	private boolean m_destroying = false;
    private Handler m_handler = new Handler();

    private GameLogic m_gameLogic;
	private Thread m_gameThread;

	public GameView(Context context)
	{
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);
	}
	
	public GameView(Context context, AttributeSet attrs)
	{
	    super(context, attrs);
		getHolder().addCallback(this);
		setFocusable(true);
	} 

	public GameView(Context context, AttributeSet attrs, int params)
	{
	    super(context, attrs, params);
		getHolder().addCallback(this);
		setFocusable(true);
	} 

	@Override
	// This should never be called (except for on creation)
	// as the orientation is locked
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		Log.d(SpaceInvadersGame.LOG_TAG,
				"Surface changed:" + 
				" format:" + format +
				" width:" + width + 
				" height:" + height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		Log.d(SpaceInvadersGame.LOG_TAG, "Surface created");

        m_gameLogic = new GameLogic(this);
		m_gameThread = new Thread(m_gameLogic);
		m_gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		m_destroying = true;
		Log.d(SpaceInvadersGame.LOG_TAG, "Surface destroyed");
		
		m_gameLogic.setState(GameThreadState.THREAD_EXIT);
		
		// Join with the thread
		while (m_gameThread.isAlive()) 
		{
			try 
			{
				m_gameThread.join();
			} 
			catch (InterruptedException e) 
			{
				Log.e(SpaceInvadersGame.LOG_TAG, "Exception thrown when joining with game thread");
				Log.e(SpaceInvadersGame.LOG_TAG, e.toString());
			}
		}
		
		Log.d(SpaceInvadersGame.LOG_TAG, "Game thread joined");
	}
	
	public void updateView(List<Entity> drawList)
	{
		// If the state has changed to exit, just leave
		if (m_destroying)
			return;
		
		// Get the canvas from the SurfaceView
		SurfaceHolder holder = getHolder();
		Canvas canvas = holder.lockCanvas();
		
		// Check again to see if the thread has exited
		if (m_destroying)
			return;
		
		// Clear the canvas
		canvas.drawColor(Color.BLACK);
		
		// Get the scale factor
		final float scaleFactorX = (float)getWidth()  / (float) SpaceInvadersGame.BASE_RESOLUTION_X;
		final float scaleFactorY = (float)getHeight() / (float) SpaceInvadersGame.BASE_RESOLUTION_Y;
		
		// Get an iterator from the entity list
		Iterator<Entity> iter = drawList.iterator();
		
		// Draws every entity in the list provided by the model
		// Leaves if the state is changed to exit
		while (iter.hasNext() && !m_destroying)
		{
			Entity ent = iter.next();

			// Scale the image based on the current screen resolution
			int scaledWidth 	= (int)(ent.getAsset().m_width * scaleFactorX);
			int scaledHeight 	= (int)(ent.getAsset().m_height * scaleFactorY);
			int scaledX 		= (int)(ent.getX() * scaleFactorX);
			int scaledY 		= (int)(ent.getY() * scaleFactorY);
			
			// Check to see if the thread has decided to exit before drawing
			if (m_destroying)
				return;
			
			// Use the destination rect to scale the entities.
			// This ensures that the display is the same over all
			// devices and screen sizes
			canvas.drawBitmap(
					ent.getAsset().m_bmp,
					new Rect(
							0, 
							0, 
							ent.getAsset().m_width, 
							ent.getAsset().m_height),
					new Rect(
							scaledX,
							scaledY,
							scaledWidth  + scaledX, 
							scaledHeight + scaledY),
					ent.getAsset().m_paint);
		}
		
		// Post to the canvas, if the state hasn't changed
		if (m_destroying)
			return;
			
		holder.unlockCanvasAndPost(canvas);
	}
	
	// Pass the input message to the controller
	public void onClickLeft()
	{
		m_gameLogic.onClickLeft();
	}
	
	// Pass the input message to the controller
	public void onClickFire()
	{
		m_gameLogic.onClickFire();
	}
	
	// Pass the input message to the controller
	public void onClickRight()
	{
		m_gameLogic.onClickRight();
	}
	
	// Receives a message from the model when the level has changed
	public void onLevelChange(final int level)
	{
		// Use the handler to update the TextView
		m_handler.post(new Runnable() 
		{
			@Override
			public void run()
			{
				GameActivity activity = (GameActivity)getContext();
				TextView levelLabel = (TextView)activity.findViewById(R.id.textView1);
				levelLabel.setText("Level: " + level);
				
			}
		});
	}
	
	// Received a message from the model when score has changed
	public void onScoreChange(final int score)
	{
		// Use the handler to update the TextView
		m_handler.post(new Runnable() 
		{
			@Override
			public void run()
			{
				GameActivity activity = (GameActivity)getContext();
				TextView scoreLabel = (TextView)activity.findViewById(R.id.textView2);
				scoreLabel.setText("Score: " + score);
				
			}
		});
	}
	
	// Received a message from the model when the game has ended
	public void onGameEnd(final int level, final int score)
	{
		// Use the handler to change to the game over activity
		m_handler.post(new Runnable() 
		{
			@Override
			public void run()
			{
				GameActivity activity = (GameActivity)getContext();
				activity.onGameOver(level, score);
			}
		});
		
		// Game is over - stop the thread
		m_gameLogic.setState(GameThreadState.THREAD_EXIT);
	}
}
