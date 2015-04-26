package com.cst.spaceinvaders.game.entities;

import com.cst.spaceinvaders.game.data.Asset;

public class Entity
{	
	private int m_x;
	private int m_y;
	private Asset m_asset;

	public Entity(Asset asset)
	{
		this(asset, 0, 0);
	}
	
	public Entity(Asset asset, int x, int y)
	{
		m_asset = asset;
		m_x = x;
		m_y = y;
	}
	
	// Get the entity's aboslute x position
	public int getX()
	{
		return m_x;
	}
	
	// Get the entity's absolute y position
	public int getY()
	{
		return m_y;
	}
	
	// Get the entity's asset
	public Asset getAsset()
	{
		return m_asset;
	}
	
	// Set the entity's absolute x position
	public void setX(int x)
	{
		m_x = x;
	}
	
	// Set the entity's absolute y position
	public void setY(int y)
	{
		m_y = y;
	}
	
	// Set the entity's asset
	public void setAsset(Asset asset)
	{
		m_asset = asset;
	}
	
	// Set the entity's absolute position
	public void setPosition(int x, int y)
	{
		m_x = x;
		m_y = y;
	}
	
	// Move the entity by delta x and delta y
	public void move(int dx, int dy)
	{
		m_x += dx;
		m_y += dy;
	}
	
	// Simple rectangle bounding box collision between two entities
	public boolean collidesWith(Entity other)
	{
		int thisX1 	= m_x;
		int thisY1 	= m_y;
		int thisX2 	= m_asset.m_width  + thisX1;
		int thisY2 	= m_asset.m_height + thisY1;
		
		int otherX1	= other.getX();
		int otherY1	= other.getY();
		int otherX2 = other.getAsset().m_width  + otherX1;
		int otherY2 = other.getAsset().m_height + otherY1;
		
		boolean xCollision = (thisX1 <= otherX2) && (thisX2 >= otherX1);
		boolean yCollision = (thisY1 <= otherY2) && (thisY2 >= otherY1);
		
		return xCollision && yCollision;
	}
}
