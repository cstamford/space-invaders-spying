package com.cst.spaceinvaders.shared.data;

public final class HighScoreEntry implements Comparable<HighScoreEntry>
{
	public String m_name;
	public int m_score;
	
	public HighScoreEntry()
	{ 
		this("undefined", 0);
	}
	
	public HighScoreEntry(HighScoreEntry old)
	{
		this(old.m_name, old.m_score);
	}
	
	public HighScoreEntry(String name, int score)
	{
		m_name = name;
		m_score = score;
	}
	
	@Override
	public String toString() 
	{
		return m_name + ": " + m_score;
	}

	@Override
	// Compares two scores, returning the difference
	public int compareTo(HighScoreEntry another)
	{
		return another.m_score - m_score;
	}
}
