package com.cst.spaceinvaders.game.data;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import com.cst.spaceinvaders.game.SpaceInvadersGame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cst.spaceinvaders.shared.data.HighScoreEntry;

public class HighScoreSerializer extends AsyncTask<Object, Void, Void>
{
	@Override
	// Param0 = context
	// Param1 = file name
	// Param2 = list of scores
	protected Void doInBackground(Object... params)
	{
		Context context = (Context)params[0];
		String fileName = (String)params[1];
		List<HighScoreEntry> scores = (List<HighScoreEntry>)params[2];
		
		try 
		{
			// Open the file
			FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		  
			Iterator<HighScoreEntry> iter = scores.iterator();
		  
			while (iter.hasNext())
			{
                HighScoreEntry entry = iter.next();
				bufferedWriter.write(entry.m_name + " " + entry.m_score);
				bufferedWriter.newLine();
			}
			
			// Close the stream
			bufferedWriter.close();
		} 
		catch (FileNotFoundException e)
		{
			Log.d(SpaceInvadersGame.LOG_TAG, "File not found exception caught. File has been made.");
		}
		catch (Exception e) 
		{
			Log.e(SpaceInvadersGame.LOG_TAG, "Exception thrown when trying to deserialize high score table", e);
		}
		
		return null;
	}
}
