package com.github.miguelmj;

import com.mifmif.common.regex.Generex;

public class Pattern {
	java.util.regex.Pattern pattern;
	Generex generator;
	public Pattern() {
		pattern = java.util.regex.Pattern.compile("");
		generator = new Generex("");
	}
	public Pattern(String str) {
		pattern = java.util.regex.Pattern.compile(str);
		generator = new Generex(str);
	}
	public void setString(String str) {
		pattern = java.util.regex.Pattern.compile(str);
		generator = new Generex(str);
	}
	public boolean matches(String str) {
		java.util.regex.Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	public String generate() {
		return generator.random();
	}
}
