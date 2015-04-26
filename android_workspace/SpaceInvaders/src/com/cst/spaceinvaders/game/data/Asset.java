package com.cst.spaceinvaders.game.data;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class Asset
{
	public Bitmap m_bmp;
	public int m_width;
	public int m_height;
	public Paint m_paint;
	
	public Asset(Bitmap bmp, int width, int height, Paint paint)
	{
		m_bmp = bmp;
		m_width = width;
		m_height = height;
		m_paint = paint;
	}
}
