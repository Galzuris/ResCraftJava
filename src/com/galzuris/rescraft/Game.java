package com.galzuris.rescraft;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.galzuris.utils.Text;
import com.galzuris.utils.YamlParser;
import com.galzuris.utils.YamlResult;

public class Game {
	private static Game instance;
	private static YamlResult trans = null;
	private static String transTag = "en";
	
	private float posx = 60;
	private float posy = 60;
	private final float speed = 128;

	public Game() {
		Game.instance = this;
	}

	public Game getInstance() {
		return Game.instance;
	}

	public void init() {
		Log.write("[game] init");		
		this.loadTranslations("/configs/translate.yml");
	}

	public void update(final float delta) {
		final GameEngineCanvas c = GameEngineCanvas.getInstance(); 
		final Graphics g = c.getGraphics();
		final String str = Game.tr("title");
		
		g.setColor(Const.ColorBlack);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		g.setColor(Const.ColorWhite);
		g.fillRoundRect((int) posx, (int) posy, Const.BlockSize, Const.BlockSize, Const.BlockSizeHalf, Const.BlockSizeHalf);
		g.drawString(str, 5, 5, 0);
		
		if (c.isActionPressed(Const.KeyActionLeft)) posx -= delta * speed;
		if (c.isActionPressed(Const.KeyActionRight)) posx += delta * speed;
		if (c.isActionPressed(Const.KeyActionUp)) posy -= delta * speed;
		if (c.isActionPressed(Const.KeyActionDown)) posy += delta * speed;
	}
	
	public static String tr(String key) {
		return trans.GetString(transTag + "." + key, key);
	}
	
	public static Vector trLangs() {	
		return trans.roots;
	}
	
	private void loadTranslations(final String path) {
		final String list = Text.LoadFromResource(path, Text.ENCODING_UTF8);
		trans = YamlParser.parse(list);
		Log.write("[game] loaded " + trans.roots.size() + " translations");
	}
}
