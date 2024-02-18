package com.galzuris.rescraft;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.galzuris.utils.*;
import com.galzuris.utils.YamlResult.Vector2i;

public class Game {
	private static final Random random = new Random();
	
	public static Image ImageTitle = null;
	public static Image ImageInventory = null;
	public static Image ImageInventorySelect = null;
	public static Image ImageInventoryRed = null;
	public static Image ImageAuthor = null;
	public static Image ImageShadowBlack = null;
	public static Image ImageShadowRed = null;
	public static Image ImageEditGrid = null;
	
	private static GameSprite spriteWhiteNumbers;
	private static GameSprite spriteGreenNumbers;
	private static Game instance;
	private static YamlResult trans = null;
	private static String transTag = "en";
	private static Block[] blocks = null;

	private World world = new World();

	public Game() {
		Game.instance = this;
	}

	public static Game getInstance() {
		return Game.instance;
	}
	
	public static void log(String text) {
		System.out.println(text);
	}

	public void init() {
		Game.log("[game] init");
		this.loadTranslations("/configs/translate.yml");
		this.loadBlocks();
		this.loadImages();
		world.init();

		// dev
		final GameEngineCanvas gc = GameEngineCanvas.getInstance();
		gc.getGraphics().setColor(Const.ColorBlack);
		gc.getGraphics().fillRect(0, 0, gc.getWidth(), gc.getHeight());
		final int logoX = (gc.getWidth() - ImageAuthor.getWidth()) / 2;
		final int logoY = (gc.getHeight() - ImageAuthor.getHeight()) / 2;
		gc.getGraphics().drawImage(ImageAuthor, logoX, logoY, 0);
		gc.repaintGraphics();
		GameEngine.getInstance().sleep(500);
		ImageAuthor = null;
		System.gc();
	}

