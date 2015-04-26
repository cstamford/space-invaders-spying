package com.cst.spaceinvaders.game.entities;

import com.cst.spaceinvaders.game.data.Asset;

public class Enemy extends Entity
{
	public Enemy(Asset asset)
	{
		super(asset);
	}
	
	public Enemy(Asset asset, int x, int y)
	{
		super(asset, x, y);
	}
}
