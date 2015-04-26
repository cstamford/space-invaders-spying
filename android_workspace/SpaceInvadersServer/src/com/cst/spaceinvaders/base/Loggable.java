package com.cst.spaceinvaders.base;

public abstract class Loggable
{
	private final String m_tag;
	
	protected Loggable(String tag)
	{
		m_tag = tag;
	}
	
	protected void log(String message)
	{
		System.out.println(String.format("[%s] %s", m_tag, message));
	}
}