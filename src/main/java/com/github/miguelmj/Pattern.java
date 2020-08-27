/**
 * @file
 * @brief This file contains the Pattern class.
 * @author Miguel Mejía J.
 * @copyright MIT License
 */
package com.github.miguelmj;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


import com.mifmif.common.regex.Generex;

/**
 * Input matcher and output generator.
 */
public class Pattern {
	private java.util.regex.Pattern pattern;
	private Generex generator;
	private List<String> variables;
	private String generatorTemplate;
	/**
	 * Constructs the Pattern with an empty string.
	 */
	public Pattern() {
		pattern = java.util.regex.Pattern.compile("");
		generator = new Generex("");
		variables = new ArrayList<String>();
	}
	/**
	 * Constructs the Pattern with the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public Pattern(String str) {
		setString(str);
	}
	/**
	 * Changes the Pattern to use the regex parsed in str.
	 * @param str Regular expression to parse.
	 */
	public void setString(String str) {
		pattern = java.util.regex.Pattern.compile(str);
		variables = new ArrayList<String>();
		
		// First we look for variables in the pattern
		//String varPattStr = "\\(\\?\\<(?<groupname>[a-zA-Z][a-zA-Z0-9]*)\\>[^)]\\)";
		//String varPattStr = "\\(\\?<([^>]?)>[^)]\\)";
		String varPattStr = "\\(\\?<(?<groupname>[^>]*)>[^)]*\\)";

		generatorTemplate = str;
		
		java.util.regex.Pattern varPatt = java.util.regex.Pattern.compile(varPattStr);
				
		Matcher varFinder = varPatt.matcher(generatorTemplate);
		while(varFinder.find()) {
			String var = varFinder.group(1);
			variables.add(var);
			generatorTemplate = varFinder.replaceFirst("\\$\\{${groupname}\\}");
			varFinder.reset(generatorTemplate);
		}
		
		// Now make the generex according to
		if(variables.isEmpty()) {
			generator = new Generex(generatorTemplate);
		}
	}
	/**
	 * Checks if str matches the pattern.
	 * @param str String to compare against the pattern.
	 * @return True if the string matches the pattern.
	 */
	public boolean matches(String str) {
		java.util.regex.Matcher matcher = pattern.matcher(str);
		boolean match = matcher.matches();
		if(match) 
			for(String var: variables) {
				String value = matcher.group(var);
				Script.pyMachine.set(var, value);
			}
		return match;
	}
	/**
	 * Generate a string that matches the pattern.
	 * @return A random string that matches the pattern.
	 */
	public String generate() {
		if(!variables.isEmpty()) {
			String varPattStr = "\\$\\{(?<var>[^}]+)\\}";
			String generatorStr = generatorTemplate;
			Matcher varFinder = java.util.regex.Pattern.compile(varPattStr).matcher(generatorStr);
			while(varFinder.find()) {
				String value = Script.pyMachine.get(varFinder.group("var")).asString();
				generatorStr = varFinder.replaceFirst(value);
				varFinder.reset(generatorStr);
			}
			generator = new Generex(generatorStr);
		}
		return generator.random();
	}
	/**
	 * Return the regex parsed to construct the pattern.
	 */
	public String toString() {
		return pattern.toString();
	}
}
