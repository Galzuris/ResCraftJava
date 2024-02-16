package com.galzuris.rescraft;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class GameEngineCanvas extends GameCanvas {
	private static GameEngineCanvas instance;

	private class KeyData {
		public int Key;
		public boolean Current = false;
		public boolean Prev = false;

		public KeyData(int key) {
			this.Key = key;
		}
	}

	private final KeyData[] keys;
	private final KeyData[] actions;

	protected GameEngineCanvas() {
		super(false);
		GameEngineCanvas.instance = this;
		Log.write("[canvas] is_touch: " + this.hasPointerEvents());

		keys = new KeyData[2];
		keys[0] = new KeyData(Const.KeySoftLeft);
		keys[1] = new KeyData(Const.KeySoftRight);

		actions = new KeyData[5];
		actions[0] = new KeyData(Const.KeyActionUp);
		actions[1] = new KeyData(Const.KeyActionDown);
		actions[2] = new KeyData(Const.KeyActionLeft);
		actions[3] = new KeyData(Const.KeyActionRight);
		actions[4] = new KeyData(Const.KeyActionFire);
	}

	public static GameEngineCanvas getInstance() {
		return GameEngineCanvas.instance;
	}

	public Graphics getGraphics() {
		return super.getGraphics();
	}

	public boolean isKeyPressed(int key) {
		return this.checkPressed(key, keys);
	}
	
	public boolean isKeyClicked(int key) {
		return this.checkClicked(key, keys);
	}
	
	public boolean isActionPressed(int key) {
		return this.checkPressed(key, actions);
	}
	
	public boolean isActionClicked(int key) {
		return this.checkClicked(key, actions);
	}

	public void keysStep() {
		this.updateKeys(keys);
		this.updateKeys(actions);
	}

	protected void keyPressed(int keyCode) {
		this.keyEvent(keyCode, true);
	}

	protected void keyReleased(int keyCode) {
		this.keyEvent(keyCode, false);
	}
	
	private boolean checkPressed(int key, KeyData[] keys) {	
		final int len = keys.length;
		for (int i = 0; i < len; i++) {
			if (keys[i].Key == key) {
				return keys[i].Current;
			}
		}
		return false;
	}
	
	private boolean checkClicked(int key, KeyData[] keys) {
		final int len = keys.length;
		for (int i = 0; i < len; i++) {
			if (keys[i].Key == key) {
				return keys[i].Current == false && keys[i].Prev == true;
			}
		}
		return false;
	}

	private void keyEvent(int key, boolean down) {
		this.setKey(keys, key, down);

		final int action = this.getGameAction(key);
		if (action != 0) {
			this.setKey(actions, action, down);
		}
	}

	private void setKey(final KeyData[] keys, final int key, final boolean down) {
		final int len = keys.length;
		for (int i = 0; i < len; i++) {
			if (keys[i].Key == key) {
				keys[i].Current = down;
				return;
			}
		}
	}

	private void updateKeys(final KeyData[] keys) {
		final int len = keys.length;
		for (int i = 0; i < len; i++) {
			keys[i].Prev = keys[i].Current;
		}
	}
}
