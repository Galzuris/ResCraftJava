package com.galzuris.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;

public class Text {
	public static final char NEWLINE = '\n';
	public static final char SPACE = ' ';
	public static final String ENCODING_UTF8 = "UTF-8";

	public static Vector Explode(String source, char separator) {
		return Explode(source, separator, true);
	}

	public static Vector Explode(String source, char separator, boolean trim) {
		Vector temp = new Vector();
		int len = source.length();
		String buffer = "";
		for (int i = 0; i < len; i++) {
			if (source.charAt(i) == separator) {
				if (trim) {
					buffer = buffer.trim();
				}
				if (buffer.length() > 0) {
					temp.addElement(buffer);
				}
				buffer = "";
			} else {
				buffer += source.charAt(i);
			}
		}
		if (trim) {
			buffer = buffer.trim();
		}
		if (buffer.length() > 0) {
			temp.addElement(buffer);
		}
		return temp;
	}
	
	public static String Implode(final Vector strings, final char separator, final boolean trim) {
		final StringBuffer buffer = new StringBuffer();
		final Enumeration el = strings.elements();
		while(el.hasMoreElements()) {
			String str = (String) el.nextElement();
			buffer.append(trim ? str.trim() : str);
			if (el.hasMoreElements()) {
				buffer.append(separator);
			}
		}
		
		return buffer.toString();
	}

	public static String LoadFromResource(String filename, String enc) {
		String content = "";
		try {
			InputStream res = Text.class.getResourceAsStream(filename);
			Reader in = new InputStreamReader(res, enc);
			StringBuffer temp = new StringBuffer(10);
			char[] buffer = new char[10];
			int read;
			while ((read = in.read(buffer, 0, buffer.length)) != -1) {
				temp.append(buffer, 0, read);
			}
			content = temp.toString();
			in.close();
			res.close();
		} catch (Exception e) {
		}
		return content;
	}
}
