package com.galzuris.rescraft;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import com.galzuris.utils.*;

public class Game {
	private static Game instance;
	private static YamlResult trans = null;
	private static String transTag = "en";
	private static Block[] blocks = null;

	public Game() {
		Game.instance = this;
	}

	public Game getInstance() {
		return Game.instance;
	}

	public void init() {
		Log.write("[game] init");
		this.loadTranslations("/configs/translate.yml");
		this.loadBlocks();
	}

	public void update(final float delta) {
	}

	public static String tr(String key) {
		return trans.GetString(transTag + "." + key, key);
	}

	public static Vector trLangs() {
		return trans.roots;
	}

	public static Block block(int id) {
		return blocks[id];
	}

	public static Image loadImage(String path) {
		try {
			return Image.createImage(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void loadTranslations(final String path) {
		final String list = Text.LoadFromResource(path, Text.ENCODING_UTF8);
		trans = YamlParser.parse(list);
		Log.write("[game] loaded " + trans.roots.size() + " translations");
	}

	private void loadBlocks() {
		final Image blocksImage = Game.loadImage("/img/blocks.png");
		if (blocksImage == null) {
			Log.write("[game] error loading blocks sprites");
			return;
		}

		final String list = Text.LoadFromResource("/configs/blocks.yml", Text.ENCODING_UTF8);
		final YamlResult info = YamlParser.parse(list);

		final int count = info.roots.size();
		Log.write("[game] loaded " + count + " blocks");

		int max = 0;
		for (int i = 0; i < count; i++) {
			final String root = (String) info.roots.elementAt(i);
			final int id = info.GetInt(root + ".id", max);
			if (id > max) {
				max = id;
			}
		}

		blocks = new Block[max + 1];
		for (int i = 0; i < count; i++) {
			final String root = (String) info.roots.elementAt(i);
			final int id = info.GetInt(root + ".id", -1);
			if (id > 0) {
				final Block b = new Block();
				b.id = id;

				final Vector2i tex = info.GetVector2i(root + ".texture", null);
				if (tex != null) {
					b.frontSprite = Image.createImage(blocksImage, tex.x * Const.BlockSize, tex.y * Const.BlockSize,
							Const.BlockSize, Const.BlockSize, 0);
					b.backSprite = this.changeImageValue(b.frontSprite, 0.6f);
				}

				blocks[id] = b;
			}
		}
	}

	private Image changeImageValue(Image source, float k) {
		final int w = source.getWidth();
		final int h = source.getHeight();
		final int size = w * h;
		final int[] data = new int[size];
		source.getRGB(data, 0, w, 0, 0, w, h);

		for (int i = 0; i < size; i++) {
			final int a = data[i] >> 24 & 0xFF;
			int r = (int) ((data[i] >> 16 & 0xFF) * k);
			int g = (int) ((data[i] >> 8 & 0xFF) * k);
			int b = (int) ((data[i] & 0xFF) * k);
			if (r < 0)
				r = 0;
			else if (r > 255) {
				r = 255;
			}
			if (g < 0)
				g = 0;
			else if (g > 255) {
				g = 255;
			}
			if (b < 0)
				b = 0;
			else if (b > 255) {
				b = 255;
			}
			data[i] = (a << 24 | r << 16 | g << 8 | b);
		}
		return Image.createRGBImage(data, w, h, true);
	}
}
