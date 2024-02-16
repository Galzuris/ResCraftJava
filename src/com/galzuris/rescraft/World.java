package com.galzuris.rescraft;

import javax.microedition.lcdui.Graphics;

import com.galzuris.utils.Block;

public class World {
	private static final int mapSize = Const.MapWidth * Const.MapHeight;

	private Graphics g;
	private byte[] frontIds = new byte[mapSize];
	private byte[] backIds = new byte[mapSize];
	private boolean[] collide = new boolean[mapSize];

	private int canvasWidth, canvasHeight;
	private int widthCount, heightCount;
	private float timer = 0;

	public void init() {
		this.g = GameEngineCanvas.getInstance().getGraphics();
		this.canvasWidth = GameEngineCanvas.getInstance().getWidth();
		this.canvasHeight = GameEngineCanvas.getInstance().getHeight();
		this.widthCount = this.canvasWidth / Const.BlockSize + 2;
		this.heightCount = this.canvasHeight / Const.BlockSize + 2;
		this.clearMap();
	}

	public void update(float delta) {
		timer += delta;
		g.setColor(Const.ColorBlack);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		final int posx = (int) (Math.sin(timer) * Const.BlockSize) + Const.BlockSize;
		final int posy = (int) (Math.cos(timer) * Const.BlockSize) + Const.BlockSize;
		if (timer > Math.PI * 2) timer -= Math.PI * 2;

		for (int x = 0; x < this.widthCount; x++) {
			for (int y = 0; y < this.heightCount; y++) {
				final int px = x * Const.BlockSize - posx;
				final int py = y * Const.BlockSize - posy;
				final int index = y * Const.BlockSize + x;
				final Block bb = Game.block(backIds[index]);
				if (bb != null) {
					g.drawImage(bb.backSprite, px, py, 0);
				}

				final Block bf = Game.block(frontIds[index]);
				if (bf != null) {
					g.drawImage(bf.frontSprite, px, py, 0);
				}
			}
		}
	}

	private void clearMap() {
		for (int i = 0; i < mapSize; i++) {
			frontIds[i] = 0;
			backIds[i] = (byte) Game.random(1, 3);
			collide[i] = false;

			if (Game.random(0, 100) > 70) {
				frontIds[i] = (byte) Game.random(1, 19);
			}
		}
	}
}
