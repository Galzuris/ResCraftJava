package com.galzuris.utils;

import javax.microedition.lcdui.Image;

import com.galzuris.rescraft.Const;

public class Block {
	public int id;
	public boolean collide = true;
	public boolean toolOnly = false;
	public byte tool = Const.ToolAny;
	public Image frontSprite = null;
	public Image backSprite = null;	
	public float destroyTime = 1f;
}
