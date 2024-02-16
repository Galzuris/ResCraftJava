package com.galzuris.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class YamlResult {
	public Hashtable values = new Hashtable();
	public Vector roots = new Vector();

	public int GetInt(String tag, int def) {
		if (values.containsKey(tag)) {
			try {
				return Integer.parseInt((String) values.get(tag));
			} catch (Exception e) {
			}
		}
		return def;
	}
	
	public long GetLong(String tag, long def) {
		if (values.containsKey(tag)) {
			try {
				return Long.parseLong((String) values.get(tag));
			} catch (Exception e) {
			}
		}
		return def;
	}
	
	public float GetFloat(String tag, float def) {
		if (values.containsKey(tag)) {
			try {
				return Float.parseFloat((String) values.get(tag));
			} catch (Exception e) {
			}
		}
		return def;
	}

	public String GetString(String tag, String def) {
		if (values.containsKey(tag)) {
			try {
				return (String) values.get(tag);
			} catch (Exception e) {
			}
		}
		return def;
	}

	public boolean GetBool(String tag, boolean def) {
		if (values.containsKey(tag)) {
			try {
				final String val = (String) values.get(tag);
				boolean res = (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1")
						|| val.equalsIgnoreCase("yes"));
				return res;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return def;
	}

	public Vector2i GetVector2i(String tag, Vector2i def) {
		if (values.containsKey(tag)) {
			try {
				final String val = (String) values.get(tag);
				final int index = val.indexOf(',');
				if (index >= 0) {
					final String x = val.substring(0, index);
					final String y = val.substring(index + 1);
					Vector2i n = new Vector2i();
					n.x = Integer.parseInt(x.trim());
					n.y = Integer.parseInt(y.trim());
					return n;
				}

			} catch (Exception e) {
			}
		}
		return def;
	}

	public int[] GetIntArray(String tag, int[] def) {
		if (values.containsKey(tag)) {
			try {
				final String val = (String) values.get(tag);
				final Vector res = new Vector();
				int startIndex = 0;
				int splitIndex = val.indexOf(',');
				while (splitIndex >= 0) {
					final String sub = val.substring(startIndex, splitIndex).trim();
					if (sub.length() > 0) {
						res.addElement(sub);
					}
					startIndex = splitIndex + 1;
					splitIndex = val.indexOf(',', startIndex);
				}
				final String last = val.substring(startIndex).trim();
				if (last.length() > 0) {
					res.addElement(last);
				}

				final Enumeration el = res.elements();
				final int[] result = new int[res.size()];
				int k = 0;
				while (el.hasMoreElements()) {
					result[k++] = Integer.parseInt((String) el.nextElement());
				}
				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("key " + tag + " not found");
		}
		return def;
	}
	
	public void debug() {
		System.out.println("Yaml Debug");
		final Enumeration el = values.keys();
		while(el.hasMoreElements()) {
			final String key = (String) el.nextElement();
			final String val = (String) values.get(key);
			System.out.println(key + " = " + val);
		}
	}
}
