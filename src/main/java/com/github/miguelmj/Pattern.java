/**
 * @file
 * @brief This file contains the Pattern class.
 * @author Miguel Mejía J.
 * @copyright MIT License
 */
package com.github.miguelmj;

import com.mifmif.common.regex.Generex;

/**
 * Input matcher and output generator.
 */
public class Pattern {
	private java.util.regex.Pattern pattern;
	private Generex generator;
	/**
	 * Constructs the Pattern with an empty string.
	 */
	public Pattern() {
		pattern = java.util.regex.Pattern.compile("");
		generator = new Generex("");
	}
	/**
	 * Constructs the Pattern with the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public Pattern(String str) {
		pattern = java.util.regex.Pattern.compile(str);
		generator = new Generex(str);
	}
	/**
	 * Changes the Pattern to use the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public void setString(String str) {
		pattern = java.util.regex.Pattern.compile(str);
		generator = new Generex(str);
	}
	/**
	 * Checks if str matches the pattern.
	 * @param str String to compare against the pattern.
	 * @return True if the string matches the pattern.
	 */
	public boolean matches(String str) {
		java.util.regex.Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * Generate a string that matches the pattern.
	 * @return A random string that matches the pattern.
	 */
	public String generate() {
		return generator.random();
	}
	/**
	 * Return the regex parsed to construct the pattern.
	 */
	public String toString() {
		return pattern.toString();
	}
}
