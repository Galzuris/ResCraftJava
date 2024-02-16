package com.galzuris.rescraft;

import javax.microedition.lcdui.game.GameCanvas;

public class Const {
	public static final int BlockSize = 16;
	public static final int BlockSizeHalf = 8;
	public static final int MapWidth = 128;
	public static final int MapHeight = 128;
	
	public static final int KeyActionUp = GameCanvas.UP;
	public static final int KeyActionDown = GameCanvas.DOWN;
	public static final int KeyActionLeft = GameCanvas.LEFT;
	public static final int KeyActionRight = GameCanvas.RIGHT;
	public static final int KeyActionFire = GameCanvas.FIRE;
	
	public static final int KeySoftLeft = -6;
	public static final int KeySoftRight = -7;
	
	public static final int ColorBlack = 0x000000;
	public static final int ColorWhite = 0xFFFFFF;

	public static final byte ToolAny = 0;
	public static final byte ToolPickaxe = 1;
	public static final byte ToolShovel = 2;
	public static final byte ToolAxe = 3;
	public static final byte ToolScissors = 4;
}
