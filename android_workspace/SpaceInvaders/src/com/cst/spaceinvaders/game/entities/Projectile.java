package com.cst.spaceinvaders.game.entities;

import com.cst.spaceinvaders.game.data.Asset;

public class Projectile extends Entity
{
	private int m_velocity;
	
	public Projectile(Asset asset)
	{
		super(asset);
	}
	
	public Projectile(Asset asset, int x, int y)
	{
		super(asset, x, y);
	}
	
	public Projectile(Asset asset, int x, int y, int velocity)
	{
		super(asset, x, y);
		
		m_velocity = velocity;
	}
	
	public int getVelocity()
	{
		return m_velocity;
	}
	
	public void setVelocity(int newVelocity)
	{
		m_velocity = newVelocity;
	}
}