	public void update(final float delta) {
		world.update(delta);
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

	public static int random(int min, int max) {
		return random.nextInt(max - min) + min;
	}
	
	public static void drawNumber(Graphics g, int x, int y, int num, int color) {
		final int numSize = 5;
		int size = numSize;
		if (num >= 1000) {
			size = numSize * 4;
		} else if (num >= 100) {
			size = numSize * 3;
		} else if (num >= 10) {
			size = numSize * 2;
		}

		int pos = x + size - numSize;
		int n = num;
		while (n > 0) {
			final int digit = n % 10;
			if (color == Const.ColorWhite) {
				spriteWhiteNumbers.setFrame(digit);
				spriteWhiteNumbers.draw(g, pos, y);
			} else {
				spriteGreenNumbers.setFrame(digit);
				spriteGreenNumbers.draw(g, pos, y);
			}
			n = n / 10;
			pos -= numSize;
		}
	}

	private void loadTranslations(final String path) {
		final String list = Text.LoadFromResource(path, Text.ENCODING_UTF8);
		trans = YamlParser.parse(list);
		Game.log("[game] loaded " + trans.roots.size() + " translations");
	}

	private void loadBlocks() {
		final Image blocksImage = Game.loadImage("/img/blocks.png");
		if (blocksImage == null) {
			Game.log("[game] error loading blocks sprites");
			return;
		}

		final String list = Text.LoadFromResource("/configs/blocks.yml", Text.ENCODING_UTF8);
		final YamlResult info = YamlParser.parse(list);

		final int count = info.roots.size();
		Game.log("[game] loaded " + count + " blocks");

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
				b.destroyTime = info.GetFloat(root + ".destroy.time", 1f);
				b.toolOnly = info.GetBool(root + ".tool.only", false);
				final String toolName = info.GetString(root + ".tool", "any");
				if (toolName.equals("pickaxe")) {
					b.tool = Const.ToolPickaxe;
				} else if (toolName.equals("axe")) {
					b.tool = Const.ToolAxe;
				} else if (toolName.equals("shovel")) {
					b.tool = Const.ToolShovel;
				} else if (toolName.equals("scissors")) {
					b.tool = Const.ToolScissors;
				}

				final Vector2i tex = info.GetVector2i(root + ".texture", null);
				if (tex != null) {
					b.frontSprite = Image.createImage(blocksImage, tex.x * Const.BlockSize, tex.y * Const.BlockSize,
							Const.BlockSize, Const.BlockSize, 0);
					b.backSprite = this.changeImageValue(b.frontSprite, 0.5f);
				}

				blocks[id] = b;
			}
		}
	}

	private void loadImages() {
		final Image gui = Game.loadImage("/img/gui.png");
		ImageAuthor = Image.createImage(gui, 0, 35, 49, 63, 0);
		
		if (GameEngineCanvas.getInstance().isBigScreen()) {
			ImageTitle = Image.createImage(gui, 0, 0, 128, 17, 0);
			ImageInventory = Image.createImage(gui, 104, 78, 24, 24, 0);
			ImageInventorySelect = Image.createImage(gui, 102, 102, 26, 26, 0);
			ImageInventoryRed = Image.createImage(gui, 76, 102, 26, 26, 0);
		} else {
			ImageTitle = Image.createImage(gui, 0, 119, 65, 9, 0);
			ImageInventory = Image.createImage(gui, 112, 18, 16, 16, 0);
			ImageInventorySelect = Image.createImage(gui, 110, 52, 18, 18, 0);
			ImageInventoryRed = Image.createImage(gui, 110, 34, 18, 18, 0);
		}
		
		ImageShadowBlack = createShadow(gui, 124, 73, 48);
		ImageShadowRed = createShadow(gui, 120, 73, 48);
		
		final int[] numFrames = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		final Image whiteNumbers = Image.createImage(gui, 0, 18, 48, 7, 0);
		final Image greenNumbers = Image.createImage(gui, 0, 26, 48, 7, 0);
		spriteWhiteNumbers = new GameSprite(whiteNumbers, 4, 7, 0, 0);
		spriteWhiteNumbers.setFrameSequence(numFrames);
		spriteGreenNumbers = new GameSprite(greenNumbers, 4, 7, 0, 0);
		spriteGreenNumbers.setFrameSequence(numFrames);
		
		final Image edit = Image.createImage(gui, 93, 52, 16, 16, 0);
		ImageEditGrid = this.repeatImage(edit, 3);
		
		Game.log("[game] images loaded");
	}
	
	private Image repeatImage(Image source, int repeats) {
		final int sw = source.getWidth();
		final int sh = source.getHeight();
		final int tw = sw * repeats;
		final int th = sh * repeats;
		final int[] sourceRgb = new int[sw * sh];
		final int[] targetRgb = new int[tw * th];

		source.getRGB(sourceRgb, 0, sh, 0, 0, sw, sh);
		for (int sx = 0; sx < sw; sx++) {
			for (int sy = 0; sy < sh; sy++) {
				final int sourceId = sy * sw + sx;
				for (int rx = 0; rx < repeats; rx++) {
					for (int ry = 0; ry < repeats; ry++) {
						final int targetId = (sy + ry * sh) * tw + (sx + rx * sw);
						targetRgb[targetId] = sourceRgb[sourceId];
					}
				}
			}
		}

		return Image.createRGBImage(targetRgb, tw, th, true);
	}
	
	private Image createShadow(Image source, int x, int y, int side) {
		int[] shadow = new int[1];
		source.getRGB(shadow, 0, 1, x, y, 1, 1);

		int shadowColor = shadow[0];
		final int size = side * side;
		shadow = new int[size];
		
		for (int i = 0; i < size; i++) {
			shadow[i] = shadowColor;
		}
		
		return Image.createRGBImage(shadow, side, side, true);
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
	
	public Image resizeImage(final Image temp, final int newX, final int newY) {
		final int tw = temp.getWidth();
		final int th = temp.getHeight();
		final int[] rgb = new int[tw * th];
		temp.getRGB(rgb, 0, tw, 0, 0, tw, th);
		final int[] rgb2 = reescalaArray(rgb, tw, th, newX, newY);
		final Image temp2 = Image.createRGBImage(rgb2, newX, newY, true);
		return temp2;
	}

	private int[] reescalaArray(final int[] ini, final int x, final int y, final int x2, final int y2) {
		final int[] out = new int[x2 * y2];
		for (int yy = 0; yy < y2; ++yy) {
			final int dy = yy * y / y2;
			for (int xx = 0; xx < x2; ++xx) {
				final int dx = xx * x / x2;
				out[x2 * yy + xx] = ini[x * dy + dx];
			}
		}
		return out;
	}
	
	public class GameSprite extends Sprite {
		public float FrameTime = 0.15f;
		private float delta = 0f;

		public GameSprite(Image img, int w, int h, int refX, int refY) {
			super(img, w, h);
			this.defineReferencePixel(refX, refY);
		}

		public void draw(Graphics g, int x, int y) {
			this.setRefPixelPosition(x, y);
			this.paint(g);
		}

		public void update(float delta) {
			this.delta -= delta;
			if (this.delta <= 0) {
				this.delta = FrameTime;
				this.nextFrame();
			}
		}
	}
}
