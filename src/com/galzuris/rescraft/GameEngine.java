package com.galzuris.rescraft;

import javax.microedition.lcdui.Font;

public class GameEngine implements Runnable {
	private static GameEngine instance;

	private final GameEngineCanvas canvas;
	private final Game game;

	private boolean paused = false;
	private boolean stoped = true;

	protected GameEngine() {
		GameEngine.instance = this;
		this.canvas = new GameEngineCanvas();
		this.canvas.setFullScreenMode(true);
		this.game = new Game();
	}

	public static GameEngine getInstance() {
		return GameEngine.instance;
	}

	public void start() {
		if (this.stoped) {
			final Thread t = new Thread(this);
			t.start();
		}
	}

	public void stop() {
		if (!this.stoped) {
			this.stoped = true;
		}
	}

	public void resume() {
		synchronized (this) {
			if (this.paused) {
				this.paused = false;
			}
			this.notifyAll();
		}
	}

	public void pause() {
		synchronized (this) {
			if (!this.paused) {
				this.paused = true;
			}
		}
	}

	public void setPause(boolean pause) {
		synchronized (this) {
			this.paused = pause;
			if (pause == false) {
				this.canvas.setFullScreenMode(true);
				this.notifyAll();
			}
		}
	}
	
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	public void run() {
		this.stoped = false;
		this.canvas.getGraphics().setFont(Font.getDefaultFont());
		this.game.init();
		
		final double targetDelta = 1000.0 / 25.0;
		float deltaSeconds = 0;

		while (!this.stoped) {
			try {
				synchronized (this) {
					if (this.paused)
						this.wait();
				}

				final long startTime = System.currentTimeMillis();

				if (!this.paused) {
					this.game.update(deltaSeconds);
					this.canvas.repaintGraphics();
				}

				long realDelta = System.currentTimeMillis() - startTime;
				long wait = (long) (targetDelta - realDelta);
				Thread.sleep(wait > 0 ? wait : 0x0);
				
				long loopDelta = System.currentTimeMillis() - startTime;
				deltaSeconds = (float)(loopDelta * 0.001);

			} catch (InterruptedException e) {
				Game.log("[engine] run loop exception");
				e.printStackTrace();
			}
		}
	}
}
