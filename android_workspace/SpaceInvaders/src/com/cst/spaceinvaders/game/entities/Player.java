package com.cst.spaceinvaders.game.entities;

import com.cst.spaceinvaders.game.data.Asset;

public class Player extends Entity
{
	public Player(Asset asset)
	{
		super(asset);
	}
	
	public Player(Asset asset, int x, int y)
	{
		super(asset, x, y);
	}
}
