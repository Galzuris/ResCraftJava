package com.galzuris.rescraft;

import java.util.Vector;

import com.galzuris.utils.Text;
import com.galzuris.utils.YamlParser;
import com.galzuris.utils.YamlResult;

public class Game {
	private static Game instance;
	private static YamlResult trans = null;
	private static String transTag = "en";

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
