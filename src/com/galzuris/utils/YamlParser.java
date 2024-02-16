package com.galzuris.utils;

import java.util.Vector;

public class YamlParser {
	private final static int Max = 10;

	public static YamlResult parse(String content) {
		final Vector lines = Text.Explode(content, Text.NEWLINE, false);
		final YamlResult result = new YamlResult();
		final String[] names = new String[Max];
		final int count = lines.size();

		for (int i = 0; i < count; i++) {
			final String line = (String) lines.elementAt(i);
			final String trimVersion = line.trim();
			if (trimVersion.length() < 1 || trimVersion.startsWith("#") || trimVersion.startsWith("//")) {
				continue;
			}

			final int level = getLevel(line);
			if (level < Max) {
				final String tag = line.substring(0, line.indexOf(":")).trim();
				final String value = line.substring(line.indexOf(":") + 1).trim();
				names[level] = tag;
				if (level == 0) {
					result.roots.addElement(tag);
				}
				if (value.length() > 0) {
					final String fullTag = buildTag(names, level);
					result.values.put(fullTag, value);
				}
			}
		}

		return result;
	}

	private static String buildTag(String[] names, int level) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i <= level; i++) {
			buffer.append(names[i]);
			if (i < level) {
				buffer.append('.');
			}
		}
		return buffer.toString();
	}

	private static int getLevel(String line) {
		int k = 0;
		final int count = line.length();
		for (int i = 0; i < count; i++) {
			if (line.charAt(i) == ' ') {
				k++;
			} else {
				break;
			}
		}
		return k / 2;
	}
}
