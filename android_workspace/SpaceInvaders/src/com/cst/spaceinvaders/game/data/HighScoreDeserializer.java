package com.cst.spaceinvaders.game.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.cst.spaceinvaders.game.SpaceInvadersGame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cst.spaceinvaders.shared.data.HighScoreEntry;

public class HighScoreDeserializer extends AsyncTask<Object, Void, List<HighScoreEntry>>
{
	@Override
	// Param0 = context
	// Param1 = file name
	// Returns list of ScoreEntry
	protected List<HighScoreEntry> doInBackground(Object... params)
	{
		Context context = (Context)params[0];
		String fileName = (String)params[1];
		List<HighScoreEntry> scoreList = new ArrayList<HighScoreEntry>();
		
		try
		{
			// Open the file
			FileInputStream inputStream = context.openFileInput(fileName);
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    
			String input = bufferedReader.readLine();
		    
			// There should only be one line
			while (input != null)
            {
				// Delimiter = whitespace
				StringTokenizer tokenizer = new StringTokenizer(input);
				
				// create a bank entry
                HighScoreEntry entry = new HighScoreEntry();
				
				// Read the name
				entry.m_name = tokenizer.nextToken();
				
				// Read the score
				entry.m_score = Integer.parseInt(tokenizer.nextToken());
			
				// Add it to the list
				scoreList.add(entry);
				
				// Read the next line
				input =  bufferedReader.readLine();
			}
			
			// Close the stream
			bufferedReader.close();
		}
		catch (FileNotFoundException e)
		{
			// Call serializer, which will make a file with our empty scorelist
			new HighScoreSerializer().execute(context, fileName, scoreList);
		}
		catch (Exception e)
		{
			Log.e(SpaceInvadersGame.LOG_TAG, "Exception thrown when trying to serialize high score table", e);
		}
		
		return scoreList;
	}
}
